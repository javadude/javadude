package com.javadude.line.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Properties;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;

import com.javadude.line.model.AutoFileCloser;
import com.javadude.line.model.Line;
import com.javadude.line.model.Role;
import com.javadude.line.model.Script;

@SuppressWarnings("serial")
public class LineFrame extends JFrame {
	private File scriptFile;
	private enum Mode {
		Normal("Normal"),
		Timed("Timed"),
		PromptForLine("Prompt for Line");
		private String text;
		private Mode(String text) {
			this.text = text;
		}
		public String getText() {
			return text;
		}
	};
	private JLabel[][] roleLabels = {
			{new JLabel("", JLabel.RIGHT),new JLabel("", JLabel.RIGHT),new JLabel("", JLabel.RIGHT)},
			{new JLabel("", JLabel.RIGHT),new JLabel("", JLabel.RIGHT),new JLabel("", JLabel.RIGHT)},
			{new JLabel("", JLabel.RIGHT),new JLabel("", JLabel.RIGHT),new JLabel("", JLabel.RIGHT)},
			{new JLabel("", JLabel.RIGHT),new JLabel("", JLabel.RIGHT),new JLabel("", JLabel.RIGHT)}
			};
	private JTextArea[] lineText = {new JTextArea(),new JTextArea(),new JTextArea(),new JTextArea()};
	
	private Action toStartAction = new AbstractAction("Start") {
		@Override public void actionPerformed(ActionEvent e) {
			script.gotoFirstLine();
			updateScreen();
		}};
	
	private Action toEndAction = new AbstractAction("End") {
		@Override public void actionPerformed(ActionEvent e) {
			script.gotoLastLine();
			updateScreen();
		}};
	private Action previousAction = new AbstractAction("Previous") {
		@Override public void actionPerformed(ActionEvent e) {
			script.gotoPreviousLine();
			updateScreen();
		}};

	private Action lineAction = new AbstractAction("Line?") {
		@Override public void actionPerformed(ActionEvent e) {
			showLine = true;
			updateScreen();
		}};
		
	private Action nextAction = new AbstractAction("Next") {
		@Override public void actionPerformed(ActionEvent e) {
			script.gotoNextLine();
			updateScreen();
		}};
	private Action myNextAction = new AbstractAction("My Next") {
		@Override public void actionPerformed(ActionEvent e) {
			script.gotoNextLine(currentRole);
			script.gotoPreviousLine();
			script.gotoPreviousLine();
			script.gotoPreviousLine();
			updateScreen();
		}};
	private Action gotoPageAction = new AbstractAction("Goto Page") {
		@Override public void actionPerformed(ActionEvent e) {
			while (true) {
				try {
					String value = JOptionPane.showInputDialog("Goto Page");
					if (value == null)
						return;
					int page = Integer.parseInt(value);
					script.gotoPage(page);
					updateScreen();
					return;
				} catch (NumberFormatException ex) {
				}
			}
		}};
	private Action setDelayAction = new AbstractAction("Set Delay") {
		public void actionPerformed(ActionEvent e) {
			while (true) {
				try {
					String value = JOptionPane.showInputDialog("Enter delay for timed mode, in milliseconds");
					if (value == null)
						return;
					delay = Integer.parseInt(value);
					timedMenuItem.setText(Mode.Timed.getText() + " (" + delay + "ms)");
					return;
				} catch (NumberFormatException ex) {
				}
			}
		}};
		
	private static Properties loadMasterProperties() {
		final File file = new File("line.properties");
		if (file.exists()) {
			final Properties properties = new Properties();
			new AutoFileCloser() {
				@Override protected void doWork() throws Throwable {
					Reader reader = autoClose(new FileReader(file));
					properties.load(reader);
				}};
			return properties;
		}
		return null;
	}
		
	private Action openAction = new AbstractAction("Open") {
		@Override public void actionPerformed(ActionEvent e) {
			// load new script
			File f = chooseFile(scriptFile);
			if (f == null) {
				return; // user canceled
			}

			// close current window (saving prefs)
			saveState();
			setVisible(false);
			
			openWindow(f);
		}};

	public static void openWindow() {
		// open prefs to find out what last file was
		Properties masterProperties = loadMasterProperties();
		if (masterProperties != null && masterProperties.getProperty("last.script") != null) {
			// try to load the last script
			String lastScript = masterProperties.getProperty("last.script");
			File f = new File(lastScript);
			if (f.exists()) {
				openWindow(f);
				return;
			}
		}
		
		// otherwise, choose script with file chooser
		File f = chooseFile(null);
		if (f != null)
			openWindow(f);
		return;
	}

