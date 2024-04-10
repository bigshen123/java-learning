package com.bigshen.learningDemo.JUC.thirdCode.ArtConcurrentBook.chapter03;

public class FinalReferenceExample {
    final int[]                  intArray; //final����������
    static FinalReferenceExample obj;

    public FinalReferenceExample() { //���캯��
        intArray = new int[1]; //1
        intArray[0] = 1; //2
    }

    public static void writerOne() { //д�߳�Aִ��
        obj = new FinalReferenceExample(); //3
    }

    public static void writerTwo() { //д�߳�Bִ��
        obj.intArray[0] = 2; //4
    }

    public static void reader() { //���߳�Cִ��
        if (obj != null) { //5
            int temp1 = obj.intArray[0]; //6
        }
    }
}
