package com.liyuan.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.liyuan.domain.File;
import com.liyuan.entity.BucketObject;
import com.liyuan.entity.StorageBucket;
import com.liyuan.service.FileService;
import com.liyuan.utils.ImgUtils;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author liyuan
 * @date 2022/10/2
 * @project Oauth2-cloud
 */
@Component
@ConfigurationProperties(prefix = "minio")
@Slf4j
public class MinioService implements InitializingBean {

    /**
     * MinIO的API地址
     */
    @Setter
    private String endpoint;

    /**
     * 用户名
     */
    @Setter
    private String accessKey;

    /**
     * 密钥
     */
    @Setter
    private String secretKey;

    /**
     * 自定义域名(非必须)
     */
    @Setter
    private String customDomain;

    /**
     * 存储桶名称，默认微服务单独一个存储桶
     */
    @Setter
    private String defaultBucket;

    @Value("${minio.img_compression_enabled:true}")
    private boolean imgCompressionEnabled;

    private MinioClient minioClient;

    @Autowired
    private FileService fileService;

    @Override
    public void afterPropertiesSet() {
        log.info("初始化 MinIO 客户端..."+endpoint);
        Assert.notBlank(endpoint, "MinIO endpoint不能为空");
        Assert.notBlank(accessKey, "MinIO accessKey不能为空");
        Assert.notBlank(secretKey, "MinIO secretKey不能为空");
        this.minioClient = MinioClient.builder()
                //.endpoint(endpoint, 443, true)
                .endpoint(endpoint).credentials(accessKey, secretKey).build();
    }

    /**
     * 创建存储桶(存储桶不存在)
     *
     * @param bucketName
     */
    @SneakyThrows
    public void createBucketIfAbsent(String bucketName) {
        BucketExistsArgs bucketExistsArgs = BucketExistsArgs.builder().bucket(bucketName).build();
        if (!minioClient.bucketExists(bucketExistsArgs)) {
            MakeBucketArgs makeBucketArgs = MakeBucketArgs.builder().bucket(bucketName).build();
            minioClient.makeBucket(makeBucketArgs);

            // 设置存储桶访问权限为PUBLIC， 如果不配置，则新建的存储桶默认是PRIVATE，则存储桶文件会拒绝访问 Access Denied
            SetBucketPolicyArgs setBucketPolicyArgs = SetBucketPolicyArgs
                    .builder().bucket(bucketName)
                    .config(publicBucketPolicy(bucketName).toString())
                    .build();
            minioClient.setBucketPolicy(setBucketPolicyArgs);
        }
    }

    /**
     * 删除存储桶(存储桶存在)
     *
     * @param bucketName
     */
    @SneakyThrows
    public void deleteBucketIfExists(String bucketName) {
        BucketExistsArgs bucketExistsArgs = BucketExistsArgs.builder().bucket(bucketName).build();
        if (minioClient.bucketExists(bucketExistsArgs)) {
            RemoveBucketArgs removeBucketArgs = RemoveBucketArgs.builder().bucket(bucketName).build();
            minioClient.removeBucket(removeBucketArgs);
        }
    }

    /**
     * 查看存储桶
     *
     * @param
     * @return 查看所有存储桶
     */
    @SneakyThrows
    public List<StorageBucket> listBucket() {
        List<StorageBucket> storageBucketList = minioClient.listBuckets().stream().map(e -> {
            StorageBucket storageBucket = new StorageBucket(e.name(),e.creationDate());
            return storageBucket;
        }).collect(Collectors.toList());
        return storageBucketList;
    }


    /**
     * 存储桶所有对象
     *
     * @param name Bucket名称
     * @return 存储桶所有对象
     */
    @SneakyThrows
    public List<BucketObject> getBucketDetail(String name) {
        List<BucketObject> bucketObjectList=new ArrayList<>();
        Iterable<io.minio.Result<Item>> listObjects = minioClient.listObjects(ListObjectsArgs.builder()
                .bucket(name)
                .recursive(true)
                .build());
        for (io.minio.Result<Item> result : listObjects) {
            Item item = result.get();
            bucketObjectList.add(new BucketObject(item.objectName(),item.size(),item.storageClass(),item.lastModified()));
        }
        return bucketObjectList;
    }