	public static File chooseFile(File lastFile) {
		File currentDir = null;
		if (lastFile != null)
			currentDir = lastFile.getParentFile();
		else
			currentDir = new File(System.getProperty("user.dir"));
		JFileChooser fc = new JFileChooser(currentDir);
		fc.addChoosableFileFilter(new FileFilter() {
			@Override public String getDescription() {
				return "*.script";
			}
			
			@Override public boolean accept(File f) {
				return f.isDirectory() || f.getName().endsWith(".linescript");
			}
		});
		fc.setMultiSelectionEnabled(false);
		fc.setDialogTitle("Select script to open");
		int returnVal = fc.showOpenDialog(null);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            return fc.getSelectedFile();
        } else {
            return null;
        }
	}
	public static void openWindow(File scriptFile) {
		try {
			Script script = new Script(new FileReader(scriptFile));
			LineFrame frame = new LineFrame(scriptFile, script);
			frame.setVisible(true);
		} catch (RuntimeException e) {
			throw e;
		} catch (Error e) {
			throw e;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}
	private JButton toStartButton = new JButton(toStartAction);
	private JButton previousButton = new JButton(previousAction);
	private JButton lineButton = new JButton(lineAction);
	private JButton nextButton = new JButton(nextAction);
	private JButton myNextButton = new JButton(myNextAction);
	private JButton toEndButton = new JButton(toEndAction);
	private JLabel status = new JLabel();
	private JLabel pageLabel = new JLabel("Page");
	private Color origTextColor;
	private Color origLabelColor;
	private Role currentRole;
	private Script script;
	private JScrollPane scroll;
	private Mode currentMode = Mode.Normal;
	private boolean showLine = false;
	private TimingLineExposer timingLineExposer;
	private int delay = 500;
	private JRadioButtonMenuItem timedMenuItem;
	
	private class TimingLineExposer extends Thread {
		private String[] text;
		private int wordNum;
		public TimingLineExposer(String text) {
			wordNum = 0;
			this.text = text.split("\\s+");
		}
		@Override public void run() {
			while (true) {
				try {
					Thread.sleep(delay);
				} catch (InterruptedException e) {
					interrupt();
				}
				synchronized (this) {
					if (isInterrupted() || wordNum == text.length)
						break;
					wordNum++;
					String line = "";
					for (int i = 0; i < wordNum; i++) {
						line+= text[i] + " ";
					}
					final String finalLine = line;
					SwingUtilities.invokeLater(new Runnable() {
						@Override public void run() {
							lineText[3].setText(finalLine);
						}});
				}
			}
		}
	};
	
	private GridBagConstraints gbc(int x, int y, int w, int h, int anchor, int fill, int i, double wx, double wy) {
		return new GridBagConstraints(x, y, w, h, wx, wy, anchor, fill, new Insets(i,i,i,i), 0, 0);
	}
	private void saveState() {
		new AutoFileCloser() {
			@Override protected void doWork() throws Throwable {
				Properties properties = new Properties();
				properties.setProperty("delay", delay + "");
				properties.setProperty("role", currentRole.getName());
				properties.setProperty("line", script.getLine(0).getNumber() + "");
				properties.setProperty("mode", currentMode.name());
				Writer writer = autoClose(new FileWriter(scriptFile.getAbsolutePath() + ".properties"));
				properties.store(writer, "Line?!?!?! Properties for " + scriptFile.getAbsolutePath());
				properties = new Properties();
				properties.setProperty("last.script", scriptFile.getAbsolutePath());
				writer = autoClose(new FileWriter("line.properties"));
				properties.store(writer, "Line?!?!?! Properties");
			}};
	}
	private void loadState() {
		final File file = new File(scriptFile.getAbsolutePath() + ".properties");
		if (file.exists()) {
			new AutoFileCloser() {
				@Override protected void doWork() throws Throwable {
					final Properties properties = new Properties();
					Reader reader = autoClose(new FileReader(file));
					properties.load(reader);
					currentRole = script.getCast().getRoles(properties.getProperty("role")).get(0);
					currentMode = Mode.valueOf(properties.getProperty("mode"));
					String delayString = properties.getProperty("delay");
					delay = Integer.parseInt(delayString);
					String line = properties.getProperty("line");
					script.gotoLine(Integer.parseInt(line));
				}};
		}
	}
	public LineFrame(File scriptFile, final Script script) {
		this.scriptFile = scriptFile;
		this.script = script;
		this.currentRole = script.getCast().getRoles().get(0);
		loadState();

		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenu gotoMenu = new JMenu("Go To");
		JMenu characterMenu = new JMenu("Character");
		JMenu modeMenu = new JMenu("Mode");
		ButtonGroup characterGroup = new ButtonGroup();
		for(Role role : script.getCast().getRoles()) {
			final Role itemRole = role;
			JRadioButtonMenuItem item = new JRadioButtonMenuItem(role.getName() + " (" + role.getLines().size() + ")");
			item.addActionListener(new ActionListener() {
				@Override public void actionPerformed(ActionEvent e) {
					currentRole = itemRole;
					updateScreen();
				}});
			characterGroup.add(item);
			characterMenu.add(item);
			if (role == currentRole) {
				characterGroup.setSelected(item.getModel(), true);
			}
		}
		ButtonGroup modeGroup = new ButtonGroup();
		for(Mode mode : Mode.values()) {
			final Mode itemMode = mode;
			String name = mode.getText();
			if (mode == Mode.Timed)
				name += " (" + delay + "ms)";
			JRadioButtonMenuItem item = new JRadioButtonMenuItem(name);
			if (mode == Mode.Timed)
				timedMenuItem = item;
			item.addActionListener(new ActionListener() {
				@Override public void actionPerformed(ActionEvent e) {
					currentMode = itemMode;
					updateScreen();
				}});
			modeGroup.add(item);
			modeMenu.add(item);
			if (mode == currentMode) {
				modeGroup.setSelected(item.getModel(), true);
			}
		}
		gotoMenu.add(gotoPageAction);
		gotoMenu.add(new JSeparator());
		gotoMenu.add(toStartAction);
		gotoMenu.add(previousAction);
		gotoMenu.add(nextAction);
		gotoMenu.add(myNextAction);
		gotoMenu.add(toEndAction);
		
		JMenuItem exit = new JMenuItem("Exit");
		exit.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				saveState();
				System.exit(0);
			}});
		modeMenu.add(new JSeparator());
		modeMenu.add(new JMenuItem(setDelayAction));
		menuBar.add(fileMenu);
		menuBar.add(gotoMenu);
		menuBar.add(characterMenu);
		menuBar.add(modeMenu);
		fileMenu.add(openAction);
		fileMenu.add(exit);
		setJMenuBar(menuBar);
		
		setLayout(new GridBagLayout());
		
		JPanel buttons = new JPanel(new GridLayout(1,0,5,5));
		buttons.add(toStartButton);
		buttons.add(previousButton);
		buttons.add(lineButton);
		buttons.add(nextButton);
		buttons.add(myNextButton);
		buttons.add(toEndButton);
		
		scroll = new JScrollPane(lineText[3]);
		
		//  component               x   y  w  h  anchor						fill							i  wx wy
		add(roleLabels[0][0],	gbc(0,  0, 1, 1, GridBagConstraints.CENTER,	GridBagConstraints.HORIZONTAL,	2, 0, 0));
		add(roleLabels[0][1],	gbc(0,  1, 1, 1, GridBagConstraints.CENTER,	GridBagConstraints.HORIZONTAL,	2, 0, 0));
		add(roleLabels[0][2],	gbc(0,  2, 1, 1, GridBagConstraints.CENTER,	GridBagConstraints.HORIZONTAL,	2, 0, 0));
		add(lineText[0],		gbc(1,  0, 1, 3, GridBagConstraints.CENTER,	GridBagConstraints.BOTH,		5, 1, 0));
		add(roleLabels[1][0],	gbc(0,  3, 1, 1, GridBagConstraints.CENTER,	GridBagConstraints.HORIZONTAL,	2, 0, 0));
		add(roleLabels[1][1],	gbc(0,  4, 1, 1, GridBagConstraints.CENTER,	GridBagConstraints.HORIZONTAL,	2, 0, 0));
		add(roleLabels[1][2],	gbc(0,  5, 1, 1, GridBagConstraints.CENTER,	GridBagConstraints.HORIZONTAL,	2, 0, 0));
		add(lineText[1],		gbc(1,  3, 1, 3, GridBagConstraints.CENTER,	GridBagConstraints.BOTH,		5, 1, 0));
		add(roleLabels[2][0],	gbc(0,  6, 1, 1, GridBagConstraints.CENTER,	GridBagConstraints.HORIZONTAL,	2, 0, 0));
		add(roleLabels[2][1],	gbc(0,  7, 1, 1, GridBagConstraints.CENTER,	GridBagConstraints.HORIZONTAL,	2, 0, 0));
		add(roleLabels[2][2],	gbc(0,  8, 1, 1, GridBagConstraints.CENTER,	GridBagConstraints.HORIZONTAL,	2, 0, 0));
		add(lineText[2],		gbc(1,  6, 1, 3, GridBagConstraints.CENTER,	GridBagConstraints.BOTH,		5, 1, 0));
		add(roleLabels[3][0],	gbc(0,  9, 1, 1, GridBagConstraints.CENTER,	GridBagConstraints.HORIZONTAL,	2, 0, 0));
		add(roleLabels[3][1],	gbc(0, 10, 1, 1, GridBagConstraints.CENTER,	GridBagConstraints.HORIZONTAL,	2, 0, 0));
		add(roleLabels[3][2],	gbc(0, 11, 1, 1, GridBagConstraints.CENTER,	GridBagConstraints.HORIZONTAL,	2, 0, 0));
		add(pageLabel,			gbc(0, 12, 1, 1, GridBagConstraints.SOUTH,	GridBagConstraints.HORIZONTAL,  2, 0, 1));
		add(scroll,				gbc(1,  9, 1, 4, GridBagConstraints.CENTER,	GridBagConstraints.BOTH,		5, 1, 1));
		add(buttons,			gbc(0, 13, 2, 1, GridBagConstraints.CENTER,	GridBagConstraints.NONE,		5, 1, 0));
		add(status,				gbc(0, 14, 3, 1, GridBagConstraints.CENTER,	GridBagConstraints.HORIZONTAL,	5, 1, 0));
		
		addWindowListener(new WindowAdapter() {
			@Override public void windowClosing(WindowEvent e) {
				saveState();
				System.exit(0);
			}});

		pageLabel.setHorizontalAlignment(JLabel.RIGHT);
		Font oldFont = roleLabels[3][0].getFont();
		Font font = new Font(oldFont.getName(), Font.BOLD, oldFont.getSize()*2);
		for(int i = 0; i < 3; i++) {
			roleLabels[3][i].setFont(font);
			roleLabels[3][i].addNotify();
			roleLabels[3][i].setText("ALL THE OTHERS");
			Dimension preferredSize = roleLabels[3][i].getPreferredSize();
			roleLabels[3][i].setPreferredSize(preferredSize);
			roleLabels[3][i].setMaximumSize(preferredSize);
			roleLabels[3][i].setMinimumSize(preferredSize);
		}
		oldFont = lineText[3].getFont();
		font = new Font(oldFont.getName(), Font.BOLD, oldFont.getSize()*2);
		lineText[3].setFont(font);
		lineText[3].setLineWrap(true);
		lineText[3].setWrapStyleWord(true);

		origLabelColor = roleLabels[0][0].getForeground();
		origTextColor = lineText[0].getForeground();
		
		setSize(800, 600);
		updateScreen();
	}
	public void updateScreen() {
		lineButton.setEnabled(currentMode != Mode.Normal);
		Line line = null;
		for (int n = lineText.length-1; n >=0; n--) {
			line = script.getLine(n);
			int i = lineText.length-n-1;
			for(int j = 0; j < 3; j++)
				roleLabels[i][j].setText(" ");
			if (line == null) {
				lineText[i].setText(" ");
				continue;
			}
			String text = line.getLine();
			if (n == 0 && line.getRoles().contains(currentRole)) {
				if (currentMode == Mode.PromptForLine) {
					if (showLine) {
						showLine = false;
					} else {
						text = "<< YOUR LINE >>";
					}
				} else if (currentMode == Mode.Timed) {
					if (showLine) {
						showLine = false;
						if (timingLineExposer != null)
							timingLineExposer.interrupt();
					} else {
						text = "<< YOUR LINE >>";
						timingLineExposer = new TimingLineExposer(line.getLine());
						timingLineExposer.start();
					}
				}
			} else {
				if (timingLineExposer != null)
					timingLineExposer.interrupt();
			}
			lineText[i].setText(text);
			lineText[i].setCaretPosition(0);
			for (int j = 0; j < line.getOriginalRoleNames().size() && j < 3; j++) {
				roleLabels[i][j].setText(line.getOriginalRoleNames().get(j));
			}
			if (line.getRoles().contains(currentRole)) {
				lineText[i].setForeground(Color.blue);
				for(int j = 0; j < 3; j++)
					roleLabels[i][j].setForeground(Color.blue);
			} else {
				lineText[i].setForeground(origTextColor);
				for(int j = 0; j < 3; j++)
					roleLabels[i][j].setForeground(origLabelColor);
			}
		}
		if (line != null) {
			status.setText(currentRole.getName() + "    (Next Line: " + script.getDistanceToNextLine(currentRole) + ")");
			pageLabel.setText("Page " + line.getPage());
		}
	}
}
