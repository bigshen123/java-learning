package com.bigshen.learningDemo.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bigshen.learningDemo.utils.bo.Index;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.settings.Settings.Builder;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * 原生基于JAVA ES操作
 * @author Admin
 *
 */
public class EsOperationUtil {
	
	/** 静态变量：系统日志 */
	private static final Log logger = LogFactory.getLog(EsOperationUtil.class);

	/** es client */
	private static TransportClient client;
	
	/** 索引client */
	private static IndicesAdminClient adminClient;
	
	/** 集群名称 */
	private static String clusterName;
	
	/** 集群主机  */
	private static String clusterHosts;
	
	/** 索引json文件路径 */
	private static String indexFilePath;
	
	/** 索引json文件路径 */
	private static String appendFilePath;
	
	// 初始化成员变量
	static {
		String filename = "elasticsearch.properties";
		clusterName = PropertiesUtil.getProperty(filename, "cluster.name");
		clusterHosts = PropertiesUtil.getProperty(filename, "cluster.hosts");
		indexFilePath = PropertiesUtil.getProperty(filename, "index.file.path");
		appendFilePath = PropertiesUtil.getProperty(filename, "append.file.path");
		
		try {
			if(clusterName != null || clusterHosts!=null) {
				// 设置集群名称
				Settings settings = Settings.builder().put("cluster.name", clusterName).build();
				// 创建client
				client = new PreBuiltTransportClient(settings);
				// 添加集群节点
				String[] hosts = clusterHosts.split(",");
				for(String host : hosts) {
					String[] ipAndPort = host.split(":");
					client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(ipAndPort[0]), Integer.parseInt(ipAndPort[1])));
				}
				
				adminClient = client.admin().indices();
			} else {
				if(logger.isErrorEnabled()) {
					logger.error("Initialization TransportClient failed.");
				}
			}
		} catch (Exception e) {
			if(logger.isErrorEnabled()) {
				logger.error("Initialization TransportClient failed. Error message: " + e.getMessage());
			}
			e.printStackTrace();
		}
	}
	
	/**
	 * 创建索引
	 * @return
	 */
	public static boolean createIndex() {
		boolean flag = false;
		
		if(adminClient==null || indexFilePath==null) {
			if(logger.isDebugEnabled()) {
				logger.debug("Failed to create the index, IndicesAdminClient is null or no index json file was found.");
			}
			return flag;
		}
		
    	try {
    		List<String> successIndex = new ArrayList<String>();   // 记录创建成功的索引名
    		List<Index> list = getIndexList(indexFilePath);        // 需要创建的索引
    		if(list != null) {
        		for(Index index : list){
                    if (isExists(index.getIndex())) {
                    	System.out.println(index.getIndex() + " index already exists.");
                        continue;
                    }
                    
                    Builder setting = Settings.builder()
                    		.put("index.number_of_shards", index.getNumber_of_shards())
                    		.put("index.number_of_replicas", index.getNumber_of_replicas())
                    		.put("index.max_result_window", 999999999);
                    
                    adminClient.prepareCreate(index.getIndex())
        		            .setSettings(setting)
        		            .addMapping(index.getType(), index.getFieldType(), XContentType.JSON)
        		            .get();
                    successIndex.add(index.getIndex());
                }
        		flag = true;
    		}
    	} catch(Exception e) {
    		if(logger.isErrorEnabled()) {
				logger.error("Index creation failed, error message: " + e.getMessage());
			}
    		e.printStackTrace();
    	}
    	
    	return flag;
	}
	
	/**
	 * 添加新字段
	 */
	public static boolean appendMapping() {
		boolean flag = false;
		
		if(adminClient==null || indexFilePath==null) {
			if(logger.isDebugEnabled()) {
				logger.debug("Failed to create the index, IndicesAdminClient is null or no index json file was found.");
			}
			return flag;
		}
		
    	try {
    		List<String> updateList = new ArrayList<String>();     // 记录修改成功的索引名
    		List<Index> list = getIndexList(appendFilePath);       // 需要新增的字段
    		if(list != null) {
        		for(Index index : list) {
                    PutMappingResponse response = adminClient.preparePutMapping(index.getIndex())
                    		.setType(index.getType())
                    		.setSource(index.getFieldType(), XContentType.JSON)
        		            .get();
                    boolean acknowledged = response.isAcknowledged();
                    if(acknowledged) {
                    	updateList.add(index.getIndex());
                    }
                }
        		if(updateList.size() != 0) {
        			flag = true;
        		}
    		}
    	} catch(Exception e) {
    		if(logger.isErrorEnabled()) {
				logger.error("Index creation failed, error message: " + e.getMessage());
			}
    		e.printStackTrace();
    	}
    	
    	return flag;
	}
	
	/**
	 * 删除索引
	 * @param indices 索引名
	 * @return true:删除成功; false:删除失败
	 * @throws Exception
	 */
	public static boolean deleteIndex(String... indices) throws Exception {
		if(indices.length == 0) {
			return false;
		}
		
		List<String> list = new ArrayList<String>();
		for(String index : indices) {
			if(isExists(index)) {
				list.add(index);
			}
		}
		
		if(list.size() ==0) {
			return true;
		}
		indices = list.toArray(new String[0]);
		DeleteIndexResponse response = adminClient.prepareDelete(indices).get();
		return response.isAcknowledged();
	}
	
	/**
	 * 判断索引是否存在
	 * @param index 索引名
	 * @return true: 存在, false: 不存在
	 */
	private static boolean isExists(String index) throws Exception {
		if(adminClient == null) {
			if(logger.isErrorEnabled()) {
				logger.error("isExists function: When determining whether an index exists or not, adminClient is null.");
			}
			return false;
		}
		
		IndicesExistsRequest request = new IndicesExistsRequest(index);
        IndicesExistsResponse response = adminClient.exists(request).actionGet();
        if (response.isExists()) {
            return true;
        }
        return false;
	}
	
	/**
	 * 解析json文件内容
	 * @return
	 */
	private static List<Index> getIndexList(String filePath) {
        JSONArray jsonArray = null;  
        try {  
            jsonArray = readJsonFile(filePath);  
        } catch (Exception e) {  
            if(logger.isErrorEnabled()) {
            	logger.error("Failure to read json file, error message:  " + e.getMessage());
            }
            e.printStackTrace();
        }
        
        if (jsonArray == null || jsonArray.size() == 0) {  
            return null;  
        }
        
        List<Index> list = new ArrayList<Index>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String index = jsonObject.getString("index");
            String type = jsonObject.getString("type");
            Integer number_of_shards = jsonObject.getInteger("number_of_shards");
            Integer number_of_replicas = jsonObject.getInteger("number_of_replicas");
            String fieldType = jsonObject.get("fieldType").toString();
            
            Index indexObject = new Index();
            indexObject.setIndex(index);
            indexObject.setType(type);
            indexObject.setFieldType(fieldType);
            indexObject.setNumber_of_shards(number_of_shards);
            indexObject.setNumber_of_replicas(number_of_replicas);
            list.add(indexObject);
        }  
        return list;
	}
	
	/**
	 * 读取索引json文件
	 * @return
	 * @throws Exception
	 */
	private static JSONArray readJsonFile(String filePath) throws Exception {
		InputStream is = new EsOperationUtil().getClass().getClassLoader().getResourceAsStream(filePath);
        BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        StringBuffer sb = new StringBuffer();
        
        String tmp = null;
        while((tmp=br.readLine()) != null) {
            sb.append(tmp);
        }
        return JSONArray.parseArray(sb.toString());
	}
	
	/** 
     * 关闭连接
     */  
    public static void close() {  
    	if(client != null) {
    		client.close();  
    	}
    }
	
}
