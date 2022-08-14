package com.bigshen.learningDemo.utils.sql;

import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional(rollbackFor=Throwable.class)
@Component
public class DbUtil {
    
    private static final Logger logger = LoggerFactory.getLogger(DbUtil.class);
    
    public static final String DATABASE_TYPE_ORACLE = "oracle";
    public static final String DATABASE_TYPE_POSTGRESQL = "postgresql";
    
    /** 分页前缀 */
    private static final Map<String, String> LIMIT_BEFORE_SQL = new HashMap<String, String>();
    /** 分页后缀 */
    private static final Map<String, String> LIMIT_AFTER_SQL = new HashMap<String, String>();
    /** 当前时间 */
    private static final Map<String, String> CURRENT_DATE_SQL = new HashMap<String, String>();
    /** 虚拟表 */
    private static final Map<String, String> VIRTUAL_TABLE_SQL = new HashMap<String, String>();
    /** 字符串数据类型 */
    private static final Map<String, String> STRING_DATA_TYPE_SQL = new HashMap<String, String>();
    /** 数字数据类型 */
    private static final Map<String, String> NUMBER_DATA_TYPE_SQL = new HashMap<String, String>();
    
    /** 当前数据库类型 */
    private static String CURRENT_DATABASE_TYPE;
    
    @PersistenceContext
    private EntityManager entityManager;

    
    static {
        LIMIT_BEFORE_SQL.put(DATABASE_TYPE_ORACLE, "select * from ( select a.*, rownum rnum from ( ");
        LIMIT_AFTER_SQL.put(DATABASE_TYPE_ORACLE, " ) a where rownum <= {1} ) where rnum > {0}");
        CURRENT_DATE_SQL.put(DATABASE_TYPE_ORACLE, "sysdate");
        VIRTUAL_TABLE_SQL.put(DATABASE_TYPE_ORACLE, "from dual");
        STRING_DATA_TYPE_SQL.put(DATABASE_TYPE_ORACLE, "nvarchar2({0})");
        NUMBER_DATA_TYPE_SQL.put(DATABASE_TYPE_ORACLE, "number");
        
        LIMIT_BEFORE_SQL.put(DATABASE_TYPE_POSTGRESQL, "");
        LIMIT_AFTER_SQL.put(DATABASE_TYPE_POSTGRESQL, " offset {0} limit {1} ");
        CURRENT_DATE_SQL.put(DATABASE_TYPE_POSTGRESQL, "now()");
        VIRTUAL_TABLE_SQL.put(DATABASE_TYPE_POSTGRESQL, "");
        STRING_DATA_TYPE_SQL.put(DATABASE_TYPE_POSTGRESQL, "varchar({0})");
        NUMBER_DATA_TYPE_SQL.put(DATABASE_TYPE_POSTGRESQL, "integer");
    }

    @PostConstruct
    private void init() {
        CURRENT_DATABASE_TYPE = "postgresql";
        logger.info("current database : {}", CURRENT_DATABASE_TYPE);
    }
    
