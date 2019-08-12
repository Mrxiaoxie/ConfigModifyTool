# ConfigModifyTool
批量修改配置文件工具,目前仅支持properties文件和yaml文件的批量修改。

### 使用步骤：
1. 修改配置文件中apaas.path路径,一般指向/home/apaas/, 这样就可以修改/home/apaas/*/application.properties文件
2. 在服务器上启动服务 java -jar deploytool-0.0.1-SNAPSHOT.jar
3. 根据部署需要，修改properties.json
4. curl -XPOST -H 'Content-Type:application/json' 'http://localhost:8181/config/update/apaas' -d @properties.json
5. 如果返回OK，则表示修改成功

properites.json说明：
1. CommonInfo是表示公共参数
2. apaas下的json是代表目录下的修改参数，当参数既存在公共参数中也存在目录参数中，以目录修改参数为准。
3. 针对properties文件，新增配置项可以配置成 "key":"+value",删除配置项可以配置成"key":"#value"，如果value的值是以+或#开头，则需要配置成++value或+#value。
4. 针对yaml文件，新增配置项可以配置成"key":"+value", 删除配置项可以配置成"key":"-value",如果value的值是以+或-开头，则需要配置成++value或+-value。<br />
   由于yaml文件存在数组的概率，当修改数组内对象的值时，可以用冒号加下标来表示。例子如下：
   ```$xslt
   #如果需要修改b2的值，则配置项写成"aa:1.b2":"newValue"。
   #同理新增，"aa:2.b3":"value3"。
   #同理删除,"aa:1:b2":"-"。
   aa:
     -
      b1:'value1'
     -
      b2:'value2'
   ```
### 样例properties.json
```$xslt
{
    "CommonInfo":{
        "props":{
            "accountserv.url":"http://172.16.0.161:9021",
            "etl.callback.url":"http://172.16.0.163:9040/quartz/jobCallback",
            "task.callback.url":"http://172.16.0.163:9040/quartz/jobCallback",
            "biz.asyncrun.url":"http://172.16.0.161:8030/biz/asyncrun?mocode={0}&lgcode={1}",
            "username":"postgres",
            "password":"csb123456",
            "url":"jdbc:postgresql://172.16.0.126:15432/xw_platform",
            "serv.accountserv":"http://172.16.0.161:9021",
            "serv.messageserv":"http://172.16.0.163:7001/api/teapi/message",
            "serv.operationserv":"http://172.16.0.161:8033",
            "spring.datasource.masterPassword[0]":"csb123456",
            "spring.datasource.masterUsername[0]":"postgres",
            "spring.datasource.masterUrl[0]":"jdbc:postgresql://172.16.0.126:15432/xw_platform?allowMultiQueries=true&stringtype=unspecified",
            "spring.datasource.slaveUrl[0]":"jdbc:postgresql://172.16.0.126:15432/xw_platform?allowMultiQueries=true&stringtype=unspecified",
            "spring.datasource.slaveUsername[0]":"postgres",
            "spring.datasource.slavePassword[0]":"csb123456",
            "spring.redis.host":"172.16.0.163",
            "spring.redis.port":"6379",
            "spring.redis.password":"",
            "spring.meta.masterPassword[0]":"csb123456",
            "spring.meta.masterUsername[0]":"postgres",
            "spring.meta.masterUrl[0]":"jdbc:postgresql://172.16.0.126:15432/xw_metadata?allowMultiQueries=true&stringtype=unspecified",
            "spring.meta.slaveUrl[0]":"jdbc:postgresql://172.16.0.126:15432/xw_metadata?allowMultiQueries=true&stringtype=unspecified",
            "spring.meta.slaveUsername[0]":"postgres",
            "spring.meta.slavePassword[0]":"csb123456",
            "spring.datasource.url":"jdbc:postgresql://172.16.0.126:15432/xw_platform",
            "spring.datasource.username":"postgres",
            "spring.datasource.password":"csb123456",
            "spring.tenant.validationQuery":"SELECT 1",
            "spring.datasource.validationQuery":"SELECT 1",
            "spring.metadata.validationQuery":"SELECT 1",
            "spring.meta.mode":"2"
        },
        "yamls":{
            "spring.datasource.url":"jdbc:postgresql://172.16.0.126:15432/xw_platform",
            "spring.datasource.username":"postgres",
            "spring.datasource.password":"csb123456"
        }
    },
    "aPaaS":{
        "account-rest":{
            "props":{
                "server.port":"9021",
                "messageserv.url":"http://172.16.0.161:8092/message",
                "web.url":"http://172.16.0.163:7000"
            },
            "yamls":{

            }
        },
        "authserver":{
            "props":{
                "server.port":"8020",
                "aliyun.sts.region":"cn-hangzhou",
                "aliyun.sts.apiVersion":"2015-04-01",
                "aliyun.sts.roleArn":"",
                "aliyun.sts.accessKeyId":"",
                "aliyun.sts.accessKeySecret":"",
                "aliyun.sts.emulatorTenants":"1008410"
            },
            "yamls":{

            }
        },
        "biengine":{
            "props":{
                "server.port":"8050",
                "report.export.queueserver.url":"http://172.16.0.163:8010",
                "tenant.conf.svc":"http://172.16.0.161:8033/resource/tenantservresources"
            },
            "yamls":{

            }
        },
        "dynamic-bizengine":{
            "props":{
                "serv.authserv.url":"http://172.16.0.163:7000/api/",
                "server.port":"8030"
            },
            "yamls":{

            }
        },
        "etl-bizengine":{
            "props":{
                "server.port":"9030"
            },
            "yamls":{

            }
        },
        "ide":{
            "props":{
                "server.port":"8090",
                "taskserv.url":"http://172.16.0.163:9040",
                "fileserver.init.url":"http://172.16.0.163:7000/api/teapi/storage/init",
                "fileserver.upload.url":"http://172.16.0.163:7000/api/teapi/storage/upload",
                "fileserver.download.url":"http://172.16.0.163:7000/file"
            },
            "yamls":{

            }
        },
        "impexp":{
            "props":{
                "server.port":"8093",
                "task.callback.url":"http://172.16.0.163:9040/quartz/jobCallback",
                "impexp.queueservice.address":"http://172.16.0.163:8010",
                "impexp.templateservice.address":"http://172.16.0.163:7000/api/teapi/protocol/file/download?filekey=",
                "bizengine.FLY.geo.code.keys":"3e9c0d0cfae8303eae51446c6fde4d92"
            },
            "yamls":{

            }
        },
        "log-rest":{
            "props":{
                "server.port":"9051"
            },
            "yamls":{

            }
        },
        "message-rest":{
            "props":{
                "server.port":"8092",
                "msg.sms.mtip":"211.147.239.62",
                "msg.sms.mtport":"8450",
                "msg.sms.moip":"211.147.239.62",
                "msg.sms.moport":"8460",
                "msg.sms.username":"test",
                "msg.sms.password":"123456",
                "msg.product.appKey":"955eba4a3799c39a91902338",
                "msg.product.masterSecret":"051c77e4b8958bac2ffbf5a3"
            },
            "yamls":{

            }
        },
        "offline-engine":{
            "props":{
                "server.port":"8060"
            },
            "yamls":{

            }
        },
        "queue-service":{
            "props":{
                "server.port":"8010"
            },
            "yamls":{

            }
        },
        "resourceconfig-service":{
            "props":{
                "server.port":"8033"
            },
            "yamls":{

            }
        },
        "role-permission-rest":{
            "props":{
                "server.port":"8084"
            },
            "yamls":{

            }
        },
        "task-service":{
            "props":{
                "server.port":"9040",
                "dynanic.run.url":"http://172.16.0.163:9030/biz/runETL",
                "dynanic.cancel.url":"http://172.16.0.163:9030/biz/cancelETL",
                "serv.microservice.url":"http://172.16.0.163:7000/api/teapi/ms-biz-bycode/{0}/{1}",
                "serv.authserv.url":"http://172.16.0.163:7000/api/",
                "spring.datasource.url":"jdbc:postgresql://172.16.0.126:15432/xw_quartz"
            },
            "yamls":{

            }
        },
        "uiprotocol-rest":{
            "props":{
                "server.port":"8083"
            },
            "yamls":{

            }
        },
        "mini-program-rest":{
            "props":{

            },
            "yamls":{
                "serv.authserv":"http://172.16.0.163:7000/api/",
                "server.port":"8021"
            }
        },
        "flowengine-rest":{
            "props":{
                "server.port":"8085",
                "biz.asyncrun.url":"http://172.16.0.161:8030/biz/run?mocode={0}&lgcode={1}"
            },
            "yamls":{

            }
        },
        "platform-rest":{
            "props":{
                "server.port":"8091",
                "server.contextPath":"",
                "metadata.connstr":"jdbc:postgresql://172.16.0.126:15432/xw_metadata?user=postgres&password=csb123456&allowMultiQueries=true&stringtype=unspecified",
                "jobsserv.url":"http://172.16.0.163:9040",
                "opsserv.url":"http://172.16.0.161:8033",
                "plat.env.url":"jdbc:postgresql://172.16.0.126:15432/xw_plat_env?allowMultiQueries=true&stringtype=unspecified",
                "plat.env.username":"postgres",
                "plat.env.password":"csb123456"
            },
            "yamls":{

            }
        },
        "workreport-rest":{
            "props":{
                "server.port":"8286",
                "remote.sendmessage.callIP":"http://172.16.0.161:8092",
                "remote.sendmessage.callAPI":"/message"
            },
            "yamls":{

            }
        },
        "announcement-rest":{
            "props":{
                "server.port":"8282",
                "remote.sendmessage.callIP":"http://172.16.0.163:7001",
                "remote.sendmessage.callAPI":"/api/teapi/message",
                "storage.rootpath":"/home/apaas/announcement/myfile"
            },
            "yamls":{

            }
        }
    }
}
```


