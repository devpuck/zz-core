package com.zz.core.api;

/**
 * <p>
 * REST API 响应码
 * </p>
 */
public enum ApiResultCode
{

    SUCCESS(0, "操作成功"),

    UNAUTHORIZED(401, "非法访问"),

    NOT_PERMISSION(403, "没有权限"),

    NOT_FOUND(404, "你请求的资源不存在"),

    FAIL(500, "操作失败"),

    LOGIN_EXCEPTION(4000, "登陆失败"),

    SYSTEM_EXCEPTION(200, "系统异常!"),

    PARAMETER_EXCEPTION(1, "请求参数校验异常"),

    PARAMETER_PARSE_EXCEPTION(5002, "请求参数解析异常"),

    HTTP_MEDIA_TYPE_EXCEPTION(5003, "HTTP Media 类型异常"),

    SPRING_BOOT_PLUS_EXCEPTION(5100, "系统处理异常"),

    BUSINESS_EXCEPTION(5101, "业务处理异常"),

    DAO_EXCEPTION(5102, "数据库处理异常"),

    VERIFICATION_CODE_EXCEPTION(5103, "验证码校验异常"),

    AUTHENTICATION_EXCEPTION(5104, "登陆授权异常"),

    UNAUTHENTICATED_EXCEPTION(5105, "没有访问权限"),

    UNAUTHORIZED_EXCEPTION(5106, "没有访问权限"),

    JWT_INVALID_TOKEN_CODE(461, "token已无效，请使用已刷新的token"),

    USER_ALREADY_EXIST_EXCEPTION(6000, "用户已经存在"),
    USER_NOT_EXIST_EXCEPTION(6001, "用户不存在"),
    USER_TYPE_EXCEPTION(6002, "用户登陆类型错误"),

    SUCCESS_AGENCY(0, "操作成功"),
    UNAUTHORIZED_AGENCY(1, "参数错误"),
    NOT_PERMISSION_AGENCY(2, "无效请求"),
    NOT_FOUND_AGENCY(100, "重复请求"),
    FAIL_AGENCY(200, "未知错误"),
    ;;

    private final int code;
    private final String msg;

    ApiResultCode(final int code, final String msg)
    {
        this.code = code;
        this.msg = msg;
    }

    public static ApiResultCode getApiCode(int code)
    {
        ApiResultCode[] ecs = ApiResultCode.values();
        for (ApiResultCode ec : ecs)
        {
            if (ec.getCode() == code)
            {
                return ec;
            }
        }
        return SUCCESS;
    }

    public int getCode()
    {
        return code;
    }

    public String getMsg()
    {
        return msg;
    }

}
