package a.b.c;

import java.io.StringReader;

import com.javadude.antxr.RecognitionException;
import com.javadude.antxr.TokenStreamException;

public class App {
	public static void main(String[] args) throws Throwable {
		go("3 + 4 * 2 + 5");
		go("3 * 4 - 2 * 5");
		go("3 * 4 * 2 * 5");
		go("3 * 4 + 2 + 5");
	}

	private static void go(String expr) throws RecognitionException,
			TokenStreamException {
		CalcLexer lexer = new CalcLexer(new StringReader(expr));
		CalcParser parser = new CalcParser(lexer);
		System.out.println(parser.expression());
	}
}
