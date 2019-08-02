package com.xuanwu.autotest.deploytool.service.impl;

import com.xuanwu.autotest.deploytool.service.ConfigService;
import com.xuanwu.autotest.deploytool.tools.PropertiesTool;
import com.xuanwu.autotest.deploytool.tools.YamlsTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @Description TODO
 * @Author xiemb
 * @Date 2019/2/19 11:27
 **/
@Service
public class ConfigServiceImpl implements ConfigService {

    public final static String COMMONPART = "CommonInfo";
    public final static String PLATFORMPART = "aPaaS";

    @Autowired
    private PropertiesTool propertiesTool;

    @Autowired
    private YamlsTool yamlsTool;
    @Value("${default.path}")
    private String path;
    @Value("${apaas.path:/home/apaas}")
    private String apaasPath;

    @Override
    public boolean updateConfig(Map<String,Object> map) throws Exception{
        Map<String,Object> commonMap = (Map)map.get(COMMONPART);
        Map<String,Object> platformMap = (Map)map.get(PLATFORMPART);
        Map<String,String> proptiesMap = (Map)commonMap.get("props");
        Map<String,String> yamlsMap = (Map)commonMap.get("yamls");
        //处理平台服务
        Map<String,Object> apaasServices = (Map)platformMap.get("apaasService");
        String apaasPath = path + File.separator  + "apaasService";
        String productPath = path + File.separator  + "productService";
        for(String key : apaasServices.keySet()){
            updateService((Map<String, Object>) apaasServices.get(key), apaasPath, proptiesMap, yamlsMap);
        }
        //处理产品服务
        Map<String,Object> productServices = (Map)platformMap.get("productService");
        for(String key : productServices.keySet()){
            updateService((Map<String, Object>) productServices.get(key), productPath, proptiesMap, yamlsMap);
        }
        return true;
    }

    private void updateService(Map<String,Object> jobConfig, String servicePath, Map<String,String> commonProps, Map<String,String> commonYamls) throws Exception{
        List<Map<String,String>> artifacts = (List<Map<String, String>>)jobConfig.get("artifacts");
        List<Map<String,String>> propsList = (List<Map<String, String>>)jobConfig.get("props");
        List<Map<String,String>> yamlsList = (List<Map<String, String>>)jobConfig.get("yamls");
        for(int i = 0; i< artifacts.size();i++){
            Map<String,String> item = artifacts.get(i);
            //获取服务的路径，检查其下是否有application.properties文件或者application.yaml文件
            //application.properties
            String dir = item.get("targetDir");
            File propFile = new File(servicePath + File.separator + dir + File.separator + "application.properties");
            if(propFile.exists() && propFile.isFile() && propsList != null && propsList.size() > 0){
                propertiesTool.update(propFile,true, commonProps, propsList.get(i));
            }
            //application.yaml
            File yamlFile = new File(servicePath + File.separator + dir + File.separator + "application.yaml");
            if(yamlFile.exists() && yamlFile.isFile() && yamlsList != null && yamlsList.size() > 0){
                yamlsTool.update(yamlFile,true,commonYamls,yamlsList.get(i));
            }
        }
    }

    @Override
    public boolean apaasConfig(Map<String,Object> configMap) throws Exception{
        Map<String,Object> commonMap = (Map)configMap.get(COMMONPART);
        Map<String,String> proptiesMap = (Map)commonMap.get("props");
        Map<String,String> yamlsMap = (Map)commonMap.get("yamls");
        Map<String,Object> apaasMap = (Map)configMap.get(PLATFORMPART);
        File file =  new File(apaasPath);
        if(!file.exists() && !file.isDirectory()){
            throw new Exception("apaas路径错误，请重新配置");
        }
        for(File tmp : file.listFiles()){
            if(tmp.isDirectory()){
                File propFile = new File(tmp,"application.properties");
                if(propFile.exists() && propFile.isFile()){
                    if(apaasMap.containsKey(tmp.getName())){
                        Map<String,String> specialMap = (Map<String, String>)((Map<String, Object>) apaasMap.get(tmp.getName())).get("props");
                        propertiesTool.update(propFile,true,proptiesMap,specialMap);
                    }
                    else{
                        propertiesTool.update(propFile,true,proptiesMap);
                    }
                }
                File yamlFile = new File(tmp,"application.yaml");
                if(yamlFile.exists() && yamlFile.isFile()){
                    if(apaasMap.containsKey(tmp.getName())){
                        Map<String,String> specialYaml = (Map<String,String>)((Map<String,Object>)apaasMap.get(tmp.getName())).get("yamls");
                        yamlsTool.update(yamlFile,true,yamlsMap,specialYaml);
                    }
                    else{
                        yamlsTool.update(yamlFile,true,yamlsMap);
                    }
                }
            }
        }
        return true;
    }
}
