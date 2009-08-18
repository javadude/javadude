package com.javadude.doclet.wikitext;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Pattern;

import com.sun.javadoc.Doc;
import com.sun.javadoc.DocErrorReporter;
import com.sun.javadoc.ParamTag;
import com.sun.javadoc.RootDoc;
import com.sun.javadoc.SeeTag;
import com.sun.javadoc.SerialFieldTag;
import com.sun.javadoc.Tag;
import com.sun.javadoc.ThrowsTag;
import com.sun.tools.doclets.standard.Standard;

/**
 * This is a _simple_ wikitext *doclet*. So __there__.
 * Copyright (c) 2009 Scott Stanchfield. All Rights Reserved.
 * Copyright (c)2009 Scott Stanchfield. All Rights Reserved.
 *
 * I hope this -*works*...
 *
 * Stuff
 *    - this *might* *be* useful...
 *    - this might be useful again...
 *
 * More stuff
 *    # hello
 *    # there
 *      # plah
 *      # fnord
 *      # crinkle
 *
 *      More content
 *
 *    {{{
 *    int x = 42;
 *
 *    void foo() {
 *       int y = 22;
 *    }
 *    }}}
 *
 * _notes_:
 * _notes:_
 * (_notes:_)
 * *notes*:
 * (*notes*):
 * *notes:*
 * =notes:=
 * =notes=:
 * (=notes=):
 *
 *      Nested paragraph
 *    # how
 *
 * || A   || B   || C   ||
 * || xxx || yyy || zzz ||
 * || qqq || rrr || sss ||
 *
 * @author scott
 *
 */
public class WikitextDoclet {
	public static boolean start(RootDoc rootDoc) {
		new WikitextDoclet();
		return Standard.start(WikitextDoclet.wrap(rootDoc));
	}
	public static boolean validOptions(String[][] options, DocErrorReporter errorReporter) {
		return Standard.validOptions(options, errorReporter);
	}
	public static int optionLength(String option) {
		return Standard.optionLength(option);
	}
	public static interface Unwrapable {
		public Object unwrap();
	}
	private static final Method unwrapMethod;
	static {
		try {
			unwrapMethod = Unwrapable.class.getDeclaredMethod("unwrap");
			System.out.println(unwrapMethod);
		} catch (Exception e) {
			throw new ExceptionInInitializerError(e);
		}
	}
	private static final Set<Method> methodsToConvert = new HashSet<Method>();
	static {
		// add methods
		try {
			methodsToConvert.add(Doc.class.getDeclaredMethod("commentText"));
			methodsToConvert.add(Doc.class.getDeclaredMethod("getRawCommentText"));
			methodsToConvert.add(ParamTag.class.getDeclaredMethod("parameterComment"));
			methodsToConvert.add(SeeTag.class.getDeclaredMethod("label"));
			methodsToConvert.add(SerialFieldTag.class.getDeclaredMethod("description"));
			methodsToConvert.add(Tag.class.getDeclaredMethod("text"));
			methodsToConvert.add(ThrowsTag.class.getDeclaredMethod("exceptionComment"));
		} catch (Exception e) {
			throw new ExceptionInInitializerError(e);
		}
	}
	public static class WrappingInvocationHandler implements InvocationHandler {
		private Stack<Nesting> nestingStack = new Stack<Nesting>();
		private Object target;
		public WrappingInvocationHandler(Object target) {
			this.target = target;
		}
		@Override public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			// unwrap any unwrappable args (there are no methods that take one of our wrappers that need to keep them that way
			if (args != null) {
				for (int i = 0; i < args.length; i++) {
					if (args[i] != null && args[i] instanceof Unwrapable) {
						args[i] = ((Unwrapable) args[i]).unwrap();
					}
				}
			}

			if (method.equals(unwrapMethod)) {
				return target;
			}

			if (methodsToConvert.contains(method)) {
				return createHtml((String) method.invoke(target, args));
			}

