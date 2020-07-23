package io.github.fengyueqiao.marsnode.common.exception;


public enum ErrorCode implements ErrorCodeI {

    E_UserNameNotExist("4001", "用户不存在"),
    E_UserPasswordWrong("4002", "用户密码错误"),

    E_UserNameConflict("4003", "用户名冲突"),
    E_PasswordGenerateError("4005", "用户密码生成错误"),
    E_UserTokenExpired("4006", "token已经超时"),
    E_RequestParameterError("4007", "请求参数错误"),
    E_ElementNameConflict("4008", "元素名冲突"),
    E_RequestNotExist("4009", "请求内容不存在"),
    E_FileIOError("4010", "文件IO错误"),
    E_PermissionDenied("4011", "无权限访问"),
    E_Node_cmdExecError("4012", "命令执行错误"),
    E_InternalError("4098", "内部错误"),
    E_UnknownError("4099", "未知错误");

    private final String errCode;
    private final String errDesc;

    private ErrorCode(String errCode, String errDesc) {
        this.errCode = errCode;
        this.errDesc = errDesc;
    }

    @Override
    public String getErrCode() {
        return errCode;
    }

    @Override
    public String getErrDesc() {
        return errDesc;
    }
}