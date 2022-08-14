package com.bigshen.learningDemo.utils.bo;

public class Index {

	/** 索引名 */
	private String index;
	
	/** type名 */
    private String type;
    
    /** 主分片数 */
    private Integer number_of_shards = 5;
    
    /** 副分份数 */
    private Integer number_of_replicas = 1;
    
    /** 字段类型json字符串 */
    private String fieldType;

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getNumber_of_shards() {
		return number_of_shards;
	}

	public void setNumber_of_shards(Integer number_of_shards) {
		this.number_of_shards = number_of_shards;
	}

	public Integer getNumber_of_replicas() {
		return number_of_replicas;
	}

	public void setNumber_of_replicas(Integer number_of_replicas) {
		this.number_of_replicas = number_of_replicas;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

}
