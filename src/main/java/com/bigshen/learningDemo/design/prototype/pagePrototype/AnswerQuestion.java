package com.bigshen.learningDemo.design.prototype.pagePrototype;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 解答题
 * @author BYJ
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnswerQuestion {

    /**
     * 问题
     */
    private String name;
    /**
     * 答案
     */
    private String key;
}
