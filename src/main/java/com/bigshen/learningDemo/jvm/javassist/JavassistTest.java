package com.bigshen.learningDemo.jvm.javassist;

import com.bigshen.learningDemo.jvm.asm.Base;
import javassist.*;

import java.io.IOException;

/**
 * @Author BYJ
 * @Date 2025/1/2 21:15
 * @Describe {@url https://www.pdai.tech/md/java/jvm/java-jvm-class-enhancer.html#javassist }
 */
public class JavassistTest {
    public static void main(String[] args) throws NotFoundException, CannotCompileException, IllegalAccessException, InstantiationException, IOException {
        ClassPool cp = ClassPool.getDefault();
        CtClass cc = cp.get("com.bigshen.learningDemo.jvm.asm.Base");
        CtMethod m = cc.getDeclaredMethod("process");
        m.insertBefore("{ System.out.println(\"start\"); }");
        m.insertAfter("{ System.out.println(\"end\"); }");
        Class c = cc.toClass();
        cc.writeFile("D:\\");
        Base h = (Base)c.newInstance();
        h.process();
    }
}