    /**
     * 上传文件对象
     *
     * @param file       MultipartFile文件对象
     * @param bucketName 存储桶名称
     * @return 上传文件路径
     */
    @SneakyThrows
    public File putObject(MultipartFile file, String bucketName,String username,String uploadInfo) {
        File fileinfo = new File();
        // 存储桶名称为空则使用默认的存储桶
        if (StrUtil.isBlank(bucketName)) {
            bucketName = defaultBucket;
        }
        // 判断存储桶是否存在
        createBucketIfAbsent(bucketName);

        // 获取文件后缀
        String suffix = FileUtil.getSuffix(file.getOriginalFilename());
        fileinfo.setType(suffix);
        // 文件名
        String fileName = file.getOriginalFilename();
        fileinfo.setName(FileUtil.mainName(file.getOriginalFilename()));
        InputStream inputStream;
        // 是否开启压缩
        if (ImgUtils.isImg(fileName) && imgCompressionEnabled) {
            fileinfo.setName(fileName);
            fileinfo.setType("img");
            if(StringUtils.hasText(username)){
                fileName="headPortrait/"+username+"."+suffix;
            }else {
                if(StringUtils.hasText(uploadInfo)){
                    fileName="applet/text/"+fileName;
                }else {
                    fileName="applet/carouselChart/"+fileName;
                }
            }
            long fileSize = file.getSize();
            log.info("图片({})压缩前大小：{}KB", fileName, fileSize / 1024);
            float compressQuality = ImgUtils.getCompressQuality(fileSize);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(Convert.toInt(fileSize));
            Thumbnails.of(file.getInputStream())
                    .scale(1f) // 图片大小比例
                    .outputQuality(compressQuality) // 图片质量压缩比
                    .toOutputStream(outputStream);
            inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            log.info("图片({})压缩后大小：{}KB", fileName, inputStream.available() / 1024);
            fileinfo.setSize((double) (inputStream.available() / 1024));
        } else {
            fileName="file/"+ DateUtil.format(LocalDateTime.now(), "yyyy/MM/dd")+"/"+fileName;
            inputStream = file.getInputStream();
            fileinfo.setSize((double) file.getSize());
        }

        // 上传参数构建
        PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                .bucket(bucketName)
                .object(fileName)
                .contentType(file.getContentType())
                .stream(inputStream, inputStream.available(), -1)
                .build();
        // 上传
        minioClient.putObject(putObjectArgs);
        String fileUrl;
        if (StrUtil.isBlank(customDomain)) { // 没有自定义文件路径域名
            GetPresignedObjectUrlArgs getPresignedObjectUrlArgs = GetPresignedObjectUrlArgs.builder()
                    .bucket(bucketName).object(fileName)
                    .method(Method.GET)
                    .build();
            fileUrl = minioClient.getPresignedObjectUrl(getPresignedObjectUrlArgs);
            fileUrl=URLDecoder.decode(fileUrl.substring(0, fileUrl.indexOf("?")),"UTF-8");
        } else {
            // 自定义文件路径域名，Nginx配置代理转发
            fileUrl = customDomain + '/' + bucketName + "/" + fileName;
        }
        fileinfo.setUrl(fileUrl);
        return fileinfo;
    }

    @Transactional(rollbackFor = Exception.class)
    public void removeObject(ArrayList<String> fileNames,ArrayList<String> ids) throws Exception {
        // 构建需要删除的对象
        List<DeleteObject> objects = new LinkedList<>();
        for(String fileName:fileNames) {
            objects.add(new DeleteObject(fileName));
        }
        // 删除
        Iterable<io.minio.Result<DeleteError>> results = minioClient.removeObjects(RemoveObjectsArgs.builder()
                .bucket(defaultBucket)
                .objects(objects)
                .build());
        for (Result<DeleteError> result : results) {
            // 删除文件不存在时，不会报错
         result.get();
        }
        fileService.removeByIds(ids);
    }


    /**
     * PUBLIC桶策略
     * 如果不配置，则新建的存储桶默认是PRIVATE，则存储桶文件会拒绝访问 Access Denied
     *
     * @param bucketName
     * @return
     */
    private static StringBuilder publicBucketPolicy(String bucketName) {
        /**
         * AWS的S3存储桶策略
         * Principal: 生效用户对象
         * Resource:  指定存储桶
         * Action: 操作行为
         */
        StringBuilder builder = new StringBuilder();
        builder.append("{\"Version\":\"2012-10-17\","
                + "\"Statement\":[{\"Effect\":\"Allow\","
                + "\"Principal\":{\"AWS\":[\"*\"]},"
                + "\"Action\":[\"s3:ListBucketMultipartUploads\",\"s3:GetBucketLocation\",\"s3:ListBucket\"],"
                + "\"Resource\":[\"arn:aws:s3:::" + bucketName + "\"]},"
                + "{\"Effect\":\"Allow\"," + "\"Principal\":{\"AWS\":[\"*\"]},"
                + "\"Action\":[\"s3:ListMultipartUploadParts\",\"s3:PutObject\",\"s3:AbortMultipartUpload\",\"s3:DeleteObject\",\"s3:GetObject\"],"
                + "\"Resource\":[\"arn:aws:s3:::" + bucketName + "/*\"]}]}");

        return builder;
    }


}

