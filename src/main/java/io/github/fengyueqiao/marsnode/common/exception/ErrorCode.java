package io.github.fengyueqiao.marsnode.common.exception;

import io.github.fengyueqiao.marsnode.common.dto.ErrorCodeI;


public enum ErrorCode implements ErrorCodeI {

    E_Node_requestParamError ("E_Node_requestParamError", "请求参数错误"),
    E_Node_fileIOError ("E_Node_fileIOError", "文件读写错误"),
    E_Node_cmdExecError ("E_Node_cmdExecError", "命令执行失败");

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