package com.javadude.annotation.processors.template;


public class ConditionParser {
	private enum TokenType {NOT, LEFT_PAREN, RIGHT_PAREN, AND, OR, VARIABLE, EOF};
	private static final String DELIMITERS = "!()&|";
	private int parsePosition;
	private int parseLine;
	private String parseExpression;
	private Token lookahead;
	private static class Token {
		private TokenType type;
		private String text;
		public Token(TokenType type, String text) {
			this.type = type;
			this.text = text;
		}
		public TokenType getType() { return type; }
		public String getText() { return text; }
	}

	// condition = term (& term)*
	//             term (| term)*
	// term = !term
	//        (condition)
	//        variableName

	public Condition parse(String expression, int line) {
//		System.out.println("parsing condition:" + expression + ':');
		parsePosition = 0;
		parseExpression = expression;
		parseLine = line;
		lookahead = null;
		return condition();
	}
	private void unexpected(String expected) {
		throw new ExpressionException("Expected " + expected + "; found '" + la().getText() + "' in expression '" + parseExpression + "' on line " + parseLine);
	}
	private Condition condition() {
		if (la().getType() == TokenType.EOF) {
			throw new ExpressionException("Missing expression on line " + parseLine);
		}
		Condition condition = term();
		loop: while (true) {
			switch(la().getType()) {
				case EOF:
				case RIGHT_PAREN:
					break loop;
				case AND:
					match(TokenType.AND, "&");
					condition = new And(condition, term());
					break;
				case OR:
					match(TokenType.OR, "|");
					condition = new Or(condition, term());
					break;
				default:
					unexpected("'&', '|' or end-of-expression");
			}
		}
		return condition;
	}
	private Condition term() {
		Condition condition = null;
		switch (la().getType()) {
			case NOT:
				match(TokenType.NOT, "!");
				condition = new Not(term());
				break;
			case LEFT_PAREN:
				match(TokenType.LEFT_PAREN, "(");
				condition = condition();
				match(TokenType.RIGHT_PAREN, ")");
				break;
			case VARIABLE:
				condition = new VariableTest(la().getText());
				match(TokenType.VARIABLE, "<variable name>");
				break;
			default:
				unexpected("'!', '(' or variable");
		}
		return condition;
	}

	private Token la() {
		if (lookahead == null)
			lookahead = nextToken();
		return lookahead;
	}
	private void match(TokenType type, String text) {
		if (la().getType() == TokenType.EOF)
			throw new ExpressionException("End-of-expression found when expecting '" + text + "' in expression '" + parseExpression + "' on line " + parseLine);
		if (la().getType() != type)
			unexpected(text);
		lookahead = null;
	}
	private Token nextToken() {
		int len = parseExpression.length();
		while (parsePosition < len && Character.isWhitespace(parseExpression.charAt(parsePosition)))
			parsePosition++;
		if (parsePosition >= len)
			return new Token(TokenType.EOF, null);
		char c = parseExpression.charAt(parsePosition);
		int i = ConditionParser.DELIMITERS.indexOf(c);
		if (i != -1) {
			parsePosition++;
			return new Token(TokenType.values()[i], "" + c);
		}
		int start = parsePosition;
		while (parsePosition < len && Character.isLetter(parseExpression.charAt(parsePosition))) {
			parsePosition++;
		}
		return new Token(TokenType.VARIABLE, parseExpression.substring(start, parsePosition));
	}
}
