package com.javadude.java8;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Listener1 implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("External pressed");
	}

}
