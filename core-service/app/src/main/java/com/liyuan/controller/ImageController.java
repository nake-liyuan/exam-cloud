package com.liyuan.controller;

import com.liyuan.api.Result;
import com.liyuan.service.impl.ImageService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author liyuan
 * @date 2023/2/10
 * @project exam-cloud
 */
@RestController
@RequestMapping("image")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    /**
     * @description 图片上传
     * @route POST: /image
     */
    @PostMapping
    @ApiOperation(value = "图片上传")
    @SneakyThrows
    public Result uploadFile(
            @ApiParam("图片") @RequestParam(value = "file") MultipartFile file
    ) {
        String fileurl = imageService.putObject(file);
        return Result.success(fileurl);
    }
}