    /**
     * 查询单条记录
     * @param sql
     * @return
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> queryForMap(String sql) {
        if (logger.isDebugEnabled()) {
            logger.debug("query for object sql : {}", sql);
        }
        
        Query query = entityManager.createNativeQuery(sql).unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        Map<String, Object> result = null;
        try {
        	result = (Map<String, Object>) query.getSingleResult();
		} catch (NoResultException e) {
			// getSingleResult()没有查询到记录抛出NoResultException，捕获异常返回null
		}
        
        if (DATABASE_TYPE_POSTGRESQL.equals(CURRENT_DATABASE_TYPE)) {
        	result = MapUtil.mapKeyToUpperCase(result);
        }
        
        return result;
    }

    /**
     * 查询单条记录
     * @param sql
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T queryForMap(String sql, Class<T> clazz) {
        if (logger.isDebugEnabled()) {
            logger.debug("query for list sql : {}", sql);
        }

        Query query = entityManager.createNativeQuery(sql).unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> list = (List<Map<String, Object>>) query.getResultList();
        if (!ObjectUtils.isEmpty(list) && list.size() > 0) if (CURRENT_DATABASE_TYPE.equals(DATABASE_TYPE_POSTGRESQL)) {
            return BeanMapUtil.postgreMap2bean(list.get(0), clazz);
        } else {
            return BeanMapUtil.oracleMap2bean(list.get(0), clazz);
        }
        return null;
    }


    /**
     * 查询第一条条记录
     * @param sql
     * @return
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> queryFirstMap(String sql) {
        if (DATABASE_TYPE_ORACLE.equals(CURRENT_DATABASE_TYPE)) {
            sql = this.oracleQueryForListPage(sql, 1, 1);
        } else if (DATABASE_TYPE_POSTGRESQL.equals(CURRENT_DATABASE_TYPE)) {
            sql = this.postgresqlQueryForListPage(sql, 1, 1);
        } else {
            throw new RuntimeException(String.format("不支持%s数据库", CURRENT_DATABASE_TYPE));
        }
        
        if (logger.isDebugEnabled()) {
            logger.debug("query for object sql : {}", sql);
        }

        Query query = entityManager.createNativeQuery(sql).unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> res = query.getResultList();
        Map<String, Object> result = new HashMap<>();
        if (!ObjectUtils.isEmpty(res) && res.size() > 0) {
            if (DATABASE_TYPE_POSTGRESQL.equals(CURRENT_DATABASE_TYPE)) {
                result = MapUtil.mapKeyToUpperCase(res.get(0));
            }
        }
        return result;
    }

    /**
     * 查询记录总条数
     * @param sql
     * @return
     */
    @SuppressWarnings("unchecked")
    public long queryCount(String sql) {
        if (logger.isDebugEnabled()) {
            logger.debug("query for object sql : {}", sql);
        }

        Query query = entityManager.createNativeQuery(sql).unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        Map<String, Object> result = (Map<String, Object>) query.getSingleResult();

        if (DATABASE_TYPE_POSTGRESQL.equals(CURRENT_DATABASE_TYPE)) {
            return Long.valueOf(result.get("total").toString());
        }
        return Long.valueOf(result.get("TOTAL").toString());
    }
    
