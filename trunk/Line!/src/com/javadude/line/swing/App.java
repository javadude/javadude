package com.javadude.line.swing;

import javax.swing.UIManager;

public class App {
	public static void main(String[] args) throws Throwable {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		LineFrame.openWindow();
	}
}