			// if an array, wrap all the elements of the array if they're interface type
			if (method.getReturnType().isArray()) {
				Object result = method.invoke(target, args);
				Class<?> componentType = method.getReturnType().getComponentType();
				if (!componentType.isInterface()) {
					return result;
				}
				int length = Array.getLength(result);
				Object newArray = Array.newInstance(componentType, length);
				for (int i = 0; i < length; i++) {
					Array.set(newArray, i, wrap(Array.get(result, i)));
				}
				return newArray;
			}

			// if primitive return type or not an interface, just return it
			if (method.getReturnType().isPrimitive() || !method.getReturnType().isInterface()) {
				return method.invoke(target, args);
			}

			// TODO wrap anything else (not positive here - is there anything we should omit?)
			Object result = method.invoke(target, args);
			return wrap(result);
		}

		private String createHtml(String result) {
			// simple rules:
			// _xxxx_ -> <i>xxxx</i>  (all on same line)
			// *xxxx* -> <b>xxxx</b>  (all on same line)
			// __xxxx__ -> <b><i>xxxx</i></b>  (all on same line)
			// =xxxx= -> <code>xxxx</code>  (can contain spaces but must not have spaces next to =)
			// blank lines separate paragraphs
			// {{{
			// xxx
			// }}} -> <pre>xxx</pre>  (must be on separate lines; newlines at either end removed)
			// # at start of line -> <li> inside <ol>  (nesting allowed - multiple #)
			// - at start of line -> <li> inside <ul>  (nesting allowed - multiple -)
			// | xx | xx | xx | -> table (line starts and ends with |)

			result = emphasisStrong.matcher(result).replaceAll("<i><b>$1</b></i>");
			result = emphasis.matcher(result).replaceAll("<i>$1</i>");
			result = strong.matcher(result).replaceAll("<b>$1</b>");
			result = code.matcher(result).replaceAll("<code>$1</code>");
			result = copyright.matcher(result).replaceAll("&copy;");

			StringBuilder html = new StringBuilder();
			String[] lines = result.split("(\r\n?|\n)");

			// normalize all lines to determine where paragraphs and indented content are
			// determine the minimum number of leading spaces on non-empty lines and
			//   trim that much off each of the lines
			int minLeading = Integer.MAX_VALUE;
			for (String line : lines) {
				if ("".equals(line.trim())) {
					continue;
				}
				minLeading = Math.min(leadingSpaces(line), minLeading);
			}
			for(int i = 0; i < lines.length; i++) {
				if (lines[i].length() > minLeading) {
					lines[i] = lines[i].substring(minLeading);
				}
			}

			nestingStack.push(new Nesting(-1, NestingType.PARAGRAPHS));

			for (String line : lines) {
				String trimmedLine = line.trim();

				// check for line items (lines that start with - and #)
				boolean poppedOut = false;
				int n = leadingSpaces(line);
				NestingType nestingType = null;
				Nesting nesting = nestingStack.peek();

				if ("{{{".equals(trimmedLine)) {
					nestingType = NestingType.PREFORMATTED;
					poppedOut = pushOrPop(nesting, n, html, nestingType);
					nesting = nestingStack.peek();
					continue;

				}
				if ("}}}".equals(trimmedLine)) {
					if (n != nesting.leadingSpaces) {
						throw new RuntimeException("matching }}} must be at same indentation as {{{");
					}
					// pop nesting
					popNesting(html);
					continue;
				}
				if (nesting.nestingType == NestingType.PREFORMATTED) {
					if ("".equals(trimmedLine)) {
						line = "";
					} else if (n < nesting.leadingSpaces) {
						throw new RuntimeException("Preformatted text must be at least the same indentation as {{{ ... }}}");
					} else {
						line = line.substring(nesting.leadingSpaces);
					}
					html.append(line);
					html.append('\n');
					continue;
				}

				// TODO allow {{{ or }}} to be on same line as other stuff
				if (line.contains("{{{") || line.contains("}}}")) {
					throw new RuntimeException("{{{ or }}} must be a on a line by itself");
				}

				if (!"".equals(trimmedLine)) {
					switch(line.charAt(n)) {
						case '-':
							nestingType = NestingType.UL;
							line = line.substring(n + 1).trim();
							break;
						case '#':
							nestingType = NestingType.OL;
							line = line.substring(n + 1).trim();
							break;
						default:
							nestingType = n == 0 ? NestingType.PARAGRAPHS : NestingType.TEXT;
							if (n == 0) {
								n = -1; // special level for paragraphs
							}
							break;
					}

					poppedOut = pushOrPop(nesting, n, html, nestingType);
					nesting = nestingStack.peek();
				}

				// if this is a blank line, or an explicit new item end the previous item
				if (poppedOut || "".equals(trimmedLine) || nestingType == NestingType.UL || nestingType == NestingType.OL) {
					finishItemInProgress(html, nesting);
				}
				if (!"".equals(trimmedLine)) {
					if (!nesting.itemInProgress) {
						nesting.itemInProgress = true;
						html.append(nesting.nestingType.itemStart());
					}
					html.append(line);
					html.append('\n');
				}
			}

			while (!nestingStack.isEmpty()) {
				popNesting(html);
			}
			return html.toString();
		}

