package com.javadude.annotation.processors.template;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Stack;

///*
//* Code generation notes
//* variable references take the form
//*     $<modifiers_>name<_suffix>$
//* where modifiers can be any of:
//*   parent
//*   lower
//*   upper
//*   stripPackage
//* and suffix can be any text that is ignored (suffix allows creation
//* of unique names for the java compilation below to work)
//*
//* All lines ending in //#DUMMY are stripped
//*
//* All multi-line comments have the delimiters removed and then the
//*   text is processed as normal. This allows expressions in places
//*   that aren't possible in java (like specifying the method access
//*   modifiers)
//*
//* FOREACH blocks push the elements on the stack one at a time
//*   making their child attributes available in scope.
//*
//*  //? conditions have special variables FIRST and LAST to
//*    indicate looping
//*
//*  conditions can use booleans for true/false and pointers
//*    (null = false, non-null = true)
//*/




public class TemplateReader {
	private ConditionParser conditionParser = new ConditionParser();

	public Processor readTemplate(Reader reader) {
		BufferedReader bufferedReader = null;
		try {
			try {
				Template template = new Template(-1);
				bufferedReader = new BufferedReader(reader);
				String lineText;
				int lineNumber = 0;

				Stack<CompositeProcessor> currentComposite = new Stack<CompositeProcessor>();
				currentComposite.push(template);
				while ((lineText = bufferedReader.readLine()) != null) {
					lineNumber++;
					// check for directives
					int i = lineText.indexOf("//#");
					if (i != -1) {
						// it's a directive (DUMMY, FOREACH, IF, END)
						if (lineText.regionMatches(i+3, "DUMMY", 0, 5))
							continue; // skip the line
						// check if anything before "//" is non-whitespace
						for (int n = 0; n < i; n++)
							if (!Character.isWhitespace(lineText.charAt(n)))
								throw new ExpressionException("Non-whitespace before //#FOREACH, //#IF or //#END is not allowed");
						if (lineText.regionMatches(i+3, "END", 0, 3)) {
							// can have comment text after the end
							currentComposite.pop();
							if (currentComposite.isEmpty()) {
								throw new ExpressionException("Too many //#END directives in template detected on line " + lineText);
							}
						} else if (lineText.regionMatches(i+3, "FOREACH", 0, 7)) {
							String expression = lineText.substring(i+10).trim();
							ForEach forEach = new ForEach(lineNumber, expression);
							currentComposite.peek().add(forEach);
							currentComposite.push(forEach);
						} else if (lineText.regionMatches(i+3, "IF", 0, 2)) {
							String expression = lineText.substring(i+5).trim();
							If if1 = new If(lineNumber, conditionParser.parse(expression, lineNumber));
							currentComposite.peek().add(if1);
							currentComposite.push(if1);
						}
						continue; // done with directives
					}

					// check for conditional inclusion of the line
					Condition condition = null;
					i = lineText.indexOf("//?");
					if (i != -1) {
						condition = conditionParser.parse(lineText.substring(i + 3), lineNumber);
						lineText = lineText.substring(0, i);
					}

					Line line = new Line(lineNumber, condition);
					currentComposite.peek().add(line);

					// chop of rest of line into text and variable refs
					boolean inVariable = false;
					int start = 0;
//					System.out.println("line:" + lineText);
					lineText = lineText.replace("/**", "[##").replace("**/", "##]");
					lineText = lineText.replace("/*", "").replace("*/", "");
					lineText = lineText.replace("[##", "/**").replace("##]", "**/");

					// trim trailing spaces
					i = lineText.length() - 1;
					while (i > -1 && Character.isWhitespace(lineText.charAt(i)))
						i--;

					lineText = lineText.substring(0, i + 1);
					i = 0;
					while (true) {
						if (i >= lineText.length())
							break;
						char c = lineText.charAt(i++);
						if (!inVariable) {
							if (c == '$') {
								inVariable = true;
								line.add(new Text(lineText.substring(start, i-1)));
//								System.out.println("    text:" + lineText.substring(start, i-1) + ':');
								start = i;
							}
						} else {
							if (c == '$') {
								inVariable = false;
								line.add(new VariableReference(lineText.substring(start, i-1)));
//								System.out.println("    variable ref:" + lineText.substring(start, i-1) + ':');
								start = i;
							}
						}
					}
					if (inVariable)
						throw new ExpressionException("Mismatched '$' on line " + lineNumber + " position " + (start-1));
					if (start < lineText.length()) {
						line.add(new Text(lineText.substring(start)));
//						System.out.println("    text:" + lineText.substring(start) + ':');
					}
				}
				if (currentComposite.size() != 1) {
					throw new ExpressionException("Not enough //#END directives in template detected at end of file");
				}
				return template;
			} finally {
				if (bufferedReader != null) {
					bufferedReader.close();
					reader = null;
				}
				if (reader != null) {
					reader.close();
				}
			}
		} catch (IOException e) {
			throw new ExpressionException("IO Error reading template", e);
		}
	}

	public static void main(String[] args) throws Exception {
		FileReader fileReader = new FileReader("/eclipse34/javadude-workspace/com.javadude.annotation/src/$packageName$/$className$Gen.java");
		new TemplateReader().readTemplate(fileReader);
	}
}
