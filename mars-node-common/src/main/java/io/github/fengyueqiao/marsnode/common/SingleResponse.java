package io.github.fengyueqiao.marsnode.common;

import io.github.fengyueqiao.marsnode.common.exception.ErrorCodeI;

/**
 * Response with single record to return
 * <p/>
 * Created by Danny.Lee on 2017/11/1.
 */

public class SingleResponse<T> extends Response {

    private T data;

    public static <T> SingleResponse<T> of(T data) {
        SingleResponse<T> singleResponse = new SingleResponse<>();
        singleResponse.setSuccess(true);
        singleResponse.setData(data);
        return singleResponse;
    }
    
    public T getData() {
        return data;
    }
    
    public void setData(T data) {
        this.data = data;
    }

    public static SingleResponse buildFailure(String errCode, String errMessage) {
        SingleResponse response = new SingleResponse();
        response.setSuccess(false);
        response.setErrCode(errCode);
        response.setErrMessage(errMessage);
        return response;
    }

    public static SingleResponse buildFailure(ErrorCodeI errCode) {
        SingleResponse response = new SingleResponse();
        response.setSuccess(false);
        response.setErrCode(errCode.getErrCode());
        response.setErrMessage(errCode.getErrDesc());
        return response;
    }

    public static SingleResponse buildSuccess(){
        SingleResponse response = new SingleResponse();
        response.setSuccess(true);
        return response;
    }
    
}