    /**
     * 查询单条记录(不改变key的大小写)
     * @param sql
     * @return
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> queryForMapNoChangeKey(String sql) {
        if (logger.isDebugEnabled()) {
            logger.debug("query for object sql : {}", sql);
        }
        
        Query query = entityManager.createNativeQuery(sql).unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        Map<String, Object> result = (Map<String, Object>) query.getSingleResult();
                
        return result;
    }
    
    /**
     * 查询多条记录
     * @param sql
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> queryForList(String sql) {
        if (logger.isDebugEnabled()) {
            logger.debug("query for list sql : {}", sql);
        }
        
        Query query = entityManager.createNativeQuery(sql).unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> result = (List<Map<String, Object>>) query.getResultList();
        
        if (DATABASE_TYPE_POSTGRESQL.equals(CURRENT_DATABASE_TYPE)) {
        	result = MapUtil.listMapKeyToUpperCase(result);
        }
        
        return result;
    }
    
    /**
     * 查询多条记录
     * @param sql
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> queryForList(String sql, Class<T> clazz) {
        if (logger.isDebugEnabled()) {
            logger.debug("query for list sql : {}", sql);
        }

        long startTime = System.currentTimeMillis();
        Query query = entityManager.createNativeQuery(sql).unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> list = (List<Map<String, Object>>) query.getResultList();
        System.out.println("end:"+(System.currentTimeMillis()-startTime));
        List<T> result = new ArrayList<T>();
        for (Map<String, Object> map : list) {
        	if (CURRENT_DATABASE_TYPE.equals(DATABASE_TYPE_POSTGRESQL)) {
            	result.add(BeanMapUtil.postgreMap2bean(map, clazz));
    		} else {
    			result.add(BeanMapUtil.oracleMap2bean(map, clazz));
    		}
		}
        System.out.println("end>>>>>>:"+(System.currentTimeMillis()-startTime));
        return result;
    }
    
    /**
     * 查询多条记录(不改变key的大小写)
     * @param sql
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> queryForListNoChangeKey(String sql) {
        if (logger.isDebugEnabled()) {
            logger.debug("query for list sql : {}", sql);
        }
        
        Query query = entityManager.createNativeQuery(sql).unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> result = (List<Map<String, Object>>) query.getResultList();
        
        return result;
    }
    
    /**
     * 分页查询
     * @param sql
     * @param page 当前页
     * @param pageSize 每页个数
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> queryForListPage(String sql, int page, int pageSize) {
        if (DATABASE_TYPE_ORACLE.equals(CURRENT_DATABASE_TYPE)) {
            sql = this.oracleQueryForListPage(sql, page, pageSize);
        } else if (DATABASE_TYPE_POSTGRESQL.equals(CURRENT_DATABASE_TYPE)) {
            sql = this.postgresqlQueryForListPage(sql, page, pageSize);
        } else {
            throw new RuntimeException(String.format("不支持%s数据库", CURRENT_DATABASE_TYPE));
        }
        
        if (logger.isDebugEnabled()) {
            logger.debug("query for list page sql : {}", sql);
        }
        
        Query query = entityManager.createNativeQuery(sql).unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> result = (List<Map<String, Object>>) query.getResultList();
        
        if (DATABASE_TYPE_POSTGRESQL.equals(CURRENT_DATABASE_TYPE)) {
        	result = MapUtil.listMapKeyToUpperCase(result);
        }
        
        return result;
    }
    
    /**
     * 分页查询
     * @param sql
     * @param page 当前页
     * @param pageSize 每页个数
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> queryForListPage(String sql, int page, int pageSize, Class<T> clazz) {
        if (DATABASE_TYPE_ORACLE.equals(CURRENT_DATABASE_TYPE)) {
            sql = this.oracleQueryForListPage(sql, page, pageSize);
        } else if (DATABASE_TYPE_POSTGRESQL.equals(CURRENT_DATABASE_TYPE)) {
            sql = this.postgresqlQueryForListPage(sql, page, pageSize);
        } else {
            throw new RuntimeException(String.format("不支持%s数据库", CURRENT_DATABASE_TYPE));
        }
        
        if (logger.isDebugEnabled()) {
            logger.debug("query for list page sql : {}", sql);
        }
        
        Query query = entityManager.createNativeQuery(sql).unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> list = (List<Map<String, Object>>) query.getResultList();
        
        List<T> result = new ArrayList<T>();
        for (Map<String, Object> map : list) {
        	if (CURRENT_DATABASE_TYPE.equals(DATABASE_TYPE_POSTGRESQL)) {
            	result.add(BeanMapUtil.postgreMap2bean(map, clazz));
    		} else {
    			result.add(BeanMapUtil.oracleMap2bean(map, clazz));
    		}
		}
        return result;
    }

    /**
     * 分页查询(传入开始、结束索引)
     * @param sql
     * @param
     * @param
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> queryForListPageByStartEnd(String sql, int start, int end) {
        if (DATABASE_TYPE_ORACLE.equals(CURRENT_DATABASE_TYPE)) {
            sql = this.getPageSql(sql, start, end);
        } else if (DATABASE_TYPE_POSTGRESQL.equals(CURRENT_DATABASE_TYPE)) {
            sql = this.getPageSql(sql, start, end-start+1);
        } else {
            throw new RuntimeException(String.format("不支持%s数据库", CURRENT_DATABASE_TYPE));
        }
        
        if (logger.isDebugEnabled()) {
            logger.debug("query for list page sql : {}", sql);
        }

        Query query = entityManager.createNativeQuery(sql).unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> result = (List<Map<String, Object>>) query.getResultList();

        if (DATABASE_TYPE_POSTGRESQL.equals(CURRENT_DATABASE_TYPE)) {
            result = MapUtil.listMapKeyToUpperCase(result);
        }

        return result;
    }

    /**
     * 分页查询(传入开始、结束索引)
     * @param sql
     * @param
     * @param
     * @return
     */
    /*@SuppressWarnings("unchecked")
    public <T> List<T> queryForListPageByStartEnd(String sql, int start, int end, Class<T> clazz) throws Exception {
        if (DATABASE_TYPE_ORACLE.equals(CURRENT_DATABASE_TYPE)) {
            sql = this.getPageSql(sql, start, end);
        } else if (DATABASE_TYPE_POSTGRESQL.equals(CURRENT_DATABASE_TYPE)) {
            sql = this.getPageSql(sql, start, end-start+1);
        } else {
            throw new RuntimeException(String.format("不支持%s数据库", CURRENT_DATABASE_TYPE));
        }
        
        if (logger.isDebugEnabled()) {
            logger.debug("query for list page sql : {}", sql);
        }

        Query query = entityManager.createNativeQuery(sql).unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> list = (List<Map<String, Object>>) query.getResultList();

        List<T> result = new ArrayList<T>();
        logger.info("list:>>>>>>>>>>>>>>"+list.size());
        if (!ObjectUtils.isEmpty(list) && list.size() > 0) {
            for (Map<String, Object> map : list) {
                if (CURRENT_DATABASE_TYPE.equals(DATABASE_TYPE_POSTGRESQL)) {
                    result.add(BeanMapUtil.postgreMap2bean(map, clazz));
                } else {
                    result.add(BeanMapUtil.oracleMap2bean(map, clazz));
                }
            }
        }
        return result;
    }*/

