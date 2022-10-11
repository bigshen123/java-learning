package com.bigshen.learningDemo.common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author byj
 * @date 2022/10/11
 * 查询多个的返回Model
 */
@Setter
@Getter
public class CollectionModel<T> extends BaseJsonModel {
    private static final long serialVersionUID = 3368275375070005995L;

    @ApiModelProperty(value = "响应中首个数组元素在所有元素（`totalItems`）中的第几项，从0开始，小于0表示元素")
    private int startIndex = -1;
    @ApiModelProperty(value = "数据库中所有元素的个数, 小于0表示没有总数")
    private int totalItems = -1;
    @ApiModelProperty(value = "查询的数据集合")
    private List<T> items;

    public CollectionModel() {
        this.items = new ArrayList<>();
    }

    /**
     * 抽取构造默认CollectionModel
     *
     * @param list      items
     * @param condition SelectListCondition
     */
    public CollectionModel(List<T> list, Condition condition) {

        setItems(list);
        if (condition != null) {
            setStartIndex(condition.getPageOffset());
        }
    }

    @JsonIgnore
    public boolean isEmpty() {
        return items == null || items.isEmpty();
    }

}
