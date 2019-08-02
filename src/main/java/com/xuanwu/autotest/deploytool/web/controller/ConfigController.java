package com.xuanwu.autotest.deploytool.web.controller;

import com.xuanwu.autotest.deploytool.common.ResultHelper;
import com.xuanwu.autotest.deploytool.service.ConfigService;
import org.apache.commons.io.IOUtils;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description TODO
 * @Author xiemb
 * @Date 2019/2/19 11:29
 **/

@RestController
@RequestMapping("/config")
public class ConfigController {

    @Autowired
    private ConfigService configService;

    @Value("${apaas.path:/home/apaas}")
    private String apaasPath;

    @RequestMapping("/update")
    @ResponseBody
    public String updateConfig(@RequestBody Map<String,Object> requestBody){
        Map<String, Object> resultMap = new HashMap<>();
        try {
            configService.updateConfig(requestBody);
        }
        catch (Exception e){
            e.printStackTrace();
            return ResultHelper.failure(e.getMessage());
        }
        return ResultHelper.success(resultMap);
    }

    @RequestMapping("/updateConfigure")
    @ResponseBody
    public String uppdateConfig(){

        return null;
    }

    @PostMapping("/update/apaas")
    @ResponseBody
    public String updateAPAASConfig(@RequestBody Map<String,Object> requestBody){
        String response;
        File apaasFile = new File(apaasPath);
        if(! apaasFile.exists() || ! apaasFile.isDirectory()){
            response = ResultHelper.failure("appas服务的路径不存在或" + apaasPath + "不是文件夹");
        }
        else{
            try{
                configService.apaasConfig(requestBody);
                response = ResultHelper.success();
            }
            catch (Exception e){
                e.printStackTrace();
                response = ResultHelper.failure(e.getMessage());
            }
        }
        return response;
    }
}