    /**
     * 执行SQL
     * @param
     * @return
     */
    @Modifying
	@Transactional(rollbackFor=Throwable.class)
    public void executeUpdate(String... sqls) {
    	for (String sql : sqls) {
    		entityManager.createNativeQuery(sql).executeUpdate();
		}
    }

    /**
     * 获取当前时间的sql
     * @return
     */
    public static String getCurrentDateSql() {
        return CURRENT_DATE_SQL.get(CURRENT_DATABASE_TYPE);
    }

    /**
     * 获取虚拟表的sql
     * @return
     */
    public static String getVirtualTableSql() {
        return VIRTUAL_TABLE_SQL.get(CURRENT_DATABASE_TYPE);
    }
    
    /**
     * 获取当前数据库类型
     * @return
     */
    public static String getCurrentDatabaseType() {
    	return CURRENT_DATABASE_TYPE;
    }
    
    /**
     * 获取字符串数据类型sql
     * @param length 字段长度
     * @return
     */
    public static String getStringDataTypeSql(Integer length) {
    	return stringFormat(STRING_DATA_TYPE_SQL.get(CURRENT_DATABASE_TYPE), length.toString());
    }
    
    /**
     * 获取数字数据类型sql
     * @return
     */
    public static String getNumberDataTypeSql() {
    	return NUMBER_DATA_TYPE_SQL.get(CURRENT_DATABASE_TYPE);
    }

    private String oracleQueryForListPage(String sql, int page, int pageSize) {
        int start = (page -1) * pageSize;
        int end = start + pageSize;
        return getPageSql(sql, start, end);
    }

    private String postgresqlQueryForListPage(String sql, int page, int pageSize) {
        int start = (page -1) * pageSize;
        return getPageSql(sql, start, pageSize);
    }

    private String getPageSql(String sql, int start, int end){
        sql = new StringBuilder()
                .append(LIMIT_BEFORE_SQL.get(CURRENT_DATABASE_TYPE))
                .append(sql)
                .append(stringFormat(LIMIT_AFTER_SQL.get(CURRENT_DATABASE_TYPE), Integer.toString(start), Integer.toString(end)))
                .toString();
        return sql;
    }
    
    private static String stringFormat(String str, Object... objects) {
        return MessageFormat.format(str, objects);
    }
    
    
    public static void main(String[] args) {
		System.out.println(MessageFormat.format("offset {0} limit {1}", 1, Integer.toString(100000)));
	}
    
}
