package com.bigshen.learningDemo.jni.execDemo;

/**
 * @Author BYJ
 * @Date 2023/11/16 22:23
 * @Describe
 */
import com.bigshen.learningDemo.jni.sdfDemo.NativeUtils;

import java.io.IOException;


public class UID {
    static {
        try {
            NativeUtils.loadLibraryFromJar("/uid.so");
        } catch (IOException e) {
            e.printStackTrace(); // This is probably not the best way to handle exception :-)
        }
    }

    public static void printids() {
        System.err.println("uid: " + getuid());
        System.err.println("euid: " + geteuid());
        System.err.println("gid: " + getgid());
        System.err.println("egid: " + getegid());
    }

    public static void seteids() {
        //printids();
        //System.err.println("setting eids");
        seteuid(getuid());
        setegid(getgid());
        //printids();
    }

    public static native void setuid(int id);
    public static native void seteuid(int id);
    public static native int getuid();
    public static native int geteuid();
    public static native void setgid(int id);
    public static native void setegid(int id);
    public static native int getgid();
    public static native int getegid();
}
