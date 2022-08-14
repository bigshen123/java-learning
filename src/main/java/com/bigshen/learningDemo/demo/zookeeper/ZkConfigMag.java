package com.bigshen.learningDemo.demo.zookeeper;

import org.I0Itec.zkclient.ZkClient;

/**
 * @ClassName ZkConfigMag
 * @Description: TODO
 * @Author BYJ
 * @Date 2020/10/25
 * @Version V1.0
 **/
public class ZkConfigMag {

    private Config config;
    /**
     * 从数据库加载配置
     */
    public Config downLoadConfigFromDB(){
        //getDB
        config = new Config("nm", "pw");
        return config;
    }

    /**
     * 配置文件上传到数据库
     */
    public void upLoadConfigToDB(String nm, String pw){
        if(config==null)config = new Config();
        config.setUserNm(nm);
        config.setUserPw(pw);
        //updateDB
    }

    /**
     * 配置文件同步到zookeeper
     */
    public void syncConfigToZk(){
        ZkClient zk = new ZkClient("192.168.3.86:2181");
        if(!zk.exists("/zkConfig")){
            zk.createPersistent("/zkConfig",true);
        }
        zk.writeData("/zkConfig", config);
        zk.close();
    }
}