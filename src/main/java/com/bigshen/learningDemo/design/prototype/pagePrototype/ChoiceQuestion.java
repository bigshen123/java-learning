package com.bigshen.learningDemo.design.prototype.pagePrototype;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

/**
 * 单选题
 * @author BYJ
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChoiceQuestion {

    /**
     * 题目
     */
    private String name;
    /**
     *  选项；A、B、C、D
      */
    private Map<String, String> option;
    /**
     * 答案；B
     */
    private String key;
}
