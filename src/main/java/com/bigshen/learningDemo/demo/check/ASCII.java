package com.bigshen.learningDemo.demo.check;

import java.util.Scanner;

/**
 * @Description:
 * @Author: byj
 * @Date: 2019/12/6 10:33
 */
public class ASCII {
    public static void main(String[] args) {
        int size=100;
        char OriginalChar = 0;
        char EncryptedChar1[]=new char[15] ;
        char EncryptedChar2[] =new char[15];
        Scanner sc=new Scanner(System.in);
        System.out.print("Enter a message for encrypt: ");
        String arr1;
        arr1=sc.nextLine();
        System.out.print("Enter a key between 1 to 100: ");
        int Key=sc.nextInt();
        for(int i=0;i<arr1.length();i++){
            OriginalChar=arr1.charAt(i);
            if (OriginalChar+Key>126)
                EncryptedChar1[i] = (char) (((OriginalChar+Key)-127) + 32);
            else
                EncryptedChar1[i] = (char) (OriginalChar + Key);

        }
        String arr2= String.valueOf(EncryptedChar1); //将char变量转换成字符串
        System.out.println("message: "+arr1);
        System.out.println("result: "+arr2);
        for(int i=0;i<arr2.length();i++){
            OriginalChar=arr2.charAt(i);
            if(OriginalChar-Key<32){
                EncryptedChar2[i]=(char) (((OriginalChar-Key)+127) - 32);
            }else{
                EncryptedChar2[i]=(char) (OriginalChar - Key);
            }
        }
        String arr3=String.valueOf(EncryptedChar2);
        System.out.println("result: "+arr2);
        System.out.println("message: "+arr3);
    }
}
