package com.xuanwu.autotest.deploytool.tools;

import com.xuanwu.autotest.deploytool.common.CommonVariables;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;

/**
 * @Description properties工作类
 * @Author xiemb
 * @Date 2019/2/19 11:27
 **/
@Component
public class PropertiesTool {

    /**
     * 修改properties文件
     * @param propFile  properties文件对象
     * @param isWrite 是否写入文件中
     * @param paramsMaps 修改项
     * @return 修改后的列表
     * @throws Exception 抛出异常
     */
    public List<String> update(File propFile, boolean isWrite, Map<String,String>... paramsMaps) throws Exception{
        List<String> fileContent = new ArrayList<>();
        InputStreamReader reader = new InputStreamReader(new FileInputStream(propFile), CommonVariables.CHARSET);
        BufferedReader bufferedReader = new BufferedReader(reader);
        String line = bufferedReader.readLine();
        while(line !=null){
            fileContent.add(line);
            line = bufferedReader.readLine();
        }
        reader.close();
        bufferedReader.close();
        List<String> keyUsed = new ArrayList<>();
        for(int j =0; j< fileContent.size(); j++){
            String obj = fileContent.get(j);
            if(obj.trim() == ""){

            }
            else if(obj.trim().startsWith(CommonVariables.DELETE_SYMBOL_PEROPERTIES)){

            }
            else{
                int index = obj.indexOf("=");
                if(index > 0){
                    String key = obj.substring(0,index);
                    for(int i =0;i<paramsMaps.length;i++){
                        Map<String,String> tmp = paramsMaps[i];
                        if(tmp.containsKey(key)){
                            keyUsed.add(key);
                            //要修改
                            String value = tmp.get(key);
                            if(value.startsWith(CommonVariables.DELETE_SYMBOL_PEROPERTIES)){
                                obj = "#" + obj;
                                fileContent.set(j,obj);
                            }
                            else{
                                if(value.startsWith(CommonVariables.ADD_SYMBOL)){
                                    value = value.substring(1);
                                }
                                obj = key + "=" + value;
                                fileContent.set(j,obj);
                            }
                        }
                    }
                }
            }
        }
        Map<String,String> addedMap = new HashMap<>();
        for(int i = 0;i<paramsMaps.length;i++){
            Map<String,String> map = paramsMaps[i];
            for(String key : map.keySet()){
                if(!keyUsed.contains(key)){
                    String value = map.get(key);
                    if(value.startsWith(CommonVariables.ADD_SYMBOL)){
                        addedMap.put(key,key + "=" + value.substring(1));
                    }
                    else{
                        //非新增忽略配置
                        System.out.println("非新增忽略配置");
                        System.out.println("配置项信息:" + key  + "=" + value);
                    }
                }
            }
        }
        fileContent.addAll(addedMap.values());
        if(isWrite){
            OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(propFile), CommonVariables.CHARSET);
            BufferedWriter writer = new BufferedWriter(out);
            for(String key : fileContent){
                writer.write(key);
                writer.newLine();
            }
            writer.flush();
            writer.close();
            out.close();
        }
        return fileContent;
    }

}
