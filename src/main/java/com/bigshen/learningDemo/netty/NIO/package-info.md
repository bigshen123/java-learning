

NIO优点总结:

优点一：SocketChannel的连接操作是异步的
也就是客户端发起的连接操作SocketChannel.connect()是异步的。
可以将SocketChannel注册到多路复用器上并关注OP_CONNECT事件等待后续结果，不需要像BIO的客户端那样被同步阻塞。

优点二：SocketChannel的读写操作都是异步的
也就是客户端发起的读写操作SocketChannel.read()和write()是异步的。如果没有可读写的数据它不会同步等待，而会直接返回。
这样IO通信链路就可以处理其他链路了，不需要同步等待这个链路可用。

优点三：优化了线程模型
由于JDK的Selector在Linux等主流操作系统上通过epoll实现，从而没有了连接句柄数的限制。这意味着一个Selector线程可以同时处理成千上万个客户端连接，
而且性能不会随客户端增加而线性下降。注意：Selector.select()是同步阻塞的。

优点四：优化了IO读写
BIO的读写是面向流的，一次性只能从流中读取一子节或多字节，而且读完后流无法再读取，需要自己缓存数据。NIO的读写是面向Buffer的，可随意读取里面任何字节的数据。
不需要自己缓存数据，只需要移动读写指针即可。




NIO问题总结:

问题一：NIO的类库和API繁杂，需要熟练掌握Selector、ServerSocketChannel、SocketChannel、ByteBuffer等。

问题二：处理常见问题的工作量和难度比较大，比如客户断连重连、网络闪断、半包读写、失败缓存、网络拥塞和异常码流等。

问题三：NIO的epoll bug会导致Selector空轮询，最终导致CPU达到100%。










