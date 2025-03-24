
一、Selector简介
Selector会不断地轮询注册在其上的Channel。如果某个Channel上发生读或写事件，那么这个Channel就处于就绪状态。
然后就绪的Channel就会被Selector轮询出来，具体就是通过Selector的SelectionKey来获取就绪的Channel集合。
获取到就绪的Channel后，就可以进行后续的IO操作了。

一个Selector多路复用器可以同时轮询多个Channel。由于JDK使用了epoll()代替传统的select实现，
所以它并没有最大连接句柄1024/2048的限制。这意味着只需要一个线程负责Selector多路复用器的轮询，就可以接入成千上万的客户端。






















