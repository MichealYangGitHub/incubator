package com.michealyang.model.base.dto;

/**
 * Created by michealyang on 17/3/7.
 */
public class ResultDto<T> {

    public ResultDto(boolean success, String msg){
        this.success = success;
        this.msg = msg;
        this.data = null;
    }

    public ResultDto(boolean success, String msg, T data){
        this.success = success;
        this.msg = msg;
        this.data = data;
    }

    private boolean success;

    private String msg;

    private T data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResultDto{" +
                "success=" + success +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
