package com.xuanwu.autotest.deploytool.common;

import com.alibaba.fastjson.JSON;

public class ResultHelper {

    public static String success(){
        return result(200, null);
    }

    public static String success(Object data){
        return result(200, data);
    }

    public static String failure(){
        return result(500, null);
    }

    public static String failure(Object data){
        return  result(500, data);
    }

    public static String failure(int status){
        return  result(status, null);
    }

    public static String failure(int status, Object data){
        return result(status,data);
    }

    public static String result(int status, Object data){
        return JSON.toJSONString(new ResponseObj(status,data));
    }
}