		private boolean pushOrPop(Nesting nesting, int n, StringBuilder html, NestingType nestingType) {
			boolean poppedOut = false;

			// pop off levels that are deeper than we currently are
			while (nesting.leadingSpaces > n) {
				popNesting(html);
				poppedOut = true;
				nesting = nestingStack.peek();
			}

			// if at same level as current nesting
			if (nesting.leadingSpaces == n) {
				// if it's a different type of nesting, pop off current and push on new below
				if (nesting.nestingType != nestingType) {
					popNesting(html);
					nesting = nestingStack.peek();
				}
			}

			// if want deeper nesting, push new nesting
			if (nesting.leadingSpaces < n) {
				pushNesting(html, n, nestingType);
				nesting = nestingStack.peek();
			}
			return poppedOut;
		}


		private void finishItemInProgress(StringBuilder html, Nesting nesting) {
			if (nesting.itemInProgress) {
				html.append(nesting.nestingType.itemEnd());
				nesting.itemInProgress = false;
				html.append('\n');
			}
		}
		private void pushNesting(StringBuilder html, int n, NestingType nestingType) {
			// if we have a paragraph or nested paragraph in progress, finish it before going more nested
			Nesting nesting = nestingStack.peek();
			if (nesting.nestingType == NestingType.PARAGRAPHS || nesting.nestingType == NestingType.TEXT || nesting.nestingType == NestingType.PREFORMATTED) {
				finishItemInProgress(html, nesting);
			}
			Nesting newNesting = new Nesting(n, nestingType);
			nestingStack.push(newNesting);
			html.append(newNesting.nestingType.start());
			html.append('\n');
		}
		private void popNesting(StringBuilder html) {
			Nesting oldNesting = nestingStack.pop();
			finishItemInProgress(html, oldNesting);
			html.append(oldNesting.nestingType.end());
			html.append('\n');
		}

