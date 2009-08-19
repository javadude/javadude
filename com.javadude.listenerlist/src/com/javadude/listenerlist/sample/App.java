/*******************************************************************************
 * Copyright (c) 2008 Scott Stanchfield
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.javadude.listenerlist.sample;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class App {
	public static void main(String[] args) {
		final WeatherStation weatherStation = new WeatherStation();
		Student billy = new Student("Billy");
		Student willy = new Student("Willy");
		Student sally = new Student("Sally");
		Student silly = new Student("Silly");

		weatherStation.addSunListener(billy);
		weatherStation.addSunListener(willy);
		weatherStation.addSunListener(sally);
		weatherStation.addSunListener(silly);

		JFrame frame = new JFrame();
		frame.setLayout(new GridLayout(1,0,0,0));
		JButton rise = new JButton("Rise");
		JButton set = new JButton("Set");
		frame.add(rise);
		frame.add(set);
		rise.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				weatherStation.announceSunRose();
			} });
		set.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				weatherStation.announceSunSet();
			} });
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
}
