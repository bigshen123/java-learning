package com.bigshen.learningDemo.mock;

import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * @author byj
 * @date 2022/11/22
 */
@ExtendWith(MockitoExtension.class)
public class MockDemo1Test {

    /**
     * 通过mock()或者@Mcok注解标注的对象，可以理解为“假对象”。
     */
    @Test
    public void mockList1() {
        List mockedList  = mock(List.class);

        //调用get(0)时，返回first
        when(mockedList.get(0)).thenReturn("first");
        //调用get(1)时，直接抛出异常
        when(mockedList.get(1)).thenThrow(new RuntimeException());

        //返回first
        System.out.println(mockedList.get(0));
        //抛出异常
        try {
            System.out.println(mockedList.get(1));
        } catch (Exception e) {
            System.out.println("抛出异常");
        }

        //没有存根，则会返回null
        System.out.println(mockedList.get(999));
    }
    @Test
    public void mockList2() {
        List mockedList  = mock(List.class);
        when(mockedList.get(0)).thenReturn(0).thenReturn(1).thenReturn(2);

        System.out.println(mockedList.get(0));
        System.out.println(mockedList.get(0));
        System.out.println(mockedList.get(0));
    }

    @Test
    public void mockVerityTest(){
        List<String> mockedList = mock(List.class);
        //using mock
        mockedList.add("once");

        mockedList.add("twice");
        mockedList.add("twice");

        mockedList.add("three times");
        mockedList.add("three times");
        mockedList.add("three times");

        //following two verifications work exactly the same - times(1) is used by default
        verify(mockedList).add("once");
        verify(mockedList, times(1)).add("once");

        //exact number of invocations verification
        verify(mockedList, times(2)).add("twice");
        verify(mockedList, times(3)).add("three times");

        //verification using never(). never() is an alias to times(0)
        verify(mockedList, never()).add("never happened");

        //verification using atLeast()/atMost()
        verify(mockedList, atMostOnce()).add("once");
        verify(mockedList, atLeastOnce()).add("three times");
        verify(mockedList, atLeast(2)).add("three times");
        verify(mockedList, atMost(5)).add("three times");
    }

    @Test
    public void mockListAnswer(){
        List mockedList = mock(List.class);
        when(mockedList.get(anyInt())).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) {
                System.out.println("哈哈哈，被我逮到了吧");
                Object[] arguments = invocationOnMock.getArguments();
                System.out.println("参数为:" + Arrays.toString(arguments));
                Method method = invocationOnMock.getMethod();
                System.out.println("方法名为:" + method.getName());

                return "结果由我决定";
            }
        });

        when(mockedList.get(0)).thenReturn("first");

        //返回first
        System.out.println(mockedList.get(0));

        //验证是否调用过get函数。这里的anyInt()就是一个参数匹配器。
        verify(mockedList).get(anyInt());

    }

    /**
     * Spy是针对于“真实存在”的对象。 在重构已有的旧代码时，Spy会比较好用。
     */
    @Test
    public void spyList(){
        //申请了一个真实的对象
        List<String> list = new LinkedList<>();
        List<String> spy = spy(list);

        //可以选择存根某些函数
        when(spy.size()).thenReturn(100);

        //调用真实的方法
        spy.add("one");
        spy.add("two");

        //打印第一个元素
        System.out.println(spy.get(0));
        System.out.println(spy.get(1));

        //获取list的大小
        System.out.println(spy.size());

        //验证
        verify(spy).add("one");
        verify(spy).add("two");

    }
}
