package com.javadude.line.model;

import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import com.javadude.line.swing.App;

public class ProductionBook {
	public static void main(String[] args) throws Throwable {
		Script script = new Script(new InputStreamReader(App.class.getResourceAsStream("/play-on.linescript")));

		// print mapping of roles to pages
		FileWriter fw1 = new FileWriter("play-on-cast-pages.csv");
		PrintWriter pw1 = new PrintWriter(fw1);
		int minPage = script.getMinPage();
		int maxPage = script.getMaxPage();
		pw1.print("Role,");
		for (int i = minPage; i < maxPage; i++) {
			pw1.print(i + ",");
		}
		pw1.println(maxPage);
		
		List<Role> roles = new ArrayList<Role>(script.getCast().getRoles());
		Collections.sort(roles, new Comparator<Role>() {
			@Override public int compare(Role r1, Role r2) {
				return r2.getPagesThisRoleAppears().size() - r1.getPagesThisRoleAppears().size();
			}});
		for (Role role : roles) {
			pw1.print(role.getName() + ",");
			Set<Integer> pages = role.getPagesThisRoleAppears();
			for (int i = minPage; i < maxPage; i++) {
				if (pages.contains(i))
					pw1.print("X,");
				else
					pw1.print(",");
			}
			if (pages.contains(maxPage))
				pw1.println("X");
			else
				pw1.println("");
		}
		
		pw1.close();

		
		FileWriter fw = new FileWriter("play-on-production-book.csv");
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
			
			String roleNames = null;
			for (String role : line.getOriginalRoleNames()) {
				if (roleNames == null)
					roleNames = role;
				else
					roleNames += ", " + role;
			}
			String text = "";
			if ("(STAGE)".equals(roleNames)) {
				text = line.getLine();
				if (text.startsWith("("))
					text = text.substring(1, text.length());
				if (text.endsWith(")"))
					text = text.substring(0, text.length()-1);
				text = '"' + text.replaceAll("\"", "\"\"") + "\",";
			} else {
				text = ",\"" + roleNames + ": " + line.getLine().replaceAll("\"", "\"\"") + '"';
			}
			pw.println("," + page + ",,,," + text + ",,,");
			script.gotoNextLine();
		}
		pw.close();
	}
}
