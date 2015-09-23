//package com.javadude.java8;
//
//public class PrintingNode<T extends Comparable<T>> extends BinaryTreeNode<T> {
//	public PrintingNode(T data) {
//		super(data);
//	}
//	@Override
//	protected PrintingNode<T> create(T data) {
//		return new PrintingNode<T>(data);
//	}
//	
//	@Override
//	protected void process() {
//		System.out.println(getData());
//	}
//}
