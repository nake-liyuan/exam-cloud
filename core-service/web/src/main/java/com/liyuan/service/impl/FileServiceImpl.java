package com.liyuan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liyuan.domain.File;
import com.liyuan.mapper.mp.FileMapper;
import com.liyuan.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
* @author liyuan
*@date 2023/2/2
*@project exam-cloud
*/
@Service
@RequiredArgsConstructor
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements FileService{

    private final FileMapper mapper;

    @Override
    public Page<File> listUserPage(Integer page, Integer size,String name, String type) {
        Page<File> filePage = mapper.selectPage(new Page<File>(page, size),
                new QueryWrapper<File>()
                        .eq(StringUtils.hasText(name), "name", name)
                        .eq(StringUtils.hasText(type), "type", type)
        );
        return filePage;
    }
}
