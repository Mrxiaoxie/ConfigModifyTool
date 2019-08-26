package com.xuanwu.autotest.deploytool.web.controller;

import com.xuanwu.autotest.deploytool.common.ResultHelper;
import com.xuanwu.autotest.deploytool.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
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

    @PostMapping("/update/apaas/{path}")
    @ResponseBody
    public String updateSingleConfig(@PathVariable("path")String path,@RequestBody Map<String,Object> requestBody){
        String response;
        try{
            File serviceDir = new File(apaasPath,path);
            if(!serviceDir.exists()|| !serviceDir.isDirectory()){
                response = ResultHelper.failure("路径" + serviceDir.getAbsolutePath() +"不存在或其不是文件夹。");
            }
            else{
                if(configService.serviceConfig(requestBody,path)){
                    response = ResultHelper.success();
                }
                else{
                    response = ResultHelper.failure("未知错误，请联系管理员。");
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
            response = ResultHelper.failure(e.getMessage());
        }
        return response;
    }
}
