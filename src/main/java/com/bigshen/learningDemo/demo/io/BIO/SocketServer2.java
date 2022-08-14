package com.bigshen.learningDemo.demo.io.BIO;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import org.apache.log4j.BasicConfigurator;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Author BYJ
 * @Date 2022/5/18 11:25
 * @Describe 多线程来优化服务器端
 */
public class SocketServer2 {

    static {
        BasicConfigurator.configure();
    }

    private static final Log LOGGER = LogFactory.getLog(SocketServer2.class);

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(83);
        try{
            while (true){
                Socket socket = serverSocket.accept();
                //当然业务处理过程可以交给一个线程(这里可以使用线程池),并且线程的创建是很耗资源的。
                //最终改变不了.accept()只能一个一个接受socket的情况,并且被阻塞的情况
                SocketServerThread socketServerThread = new SocketServerThread(socket);
                new Thread(socketServerThread).start();
            }
        }catch (Exception e){
            SocketServer2.LOGGER.error(e.getMessage(), e);
        } finally {
            serverSocket.close();
        }
    }
}
