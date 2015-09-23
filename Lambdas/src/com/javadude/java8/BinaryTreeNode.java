package com.javadude.java8;

import java.util.function.Consumer;

public class BinaryTreeNode<T extends Comparable<T>> {
	private final T data;
	private BinaryTreeNode<T> left;
	private BinaryTreeNode<T> right;
	public BinaryTreeNode(T data) {
		this.data = data;
	}
	protected BinaryTreeNode<T> create(T data) {
		return new BinaryTreeNode<T>(data);
	}
	
	public final void add(T data) {
		if (data.compareTo(this.data) < 0) {
			if (left == null)
				left = create(data);
			else
				left.add(data);
		} else {
			if (right == null)
				right = create(data);
			else
				right.add(data);
		}
	}
	public T getData() {
		return data;
	}
	public BinaryTreeNode<T> getLeft() {
		return left;
	}
	public BinaryTreeNode<T> getRight() {
		return right;
	}
	public void inorder(Consumer<BinaryTreeNode<T>> consumer) {
		if (left != null)
			left.inorder(consumer);
		
		// do something
		consumer.accept(this);
		
		if (right != null)
			right.inorder(consumer);
	}
	@Override
	public String toString() {
		return "BinaryTreeNode [data=" + data + "]";
	}
}
