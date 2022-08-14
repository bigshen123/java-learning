package com.bigshen.learningDemo.utils.idGenerator.two;
import org.junit.Test;

/**
 * @ClassName TestIDGenerate
 * @Description: TODO
 * @Author BYJ
 * @Date 2020/3/8
 * @Version V1.0
 **/
public class TestIDGenerate {

    @Test
    public void testGenerateID() {
        RandomLogTraceIDGenerator gen = new RandomLogTraceIDGenerator();

        String id = gen.generate();
        String[] parts = id.split("-");

        String lastHostName = parts[0];
        System.out.println("lastHostName:"+lastHostName);
        String randChars = parts[1];
        System.out.println("randChars:"+randChars);
        String time = parts[2];
        System.out.println("time:"+time);
        System.out.println(id);
    }
}
