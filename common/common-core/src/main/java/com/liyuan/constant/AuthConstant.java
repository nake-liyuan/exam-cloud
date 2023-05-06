package com.liyuan.constant;

/**
 * @author liyuan
 * @date 2022/9/9
 * @project exam
 */
public interface AuthConstant {

    /**
     * 黑名单token前缀
     */
    String TOKEN_BLACKLIST_PREFIX = "blacklist_token:";

    /**
     * JWT存储权限前缀
     */
    String AUTHORITY_PREFIX = "ROLE_";

    /**
     * JWT存储权限属性
     */
    String AUTHORITY_CLAIM_NAME = "authorities";

    /**
     * JWT令牌前缀
     */
    String JWT_TOKEN_PREFIX = "Bearer ";

    /**
     * 认证身份标识
     */
    String AUTHENTICATION_IDENTITY_KEY = "authenticationIdentity";

    /**
     * 认证信息Http请求头
     */
    String JWT_TOKEN_HEADER = "Authorization";

    /**
     * 授权模式
     */
    String GRANT_TYPE_KEY = "grant_type";

    /**
     * 客户端ID
     */
    String CLIENT_ID = "client_id";

    /**
     * refresh_token
     */
    String REFRESH_TOKEN = "refresh_token";

    /**
     * 登录用户名
     */
    String USER_NAME_KEY = "user_name";

    /**
     * 后台管理client_id
     */
    String ADMIN_CLIENT_ID = "admin-web";

    /**
     * 前台商城client_id
     */
    String PORTAL_CLIENT_ID = "portal-app";

    /**
     * 后台管理接口路径匹配
     */
    String ADMIN_URL_PATTERN = "/admin/**";

    /**
     * 前台管理接口路径匹配
     */
    String PORTAL_URL_PATTERN = "/portal/**";

    /**
     * 前台管理接口路径匹配
     */
    String ADMINUSERINFO_URL_PATTERN = "/user/info";

    /**
     * Redis缓存权限规则key
     */
    String RESOURCE_ROLES_MAP_KEY = "auth:resourceRolesMap";
    /**
     * 用户信息Http请求头
     */
    String USER_TOKEN_HEADER = "user";

    /**
     * 邮件验证码标题
     */
    String Email_verification_code_title = "考证宝典 | 考证宝典 重置密码信息";

    /**
     * 邮箱验证码前缀
     */
    String Email_verification_code = "email_code:";

    /**
     * 验证码过期时间
     */
    Integer Email_verification_code_Expiration_time = 1200;

    /**
     * 验证码key前缀
     */
    String VALIDATION_CODE_KEY_PREFIX = "captcha_code:";

    /**
     * 短信验证码key前缀
     */
    String SMS_CODE_PREFIX = "sms_code:";

    /**
     * 用户角色菜单key前缀
     */
    String RESOURCE_ROLES_MENUS_MAP_KEY = "role_menu_resources:resourceRolesMenusMap";

    /**
     * 用户学科资源key前缀
     */
    String USER_SUBJECT_CODE_PREFIX = "user_subject_resources:";

    /**
     * 用户当前备考学科
     */
    String USER_CURRENT_SUBJECT_CODE_PREFIX = "user_current_subject:";

    /**
     * 小程序用户访问令牌
     */
    String USER_APPLET_ACCESS_TOKEN = "user_applet_access_token:";

    /**
     * 用户ID
     */
    String User_ID = "2";

    //用户关注的圈子前缀
    String USER_FOLLOW_CIRCLE = "user_follow_circle:";

    //用户关注的用户前缀
    String USER_FOLLOW_USER = "user_follow_user:";

    //用户的粉丝用户前缀
    String USER_FANS_USER = "user_fans_user:";

    //用户踩的帖子前缀
    String USER_TRAMPLE_POSTINGS = "user_trample_postings:";

    //用户点赞的帖子前缀
    String USER_LIKE_POSTINGS = "user_like_postings:";

    //用户观看的帖子前缀
    String USER_VIEW_POSTINGS = "user_view_postings:";

    //用户圈子排行榜前缀
    String USER_CIRCLE_RANKING = "user_circle_ranking:zset";

    //用户发布的帖子前缀
    String USER_RELEASE_POSTINGS = "user_release_postings:";

    //用户搜索值前缀
    String USER_SEARCHES_VALUE = "user_searches_value:";

    //用户帖子评论前缀
    String USER_LIKE_POSTINGS_COMMENT = "user_like_postings_comment:";

    //用户每日答题信息前缀
    String USER_EVERYDAY_ANSWER_ID = "user_everyday_answer_id:";

    //用户学科错题前缀
    String USER_SUBJECT_WRONG_ID = "user_subject_wrong_id:";

    //用户学科收藏前缀
    String USER_SUBJECT_COLLECTION_ID = "user_subject_collection_id:";

    //用户学科笔记前缀
    String USER_SUBJECT_NOTE_ID = "user_subject_note_id:";

    //活动观看计数前缀
    String ACTIVITY_VIEW_COUNT="activity_view_count:";

    //用户创建的圈子前缀
    String USER_ESTABLISH_CIRCLE="user_establish_circle:";

    //用户加入的圈子前缀
    String USER_JOIN_CIRCLE="user_join_circle:";



}
