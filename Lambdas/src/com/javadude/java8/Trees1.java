package com.javadude.java8;

public class Trees1 {
	private static class Wrapper {
		public int sum = 0;
	}
	public static void main(String[] args) {
//		PrintingNode<Integer> root = new PrintingNode<Integer>(42);
		BinaryTreeNode<Integer> root = new BinaryTreeNode<Integer>(42);
		root.add(10);
		root.add(60);
		root.add(100);
		root.add(1024);
		root.add(1);
		root.add(64);
		root.add(40);
		int[] sum = {0};
		Wrapper wrapper = new Wrapper();
		root.inorder(n -> sum[0] += n.getData());
		System.out.println(sum[0]);
	}
}