		private int leadingSpaces(String line) {
			int n = 0;
			int len = line.length();
			while (n < len && Character.isWhitespace(line.charAt(n))) {
				n++;
			}
			if (n == len) {
				return 0;
			}
			return n;
		}
	}
	private static final Pattern copyright = Pattern.compile("(?<=\\A|\\s)(\\(c\\))(?=\\z|\\s|\\d{4})");
	private static final Pattern emphasis = Pattern.compile("\\b_(?=[^_\\s])(.*?)(?<=[^_\\s])_\\b");
	private static final Pattern emphasisStrong = Pattern.compile("\\b__(?=[^_\\s])(.*?)(?<=[^_\\s])__\\b");
	private static final Pattern strong = Pattern.compile("\\B\\*(?=[^\\*\\s])(.*?)(?<=[^\\*\\s])\\*\\B");
	private static final Pattern code = Pattern.compile("\\B=(?=[^\\*\\s])(.*?)(?<=[^\\*\\s])=\\B");

	private static enum NestingType {
		PARAGRAPHS {
			@Override public String start() { return ""; }
			@Override public String end() { return ""; }
			@Override public String itemStart() { return "<p>"; }
			@Override public String itemEnd() { return "</p>"; }
		},
		PREFORMATTED {
			@Override public String start() { return "<pre>"; }
			@Override public String end() { return "</pre>"; }
			@Override public String itemStart() { return ""; }
			@Override public String itemEnd() { return ""; }
		},
		OL {
			@Override public String start() { return "<ol>"; }
			@Override public String end() { return "</ol>"; }
			@Override public String itemStart() { return "<li>"; }
			@Override public String itemEnd() { return "</li>"; }
		},
		UL {
			@Override public String start() { return "<ul>"; }
			@Override public String end() { return "</ul>"; }
			@Override public String itemStart() { return "<li>"; }
			@Override public String itemEnd() { return "</li>"; }
		},
		TEXT {
			@Override public String start() { return "<dl>"; }
			@Override public String end() { return "</dl>"; }
			@Override public String itemStart() { return "<dd>"; }
			@Override public String itemEnd() { return "</dd>"; }
		};
		public abstract String start();
		public abstract String end();
		public abstract String itemStart();
		public abstract String itemEnd();
	};
	private static class Nesting {
		public int leadingSpaces;
		public NestingType nestingType;
		public boolean itemInProgress;
		public Nesting(int leadingSpaces, NestingType nestingType) {
			this.leadingSpaces = leadingSpaces;
			this.nestingType = nestingType;
		}
	}

	public static void main(String[] args) {
		String testData = " This is a _simple_ wikitext *doclet*. So __there__.\n" +
		" continue this paragraph\n" +
		"\n" +
		" I hope this -*works*...\n" +
		"\n" +
		" Stuff\n" +
		"    - this *might* *be* useful...\n" +
		"    - this might be useful again...\n" +
		"       - nested...\n" +
		"\n" +
		"   Uh oh... nested paragraph\n" +
		"   Another nested paragraph\n" +
		"      More nested paragraph\n" +
		"   Back out paragraph\n" +
		"\n" +
		"_notes_:\n" +
		"_notes:_\n" +
		"(_notes:_)\n" +
		"*notes*:\n" +
		"(*notes*):\n" +
		"*notes:*\n" +
		"=notes:=\n" +
		"=notes=:\n" +
		"(=notes=):\n" +
		"\n" +
		"\n" +
		" More stuff\n" +
		"    # hello\n" +
		"    # there\n" +
		"      # plah\n" +
		"      # fnord\n" +
		"      # crinkle\n" +
		"\n" +
		"     # how\n" +
		"\n" +
		" || A   || B   || C   ||\n" +
		" || xxx || yyy || zzz ||\n" +
		" || qqq || rrr || sss ||\n" +
		"";
		System.out.println(new WikitextDoclet().createHtml(testData));
	}

	// pass through for testing
	private String createHtml(String testData) {
		return new WrappingInvocationHandler(null).createHtml(testData);
	}

	public static <T> T wrap(T o) {
		if (o == null) {
			return null;
		}
		Class<?>[] interfaces = o.getClass().getInterfaces();
		Class<?>[] targetInterfaces = new Class<?>[interfaces.length + 1];
		System.arraycopy(interfaces, 0, targetInterfaces, 0, interfaces.length);
		targetInterfaces[interfaces.length] = Unwrapable.class;
		@SuppressWarnings("unchecked")
		T t = (T) Proxy.newProxyInstance(WikitextDoclet.class.getClassLoader(),
										targetInterfaces, new WrappingInvocationHandler(o));
		return t;
	}
}
