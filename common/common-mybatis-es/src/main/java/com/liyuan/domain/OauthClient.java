package com.liyuan.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
* @author liyuan
*@date 2022/10/15
*@project exam-cloud
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "oauth_client")
public class OauthClient implements Serializable {
    @TableId(value = "client_id",type = IdType.INPUT)
    private String clientId;

    @TableField(value = "resource_ids")
    private String resourceIds;

    @TableField(value = "client_secret")
    private String clientSecret;

    @TableField(value = "scope")
    private String scope;

    @TableField(value = "authorized_grant_types")
    private String authorizedGrantTypes;

    @TableField(value = "web_server_redirect_uri")
    private String webServerRedirectUri;

    @TableField(value = "authorities")
    private String authorities;

    @TableField(value = "access_token_validity")
    private Integer accessTokenValidity;

    @TableField(value = "refresh_token_validity")
    private Integer refreshTokenValidity;

    @TableField(value = "additional_information")
    private String additionalInformation;

    @TableField(value = "autoapprove")
    private String autoapprove;

}