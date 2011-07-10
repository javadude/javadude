package com.javadude.line.model;

import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import com.javadude.line.swing.App;

public class ProductionBook {
	public static void main(String[] args) throws Throwable {
		Script script = new Script(new InputStreamReader(App.class.getResourceAsStream("/rumors.txt")));
		FileWriter fw = new FileWriter("rumors-production-book.csv");
		PrintWriter pw = new PrintWriter(fw);
		pw.println("Label,Pg,Notes,Set,Backstage,Blocking,Line,Light,Sound,Backdrop");
		script.gotoFirstLine();
		int lastPage = -1;
		while (true) {
			Line line = script.getLine(0);
			if (line == null)
				break;
			String page = "";
			if (line.getPage() > lastPage) {
				page = line.getPage() + "";
				lastPage = line.getPage();
			}
			
			String roles = null;
			for (String role : line.getOriginalRoleNames()) {
				if (roles == null)
					roles = role;
				else
					roles += ", " + role;
			}
			String text = "";
			if ("(STAGE)".equals(roles)) {
				text = line.getLine();
				if (text.startsWith("("))
					text = text.substring(1, text.length());
				if (text.endsWith(")"))
					text = text.substring(0, text.length()-1);
				text = '"' + text.replaceAll("\"", "\"\"") + "\",";
			} else {
				text = ",\"" + roles + ": " + line.getLine().replaceAll("\"", "\"\"") + '"';
			}
			pw.println("," + page + ",,,," + text + ",,,");
			script.gotoNextLine();
		}
		pw.close();
	}
}
