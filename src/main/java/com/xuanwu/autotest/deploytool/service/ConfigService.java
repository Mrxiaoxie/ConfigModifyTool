package com.xuanwu.autotest.deploytool.service;

import java.util.Map;

public interface ConfigService {

    boolean updateConfig(Map<String,Object> configMap) throws Exception;

    boolean apaasConfig(Map<String,Object> configMap) throws Exception;

    boolean serviceConfig(Map<String,Object> configMap,String serviceDir) throws Exception;
}
