package com.javadude.line.model;

import java.io.BufferedReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class Script {
	private Line firstLine;
	private Line lastLine;
	private Line currentLine;
	private int numLines;
	private String act;
	private int page = Integer.MIN_VALUE;
	private int maxPage = Integer.MIN_VALUE;
	private int minPage = Integer.MAX_VALUE;
	private String line;
	private int lineNumber;
	private Cast cast;
	private String lastPossibleName;

	public Script(Reader reader) {
		load(reader);
		gotoFirstLine();
	}
	
	public int getMinPage() {
		return minPage;
	}

	public int getMaxPage() {
		return maxPage;
	}

	public void add(String roleName, int number, String line) {
		if ("ALL THE OTHERS".equals(roleName)) {
			roleName = "^" + lastPossibleName;
			add(cast.getRoles(roleName), cast.getAllTheOthersName(), number, line);
		} else {
			add(cast.getRoles(roleName), cast.getOriginalRoleNames(roleName), number, line);
		}
		lastPossibleName = roleName;
	}
	public void add(List<Role> roles, List<String> originalRoleNames, int number, String line) {
		Line newLine = new Line(number, act, lastLine, page, line, originalRoleNames, roles);
		if (firstLine == null) {
			firstLine = newLine;
		} else {
			lastLine.setNext(newLine);
		}
		lastLine = newLine;
		numLines++;
	}
	protected int getNumberOfLines() {
		return numLines;
	}
	protected Line getFirstLine() {
		return firstLine;
	}
	protected Line getLastLine() {
		return lastLine;
	}
	protected Line getCurrentLine() {
		return currentLine;
	}
	public void gotoFirstLine() {
		currentLine = firstLine;
	}
	public void gotoLastLine() {
		currentLine = lastLine;
	}
	public void gotoNextLine() {
		if (currentLine != null)
			currentLine = currentLine.getNext();
		else
			gotoFirstLine();
	}
	public void gotoNextLine(Role currentRole) {
		if (currentLine != null)
			currentLine = currentLine.getNext();
		if (currentLine == null)
			gotoFirstLine();
		while (currentLine != null && !currentLine.getRoles().contains(currentRole)) {
			currentLine = currentLine.getNext();
		}
		if (currentLine == null)
			gotoFirstLine();
	}
	public void gotoLine(int lineNumber) {
		gotoFirstLine();
		while (currentLine != null && currentLine.getNumber() != lineNumber && currentLine != lastLine) {
			currentLine = currentLine.getNext();
		}
		if (currentLine.getNumber() != lineNumber) {
			System.err.println("No line with line number " + lineNumber + " found in script; starting at first line");
			gotoFirstLine();
		}
	}
	public int getDistanceToNextLine(Role currentRole) {
		int n = 1;
		Line temp = currentLine.getNext();
		if (temp == null)
			temp = firstLine;
		while (temp != null && !temp.getRoles().contains(currentRole)) {
			temp = temp.getNext();
			n++;
		}
		return n;
	}
	public Line getLine(int n) {
		Line line = currentLine;
		while (n > 0 && line != null) {
			line = line.getPrevious();
			n--;
		}
		return line;
	}
	public void gotoPreviousLine() {
		if (currentLine != null)
			currentLine = currentLine.getPrevious();
		else
			gotoLastLine();
	}

	public Cast getCast() {
		return cast;
	}
	public boolean gotoAct(String name) {
		currentLine = firstLine;
		while(currentLine != null) {
			if (currentLine.getAct().equals(name))
				return true;
		}
		return false;
	}
	public boolean gotoPage(int num) {
		currentLine = firstLine;
		while (currentLine != null) {
			if (currentLine.getPage() >= num)
				return true;
			currentLine = currentLine.getNext();
		}
		return false;
	}
	private RuntimeException err(String message) {
		return new RuntimeException(message + "' on line " + lineNumber + ": " + line);
	}

	// TODO CHECK THAT ROLES HAS BEEN SPECIFIED
	private void load(final Reader reader) {
		new AutoFileCloser() {
			@Override protected void doWork() throws Throwable {
				BufferedReader br = autoClose(new BufferedReader(autoClose(reader)));
				lineNumber = 0;
				while ((line = br.readLine()) != null) {
					line = line.trim();
					lineNumber++;
					if (line.startsWith("!ROLES")) {
						line = line.substring(6).trim();
						List<Role> roles = new ArrayList<Role>();
						for (String n : line.split("[ ,]+")) {
							roles.add(new Role(n));
						}
						cast = new Cast(roles);

						continue;
					}
					if (line.startsWith("!ACT")) {
						act = line.substring(4).trim();
						continue;
					}
					if (line.startsWith("!SCENE")) {
						continue;
					}
					if (line.startsWith("-")) {
						String numString = line.substring(1).trim();
						if (!numString.endsWith("-"))
							throw err("Invalid page number specification " + numString);
						if (act == null)
							throw err("Page number found before act specification");
						numString = numString.substring(0, numString.length()-1).trim();
						try {
							page = Integer.parseInt(numString);
							maxPage = Math.max(page, maxPage);
							minPage = Math.min(page, minPage);
						} catch (NumberFormatException e) {
							throw err("Invalid page number '" + numString + "'");
						}
						continue;
					}					
					if (line.startsWith("(")) {
						if (act == null)
							throw err("Stage direction found before act specification");
						if (page == Integer.MIN_VALUE)
							throw err("Stage direction found before page specification");
						add(cast.getStageDirection(), cast.getStageDirectionName(), lineNumber, italicize());
						continue;
					}
					// if the line starts with ALL UPPER followed by . it's a character's line
					int dot = line.indexOf('.');
					if (dot != -1) {
						String possibleName = line.substring(0, dot).trim();
						for (int i = 0; i < possibleName.length(); i++) {
							if (Character.isUpperCase(possibleName.charAt(i)))
								continue;
							if (Character.isWhitespace(possibleName.charAt(i)))
								continue;
							if ('&' == possibleName.charAt(i))
								continue;
							if (',' == possibleName.charAt(i))
								continue;
							if ('_' == possibleName.charAt(i))
								continue;
							throw err("Invalid line - should start with all upper-case followed by dot followed by dialog");
						}
						// TODO SPLIT UP "&" into two separate roles
						// TODO REPLACE "ALL" with all roles
						
						// it's a line - strip off the name from the line
						if (dot == line.length()-1)
							throw err("Invalid line - should start with all upper-case followed by dot followed by dialog");
						line = line.substring(dot+1).trim();
						if (act == null)
							throw err("Dialog found before act specification");
						if (page == Integer.MIN_VALUE)
							throw err("Dialog found before page specification");
						
						// check proper _xxxx_ matching and convert to <i>xxxx</i>
						add(possibleName, lineNumber, italicize());
					}
				}
			}

			private String italicize() {
				String result = "";
				boolean italic = false;
				for (int i = 0; i < line.length(); i++) {
					char c = line.charAt(i);
					switch (c) {
						case '_':
							if (italic)
								result += "</i>";
							else
								result += "<i>";
							italic = !italic;
							break;
						case '(':
							if (italic)
								throw err("Invalid line - '(' found inside _xxxx_ for italics)");
							result += "(";
							break;
						case ')':
							if (italic)
								throw err("Invalid line - ')' found inside _xxxx_ for italics)");
							result += ")";
							break;
						default:
							result += c;
					}
				}
				if (italic)
					throw err("Invalid line - mismatched _xxx_ for italics");
				return result;
			}};
	}

}
