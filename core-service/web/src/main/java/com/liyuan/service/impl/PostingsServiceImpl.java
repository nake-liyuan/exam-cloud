package com.liyuan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liyuan.domain.Postings;
import com.liyuan.mapper.mp.PostingsMapper;
import com.liyuan.service.PostingsService;
import org.springframework.stereotype.Service;
/**
* @author liyuan
*@date 2023/2/11
*@project exam-cloud
*/
@Service
public class PostingsServiceImpl extends ServiceImpl<PostingsMapper, Postings> implements PostingsService{

}
