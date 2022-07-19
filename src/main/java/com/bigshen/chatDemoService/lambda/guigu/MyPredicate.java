package com.bigshen.chatDemoService.lambda.guigu;

@FunctionalInterface
public interface MyPredicate<T> {

	public boolean test(T t);
	
}
