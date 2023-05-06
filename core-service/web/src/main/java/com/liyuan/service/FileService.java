package com.liyuan.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.liyuan.domain.File;

/**
* @author liyuan
*@date 2023/2/2
*@project exam-cloud
*/
public interface FileService extends IService<File>{

    Page<File> listUserPage(Integer page, Integer size,String name,String type);

}
