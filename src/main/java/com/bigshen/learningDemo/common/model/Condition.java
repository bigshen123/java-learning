package com.bigshen.learningDemo.common.model;


import com.bigshen.learningDemo.common.util.HumpUtil;
import com.bigshen.learningDemo.common.util.ResourceUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

import java.util.*;

/**
 * 计数、删除的模糊条件
 *
 * @author gaodq on 2019/1/14
 */
@Setter
@Getter
public class Condition extends BaseJsonModel {

    private static final long serialVersionUID = 5731639901348497286L;

    @ApiModelProperty(name = "fields[{type}]", value = "需要查询的字段，可过滤嵌套的字段，eg：fields[users]=id,name(仅查询用户表的id，name)")
    @JsonIgnore
    private String fieldsSwaggerView;
    private Map<String, List<String>> fields;

    @ApiModelProperty(name = "search[{field}]", value = "模糊查询,仅支持不同字段and连接,不支持id的模糊查询(忽略大小写),eg: type ilike '%or%' and field ilike '%name%' ")
    @JsonIgnore
    protected String searchSwaggerView;
    protected Map<String, String> search;

    @ApiModelProperty(name = "searchL[{field}]", value = "左模糊查询,仅支持不同字段and连接,不支持id的模糊查询(忽略大小写),eg: type ilike '%gs' and field ilike '%me' ")
    @JsonIgnore
    protected String searchLSwaggerView;
    protected Map<String, String> searchL;

    @ApiModelProperty(name = "searchR[{field}]", value = "右模糊查询,仅支持不同字段and连接,不支持id的模糊查询(忽略大小写),eg: type ilike 'org%'  and field ilike 'na%' ")
    @JsonIgnore
    protected String searchRSwaggerView;
    protected Map<String, String> searchR;

    @ApiModelProperty(name = "searchOr[{field}]", value = "模糊查询,仅支持不同字段OR连接,不支持id的模糊查询(忽略大小写),eg: type ilike '%or%' or field ilike '%name%' ")
    @JsonIgnore
    protected String searchOrSwaggerView;
    protected Map<String, String> searchOr;

    @ApiModelProperty(name = "filter[{field}]", value = "等于,相同字段or,不同字段and,eg: (type = 'userGroups' or type = 'users') and id = '10' ")
    @JsonIgnore
    protected String filterSwaggerView;
    protected Map<String, List<String>> filter;

    @ApiModelProperty(name = "filterOr[{field}]", value = "等于,相同字段or,不同字段or,eg: (type = 'userGroups' or type = 'users') or id = '10' ")
    @JsonIgnore
    protected String filterOrSwaggerView;
    protected Map<String, List<String>> filterOr;

    @ApiModelProperty(name = "neq[{field}]", value = "不相等,相同字段and,不同字段and,eg: type != 'users' and type != 'userGroups' and id != '10' ")
    @JsonIgnore
    protected String neqSwaggerView;
    protected Map<String, List<String>> neq;

    @ApiModelProperty(name = "lt[{field}]", value = "小于,仅支持不同字段and连接,eg: type < 'users' and id < '10' ")
    @JsonIgnore
    protected String ltSwaggerView;
    protected Map<String, String> lt;

    @ApiModelProperty(name = "lte[{field}]", value = "小于等于,仅支持不同字段and连接,eg: type <= 'users' and id <= '10' ")
    @JsonIgnore
    protected String lteSwaggerView;
    protected Map<String, String> lte;

    @ApiModelProperty(name = "gt[{field}]", value = "大于, 仅支持不同字段and连接,eg: type > 'userGroups' and id > '10' ")
    @JsonIgnore
    protected String gtSwaggerView;
    protected Map<String, String> gt;

    @ApiModelProperty(name = "gte[{field}]", value = "大于等于，仅支持不同字段and连接,eg: type >= 'userGroups' and id >= '10' ")
    @JsonIgnore
    protected String gteSwaggerView;
    protected Map<String, String> gte;


    @ApiModelProperty(value = "排序,eg1: sort=-name,age(分别按照name的倒叙和age升序排序), eg2:sort=-name,-age(分别按照name的倒叙和age倒叙排序)")
    private List<String> sort;


    @ApiModelProperty(name = "page[{offset,limit,count}]", value = "分页查询，offset 指行号下标，从0开始, 大于或等于0，缺省0; limit 指从第offset取多少行，大于或等于0， 缺省Integer.MAX_VALUE; count 小于1 表示不查询总数，缺省查询总数。 例如:page[offset]=1,page[limit]=5，表示取下标为1~5行的数据")
    @JsonIgnore
    private String pageSwaggerView;
    private Map<String, Integer> page;


    @ApiModelProperty(value = "查询嵌套的表，eg：include=userGroups, (查询用户，同时查询用户对应的用户组)")
    private List<String> include;

    @ApiModelProperty(value = "分组查询，与聚合函数一起使用，目前用于influxDb的多维度查询， eg：group=time(30m), (以每30分钟为一组进行聚合)")
    private List<String> group;

