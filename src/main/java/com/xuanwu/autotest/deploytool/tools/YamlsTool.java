package com.xuanwu.autotest.deploytool.tools;

import com.xuanwu.autotest.deploytool.common.CommonVariables;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.sql.SQLOutput;
import java.util.*;

/**
 * @Description TODO
 * @Author xiemb
 * @Date 2019/2/19 11:27
 **/
@Component
public class YamlsTool {

    /**
     * 更新yaml文件
     * @param yamlFile yaml文件对象
     * @param isWrite 是否写入文件中
     * @param params 修改项 如果修改项的值以+开头为新增，以#开头为删除，没有以上两个特殊字符开头则修改
     * @return 修改后的map
     * @throws Exception 抛出异常
     */
    public Map update(File yamlFile, boolean isWrite, Map<String,String>...params) throws Exception{
        DumperOptions options = new DumperOptions();
        options.setDefaultScalarStyle(DumperOptions.ScalarStyle.PLAIN);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        Yaml yaml = new Yaml(options);
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(yamlFile), CommonVariables.CHARSET));
        Map yamlsMap = yaml.loadAs(reader,Map.class);
        for(int i =0;i<params.length;i++){
            Map<String,String> item = params[i];
            for(String key : item.keySet()){
                Map tmpMap = yamlsMap;
                String[] keys = key.split("\\.");
                if(keys.length == 1){
                    if(tmpMap.containsKey(keys[0])){
                        String value = item.get(key);
                        if(value.startsWith(CommonVariables.DELETE_SYMBOL_YAML)){
                            //删除
                            tmpMap.remove(keys[0]);
                        }
                        else{
                            //修改
                            tmpMap.put(keys[0],item.get(key));
                        }
                    }
                    else{
                        //不包含，判断是否新增
                        String value = item.get(key);
                        if(value.startsWith(CommonVariables.ADD_SYMBOL)){
                            //新增
                            tmpMap.put(keys[0],value.substring(1));
                        }
                        else{
                            //既不包含key，也不是新增。
                            System.out.println("既不包含key，也不是新增。");
                            System.out.println("tmpMap:" + tmpMap + " key:" + keys[0]);
                        }
                    }
                }
                else{
                    int j = 0;
                    for(; j< keys.length;j++){
                        String k = keys[j];
                        String index = null;
                        if(k.contains(":")){
                            String[] keyIndex = k.split("\\:");
                            k = keyIndex[0];
                            index = keyIndex[1];
                        }
                        if(tmpMap.containsKey(k)){
                            if(j == keys.length - 1){
                                String value = item.get(key);
                                if(value.startsWith(CommonVariables.DELETE_SYMBOL_YAML)){
                                    //判断是否删除
                                    tmpMap.remove(k);
                                }
                                else{
                                    //更新
                                    if(value.startsWith(CommonVariables.ADD_SYMBOL)){
                                        value = value.substring(1);
                                    }
                                    tmpMap.put(k,value);
                                }
                            }
                            else{
                                Object obj = tmpMap.get(k);
                                if(obj instanceof LinkedHashMap){
                                    tmpMap = (Map)obj;
                                }
                                else if(obj instanceof ArrayList){
                                    List list = (List)obj;
                                    int indexTarget = Integer.parseInt(index);
                                    Map indexmap = null;
                                    if(indexTarget >= list.size()){
                                        //判断是否需要新增
                                        String value = item.get(key);
                                        if(value.startsWith("+")){
                                            indexmap = new HashMap();
                                            list.add(indexmap);
                                        }
                                        else{
                                            break;
                                        }
                                    }
                                    else{
                                        indexmap = (Map)list.get(indexTarget);
                                    }
                                    //考虑一下删除数组的问题
                                    tmpMap = indexmap;
                                }
                                else{
                                    //未知类型
                                    System.out.println("未知类型:" + obj.getClass().toString());
                                }
                            }

                        }
                        else{
                            String value = item.get(key);
                            if(value.startsWith(CommonVariables.ADD_SYMBOL)){
                                //新增
                                if(j == keys.length - 1){
                                    tmpMap.put(k,value.substring(1));
                                }
                                else{
                                    //需要考虑下一个是MAP还是数组
                                    if(keys[j+1].contains(":")){
                                        //数组
                                        ArrayList tmpList = new ArrayList<>();
                                        Map map = new HashMap();
                                        tmpList.add(map);
                                        tmpMap.put(k, tmpList);
                                        tmpMap = map;
                                    }
                                    else{
                                        //MAP
                                        Map map = new HashMap<>();
                                        tmpMap.put(k,map);
                                        tmpMap = map;
                                    }
                                }
                            }
                            else if(value.startsWith(CommonVariables.DELETE_SYMBOL_YAML)){
                                //这个是删除的 忽略
                                System.out.println("配置项为删除项[" + key +"]，忽略");
                            }
                            else{
                                //配置项错误
                                System.out.println("配置项错误");
                                System.out.println("tmp:" + tmpMap.toString() + " key:" + key + " value:" + value);
                            }

                        }
                    }
                }
            }
        }
        if(isWrite){
            //写文件
            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(yamlFile),CommonVariables.CHARSET);
            yaml.dump(yamlsMap, writer);
            writer.close();
        }
        return yamlsMap;
    }

    private void appendYaml(Map map, String[] keys, Object value){
        Object tempValue = value;
        int i = keys.length - 1;
        String k = null;
        for(;i >=0;i--){
            Map tmp = new HashMap();
            k = keys[i];
            String index = null;
            if(k.contains(":")){
                String[] keyIndex = k.split(":");
                k = keyIndex[0];
                index = keyIndex[1];
            }
            if(index == null){
                tmp.put(k,tempValue);
                tempValue = tmp;
            }
        }
    }
}
