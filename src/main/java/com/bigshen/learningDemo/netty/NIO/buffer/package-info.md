

(1)Buffer缓冲区的作用
* Buffer缓冲区的作用：在NIO中，所有的数据都是通过使用Buffer缓冲区来处理的。
* 如果要通过NIO，将数据写到文件和网络或从文件和网络中读取数据，那么就需要使用Buffer缓冲区来进行处理。

(2)Buffer缓冲区的4个核心概念
* 1.position：表示数组中可以读写的位置
* 2.limit：表示对Buffer缓冲区使用的一个限制
* 3.capacity：buffer的容量
* 4.mark 表示对当前position位置的标记

(3)使用Direct模式创建的Buffer缓冲区

(4)如何分配和读写一个Buffer缓冲区

(5)如何操作一个分配好的Buffer缓冲区


* 总结：
* Buffer缓冲区就是一个数据缓冲区，可以支持不同的数据类型。比如ByteBuffer、CharBuffer、LongBuffer、DoubleBuffer、FloatBuffer、IntBuffer，
* 这些不同的Buffer里面就可以包裹不同基本类型的数组。每个Buffer类都是Buffer接口的子实例，大多数标准IO操作都使用ByteBuffer。