    /**
     * 当前 SQL 语句中 模糊查询 相关 关键字 为null或空字符 则用默认
     */
    private String sqlLikeString;

    public Condition() {
    }

    public Condition(String sqlLikeString) {
        this.sqlLikeString = sqlLikeString;
    }

    public void addField(Class<? extends Resource> resourceClass, String... fields) {
        addField(ResourceUtil.getPath(resourceClass), fields);
    }

    public void addField(String resourcePath, String... fields) {
        if (this.fields == null) {
            this.fields = new HashMap<>(10);
        }
        addMapData(this.fields, resourcePath, fields);
    }

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    public List<String> getFields(String resourceType) {
        return Optional.ofNullable(fields).map(it -> it.get(resourceType)).orElse(null);
    }

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    public List<String> getFields(Class<?> resourceClass) {
        return getFields(ResourceUtil.getPath(resourceClass));
    }

    public void addSearch(String field, String value) {
        if (search == null) {
            search = new HashMap<>(10);
        }
        search.put(field, value);
    }

    public void addSearchL(String field, String value) {
        if (searchL == null) {
            searchL = new HashMap<>(10);
        }
        searchL.put(field, value);
    }

    public void addSearchR(String field, String value) {
        if (searchR == null) {
            searchR = new HashMap<>(10);
        }
        searchR.put(field, value);
    }


    public void addSearchOr(String field, String value) {
        if (searchOr == null) {
            searchOr = new HashMap<>(10);
        }
        searchOr.put(field, value);
    }


    public void addFilter(String field, String... values) {
        if (filter == null) {
            filter = new HashMap<>(10);
        }
        addMapData(filter, field, values);
    }

    public void addFilterOr(String field, String... values) {
        if (filterOr == null) {
            filterOr = new HashMap<>(10);
        }
        addMapData(filterOr, field, values);
    }

    public void addNeq(String field, String... values) {
        if (this.neq == null) {
            this.neq = new HashMap<>(10);
        }
        addMapData(neq, field, values);
    }

    public void addLt(String field, String value) {
        if (lt == null) {
            lt = new HashMap<>(10);
        }
        lt.put(field, value);
    }

    public void addLte(String field, String value) {
        if (lte == null) {
            lte = new HashMap<>(10);
        }
        lte.put(field, value);
    }

    public void addGt(String field, String value) {
        if (gt == null) {
            gt = new HashMap<>(10);
        }
        gt.put(field, value);
    }

    public void addGte(String field, String value) {
        if (gte == null) {
            gte = new HashMap<>(10);
        }
        gte.put(field, value);
    }

    public void addSort(String... fields) {
        if (sort == null) {
            sort = new ArrayList<>(5);
        }
        Collections.addAll(sort, fields);
    }

    public void addInclude(String resourceType) {
        if (getInclude() == null) {
            setInclude(new ArrayList<>());
        }
        if (!getInclude().contains(resourceType)) {
            getInclude().add(resourceType);
        }
    }

    public void setPage(int offset, int limit) {
        if (getPage() == null) {
            setPage(new HashMap<>());
        }
        getPage().put("offset", offset);
        getPage().put("limit", limit);
    }

    public void setPage(int offset, int limit, boolean count) {
        if (getPage() == null) {
            setPage(new HashMap<>());
        }
        getPage().put("offset", offset);
        getPage().put("limit", limit);
        getPage().put("count", count ? 1 : 0);
    }

    @JsonIgnore
    public Map<String, List<String>> getUnderlineFields() {
        return HumpUtil.getUnderlineValueMap(fields);
    }

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    public boolean isSorted() {
        return CollectionUtils.isNotEmpty(sort);
    }

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    public boolean isSortByDesc() {
        return isSorted() && sort.get(0).startsWith("-");
    }

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    public boolean isPaged() {
        return MapUtils.isNotEmpty(page) && (page.containsKey("offset") || page.containsKey("limit"));
    }

    /**
     * 数据库的 offset 从 0 开始
     * <p>
     * 缺省 0
     *
     * @return offset
     */
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    public int getPageOffset() {
        return Optional.ofNullable(page)
                .map(map -> map.get("offset") == null ? 0 : map.get("offset"))
                .filter(offset -> offset >= 0)
                .orElse(0);
    }

    /**
     * 缺省 Integer.MAX_VALUE
     *
     * @return limit
     */
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    public int getPageLimit() {
        return Optional.ofNullable(page)
                .map(map -> map.get("limit") == null ? Integer.MAX_VALUE : map.get("limit"))
                .filter(limit -> limit >= 0)
                .orElse(Integer.MAX_VALUE);
    }

    /**
     * 缺省 true
     *
     * @return limit
     */
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    public boolean getPageCount() {
        return Optional.ofNullable(page)
                .map(map -> map.get("count") == null ? 1 : map.get("count"))
                .orElse(1) > 0;
    }

    private void addMapData(Map<String, List<String>> map, String field, String... values) {
        if (map.get(field) == null) {
            map.put(field, new ArrayList<>(Arrays.asList(values)));
        } else {
            for (String value : values) {
                map.get(field).add(value);
            }
        }
    }

}
