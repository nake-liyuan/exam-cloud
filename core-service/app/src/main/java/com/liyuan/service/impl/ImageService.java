package com.liyuan.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.liyuan.util.ImgUtils;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.http.Method;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.Objects;

/**
 * @author liyuan
 * @date 2023/2/10
 * @project exam-cloud
 */
@Component
@ConfigurationProperties(prefix = "minio")
@Slf4j
public class ImageService implements InitializingBean {

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

    private MinioClient minioClient;

    @Override
    public void afterPropertiesSet() {
        log.info("初始化 MinIO 客户端..."+endpoint);
        Assert.notBlank(endpoint, "MinIO endpoint不能为空");
        Assert.notBlank(accessKey, "MinIO accessKey不能为空");
        Assert.notBlank(secretKey, "MinIO secretKey不能为空");
        this.minioClient = MinioClient.builder()
                .endpoint(endpoint).credentials(accessKey, secretKey).build();
    }

    @SneakyThrows
    public String putObject(MultipartFile file){
        String uploadInfo = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest().getHeader("uploadInfo");
        // 文件名
        String fileName = file.getOriginalFilename();
        if("activity".equals(uploadInfo)){
            fileName="applet/activity/"+fileName;
        }
        if("group".equals(uploadInfo)){
            fileName="applet/group/"+fileName;
        }
        if("postings".equals(uploadInfo)){
            fileName="applet/postings/"+fileName;
        }
        InputStream inputStream;
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

        // 上传参数构建
        PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                .bucket(defaultBucket)
                .object(fileName)
                .contentType(file.getContentType())
                .stream(inputStream, inputStream.available(), -1)
                .build();
        // 上传
        minioClient.putObject(putObjectArgs);
        String fileUrl;
        if (StrUtil.isBlank(customDomain)) { // 没有自定义文件路径域名
            GetPresignedObjectUrlArgs getPresignedObjectUrlArgs = GetPresignedObjectUrlArgs.builder()
                    .bucket(defaultBucket).object(fileName)
                    .method(Method.GET)
                    .build();
            fileUrl = minioClient.getPresignedObjectUrl(getPresignedObjectUrlArgs);
            fileUrl= URLDecoder.decode(fileUrl.substring(0, fileUrl.indexOf("?")),"UTF-8");
        } else {
            // 自定义文件路径域名，Nginx配置代理转发
            fileUrl = customDomain + '/' + defaultBucket + "/" + fileName;
        }
        return fileUrl;
    }

}
