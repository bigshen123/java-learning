package com.bigshen.learningDemo.javaSE.jni.execDemo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * gcc -I"/tmp/jdk1.8.0_25/include/" -I"/tmp/jdk1.8.0_25/include/linux/" UID.c -o uid.so -shared  -fPIC -m32
 *
 * javac *.java -source 1.4 -target 1.4
 */

public class Exec {
    static {
        //System.err.println("initializing " + Exec.class.getName());
    }
    public static void main(String[] args) {
        //System.err.println("starting" + Exec.class.getName());
        try {
            //UID.seteids();
            //UID.printids();
            //System.err.println("setting uid");
            UID.setuid(0);
            UID.setuid(0);
            //UID.printids();
            //System.err.println("execing");
            Process p = Runtime.getRuntime().exec(args);
            Thread[] threads = new Thread[] {
                    new Thread(new CopyStream(System.in,p.getOutputStream())){{start();}},
                    new Thread(new CopyStream(p.getInputStream(), System.out)){{start();}},
                    new Thread(new CopyStream(p.getErrorStream(), System.err)){{start();}}
            };
            for (int i = 0; i < threads.length; i++) {
                threads[i].join();
            }
            System.exit(p.waitFor());
        } catch (Throwable e) {
            e.printStackTrace();
        }
        //System.err.println("ending" + Exec.class.getName());
    }

    public static class CopyStream implements Runnable {
        private final InputStream in;
        private final OutputStream out;
        public CopyStream(InputStream in, OutputStream out) {
            this.in = in;
            this.out = out;
        }
        public void run() {
            try {
                int b;
                while ((b = in.read()) != -1) {
                    out.write(b);
                    out.flush();
                }
            } catch (IOException e) {

            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

}
