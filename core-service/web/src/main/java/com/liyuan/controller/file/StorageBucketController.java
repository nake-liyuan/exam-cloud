package com.liyuan.controller.file;


import com.liyuan.api.Result;
import com.liyuan.entity.BucketObject;
import com.liyuan.entity.StorageBucket;
import com.liyuan.service.impl.MinioService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author liyuan
 * @date 2022/10/4
 * @project Oauth2-cloud
 */
@Api(tags = "存储桶接口")
@RestController
@RequestMapping("/bucket")
@RequiredArgsConstructor
public class StorageBucketController {

    private final MinioService minioService;

    /**
     * @description 创建存储桶(存储桶不存在)
     * @route GET: /bucket
     */
    @PostMapping
    @ApiOperation(value = "创建存储桶(存储桶不存在)")
    @SneakyThrows
    public Result<String> saveBucket(
            @ApiParam("存储桶名称") @RequestParam(value = "bucketName") String bucketName
    ) {
        minioService.createBucketIfAbsent(bucketName);
        return Result.success();
    }

    /**
     * @description 删除存储桶
     * @route DELETE: /bucket
     */
    @DeleteMapping
    @ApiOperation(value = "删除存储桶")
    @SneakyThrows
    public Result deleteFile(
            @ApiParam("存储桶名称") @RequestParam(value = "bucketName") String bucketName
    ) {
        minioService.deleteBucketIfExists(bucketName);
        return Result.success();
    }

    /**
     * @description 查看所有存储桶
     * @route GET: /bucket
     */
    @GetMapping
    @ApiOperation(value = "查看所有存储桶")
    @SneakyThrows
    public Result listBucket() {
        List<StorageBucket> storageBucketList = minioService.listBucket();
        return Result.success(storageBucketList);
    }

    /**
     * @description 查看存储桶所有对象
     * @route GET: /bucket
     */
    @GetMapping("/{name}")
    @ApiOperation(value = "查看存储桶所有对象")
    @SneakyThrows
    public Result getBucketDetail(
            @ApiParam(value = "存储桶名称") @PathVariable String name
    ) {
        List<BucketObject> bucketDetail = minioService.getBucketDetail(name);
        return Result.success(bucketDetail);
    }



}
