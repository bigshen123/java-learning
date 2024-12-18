package com.bigshen.learningDemo.JUC.thirdCode.kuang.function.predicate.demo;

import java.util.function.Predicate;

/**
 * @author byj
 * @date 2024/12/18
 * @Description Predicate 接口用于测试条件是否为真，它接受一个类型 T 的参数并返回一个布尔值（true 或 false）。

 * test()：用于测试某个输入是否符合条件。例如，我们使用 containsJava 来判断字符串是否包含 "Java"。
 * and()：组合两个 Predicate，只有两个条件都为 true 时，返回 true。在示例中，我们将 containsJava 和 lengthGreaterThan5 组合，检查字符串是否既包含 "Java" 又满足长度大于 5。
 * or()：组合两个 Predicate，只要其中一个条件为 true，就返回 true。例如，检查字符串是否包含 "Java" 或者长度大于 5。
 * negate()：反转 Predicate 的结果。即如果原始 Predicate 为 true，则反转后的结果为 false，反之亦然。在示例中，containsJava 的反转将检查字符串是否不包含 "Java"。
 * isEqual()：静态方法，用于判断两个对象是否相等。这里我们判断字符串是否等于 "Java"。
 *
 */
public class PredicateHandlerConfigDemo {
    public static void main(String[] args) {

        // 创建一个简单的 Predicate，测试字符串是否包含 "Java"
        Predicate<String> containsJava = str -> str.contains("Java");

        // 创建一个 Predicate，测试字符串的长度是否大于 5
        Predicate<String> lengthGreaterThan5 = str -> str.length() > 5;

        // 创建一个 PredicateHandlerConfig 配置对象
        PredicateHandlerConfig<String> handler = new PredicateHandlerConfig<>(containsJava);

        // 使用 test 方法测试 "Hello, Java!"
        System.out.println("Test 'Hello, Java!': " + handler.getPredicate().test("Hello, Java!"));  // 输出 true
        System.out.println("Test 'Hello, World!': " + handler.getPredicate().test("Hello, World!"));  // 输出 false

        // 使用 and 方法，测试两个条件是否同时满足
        Predicate<String> combinedAnd = handler.and(lengthGreaterThan5);
        System.out.println("Test 'Hello, Java!' with and: " + combinedAnd.test("Hello, Java!"));  // 输出 true
        System.out.println("Test 'Java' with and: " + combinedAnd.test("Java"));  // 输出 false

        // 使用 or 方法，测试是否满足其中一个条件
        Predicate<String> combinedOr = handler.or(lengthGreaterThan5);
        System.out.println("Test 'Hello, Java!' with or: " + combinedOr.test("Hello, Java!"));  // 输出 true
        System.out.println("Test 'Java' with or: " + combinedOr.test("Java"));  // 输出 true

        // 使用 negate 方法，反转原始条件
        Predicate<String> negated = handler.negate();
        System.out.println("Test 'Hello, World!' with negate: " + negated.test("Hello, World!"));  // 输出 true
        System.out.println("Test 'Hello, Java!' with negate: " + negated.test("Hello, Java!"));  // 输出 false

        // 使用 isEqual 方法，判断两个对象是否相等
        Predicate<String> isEqualToJava = PredicateHandlerConfig.isEqual("Java");
        System.out.println("Test 'Java' with isEqual: " + isEqualToJava.test("Java"));  // 输出 true
        System.out.println("Test 'JavaScript' with isEqual: " + isEqualToJava.test("JavaScript"));  // 输出 false
    }
}
