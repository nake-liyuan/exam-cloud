package com.liyuan.domain.query;

import com.liyuan.domain.User;
import lombok.Data;

import java.util.ArrayList;

/**
 * @author liyuan
 * @date 2022/11/4
 * @project exam-cloud
 */
@Data
public class UserQuery extends User {

    private ArrayList<String> roles;
}
