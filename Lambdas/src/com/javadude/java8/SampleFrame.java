package com.javadude.java8;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class SampleFrame extends JFrame implements ActionListener {
	private class Listener2 implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("Internal pressed");
		}
	}	
	
	private static final long serialVersionUID = 1L;
	public SampleFrame() {
		setLayout(new GridLayout(0,1,3,3));
		JButton[] buttons = new JButton[20];
		for (int i = 0; i < buttons.length; i++) {
			buttons[i] = new JButton("Button " + i);
			add(buttons[i]);
		}
		
		buttons[0].addActionListener(this);
		buttons[1].addActionListener(new Listener1());
		buttons[2].addActionListener(new Listener2());
		
		String stuff = "Hello!";
		
		buttons[3].addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				System.out.println(stuff + "Anon pressed");
			}});
		buttons[4].addActionListener(
				(ActionEvent e) -> {
					System.out.println("Lambda1 pressed");
				}
		);
		buttons[5].addActionListener(
				(e) -> {
					System.out.println("Lambda2 pressed");
				}
		);
		buttons[6].addActionListener(
				e -> {
					System.out.println("Lambda3 pressed");
				}
				);
		buttons[7].addActionListener( e -> System.out.println("Lambda4 pressed") );
		buttons[8].addActionListener( e -> System.out.println(e) );
		buttons[9].addActionListener( System.out::println );
		buttons[9].addActionListener( System.out::println );
		buttons[10].addActionListener( SampleFrame::dump );

		pack();
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
	
	public static void dump(ActionEvent e) {
		System.out.println(((JButton)e.getSource()).getText());
	}
	public static void main(String[] args) {
		new SampleFrame().setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("Pressed!");
	}
}
