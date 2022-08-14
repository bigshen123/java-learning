package com.bigshen.learningDemo.lambda.guigu;

@FunctionalInterface
public interface MyPredicate<T> {

	public boolean test(T t);
	
}
