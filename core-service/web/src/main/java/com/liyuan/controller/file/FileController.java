package com.liyuan.controller.file;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liyuan.api.Result;
import com.liyuan.domain.File;
import com.liyuan.result.PageResult;
import com.liyuan.service.FileService;
import com.liyuan.service.impl.MinioService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

/**
 * @author liyuan
 * @date 2022/10/2
 * @project Oauth2-cloud
 */
@Api(tags = "文件接口")
@RestController
@RequestMapping("files")
@RequiredArgsConstructor
public class FileController {

    private final MinioService minioService;
    private final FileService fileService;

    /**
     * @description 文件列表展示
     * @route GET: /files
     */
    @ApiOperation("文件列表展示（分页）")
    @GetMapping("/page/{page}/{size}")
    public PageResult listFile(
            @ApiParam("页码") @PathVariable Integer page,
            @ApiParam("条数") @PathVariable Integer size,
            @ApiParam("文件名") @RequestParam(required = false,value = "name") String name,
            @ApiParam("文件类型") @RequestParam(required = false,value = "type") String type){
        Page<File> filePage = fileService.listUserPage(page, size, name, type);
        return PageResult.success(filePage);
    }
    /**
     * @description 文件信息记录
     * @route POST: /files
     */
    @ApiOperation("文件信息记录")
    @PostMapping
    public Result record(@RequestBody File file){
        boolean saveState=false;
        File fileInfo = fileService.getOne(new QueryWrapper<File>().eq("name", file.getName()));
        if(fileInfo!=null){
            file.setId(fileInfo.getId());
            saveState = fileService.updateById(file);
        }else {
            saveState = fileService.save(file);
        }
        return Result.judge(saveState);
    }

    /**
     * @description 文件信息修改
     * @route PUT: /files
     */
    @ApiOperation("文件信息修改")
    @PutMapping
    public Result edit(@RequestBody File file){
        boolean updateById = fileService.updateById(file);
        return Result.judge(updateById);
    }

    /**
     * @description 文件上传
     * @route POST: /files/upload
     */
    @PostMapping("/upload")
    @ApiOperation(value = "文件上传")
    @SneakyThrows
    public Result uploadFile(
            @ApiParam("文件") @RequestParam(value = "file") MultipartFile file,
            @ApiParam("存储桶名称(没值默认存储桶)") @RequestParam(value = "bucketName", required = false) String bucketName
    ) {

        String username = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("username");
        String uploadInfo = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("uploadInfo");
        File fileinfo = minioService.putObject(file, bucketName, username,uploadInfo);
        return Result.success(fileinfo);
    }

    /**
     * @description 文件删除
     * @route DELETE: /files
     */
    @DeleteMapping
    @ApiOperation(value = "文件删除")
    @SneakyThrows
    public Result deleteFile(
            @ApiParam("文件路径") @RequestParam(value = "paths") ArrayList<String> paths,
            @ApiParam("文件id") @RequestParam(value = "ids") ArrayList<String> ids
    ) {
        minioService.removeObject(paths,ids);
        return Result.success();
    }

}