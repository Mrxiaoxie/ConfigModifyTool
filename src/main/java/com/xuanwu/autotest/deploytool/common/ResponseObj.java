package com.xuanwu.autotest.deploytool.common;

public class ResponseObj {

    private Integer status;
    private Object data;

    public ResponseObj(){

    }

    public ResponseObj(Integer status, Object data){
        this.status = status;
        this.data = data;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
