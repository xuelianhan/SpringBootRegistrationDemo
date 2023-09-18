package com.github.register.domain.user;

/**
 * @author sniper
 * @date 15 Sep 2023
 */
public enum DeletedStatusEnum {

    EXIST(0, "exist"),
    DELETED(1, "deleted");

    DeletedStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private int code;

    private String desc;

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

}
