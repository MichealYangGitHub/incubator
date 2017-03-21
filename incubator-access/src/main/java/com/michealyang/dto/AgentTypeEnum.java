package com.michealyang.dto;

/**
 * Created by michealyang on 17/3/17.
 */
public enum AgentTypeEnum {
    WEB(10, "Web版"),
    MOBILE(20, "移动版"),
    INVALID(90, "非法类型");

    AgentTypeEnum(int value, String msg) {
        this.value = value;
        this.msg = msg;
    }

    private int value;
    private String msg;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
