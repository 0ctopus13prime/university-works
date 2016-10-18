package kdy.compiler.lexer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TestLexicalAnalyzer implements LexicalAnalyzer {
	
	private List<Token> tokenList;
	
	public TestLexicalAnalyzer() {
		tokenList = new LinkedList<Token>();
		tokenList.add(new Token("PROGRAM", TokenType.PROGRAM));
		tokenList.add(new Token("Euclid", TokenType.ID));
		tokenList.add(new Token(";", TokenType.SEMICOLON));
		tokenList.add(new Token("VAR",TokenType.VAR));
		tokenList.add(new Token("m",TokenType.ID));
		tokenList.add(new Token(",",TokenType.COMMA));
		tokenList.add(new Token("n",TokenType.ID));
		tokenList.add(new Token(",",TokenType.COMMA));
		tokenList.add(new Token("temp",TokenType.ID));
		tokenList.add(new Token(",",TokenType.COMMA));
		tokenList.add(new Token("r",TokenType.ID));
		tokenList.add(new Token(":",TokenType.COLON));
		tokenList.add(new Token("INTEGER",TokenType.INTEGER));
		tokenList.add(new Token(";",TokenType.SEMICOLON));
		
		tokenList.add(new Token("BEGIN",TokenType.BEGIN));
		
		//실행 시작
		
		tokenList.add(new Token("WRITE", TokenType.WRITE));
		tokenList.add(new Token("(", TokenType.PAREN_LEFT));
		tokenList.add(new Token("?", TokenType.STRING));
		tokenList.add(new Token(")", TokenType.PAREN_RIGHT));
		tokenList.add(new Token(";", TokenType.SEMICOLON));
		
		tokenList.add(new Token("READ", TokenType.READ));
		tokenList.add(new Token("(", TokenType.PAREN_LEFT));
		tokenList.add(new Token("n", TokenType.ID));
		tokenList.add(new Token(")", TokenType.PAREN_RIGHT));
		tokenList.add(new Token(";", TokenType.SEMICOLON));
		
		tokenList.add(new Token("WRITE", TokenType.WRITE));
		tokenList.add(new Token("(", TokenType.PAREN_LEFT));
		tokenList.add(new Token("?", TokenType.STRING));
		tokenList.add(new Token(")", TokenType.PAREN_RIGHT));
		tokenList.add(new Token(";", TokenType.SEMICOLON));
		
		tokenList.add(new Token("READ", TokenType.READ));
		tokenList.add(new Token("(", TokenType.PAREN_LEFT));
		tokenList.add(new Token("m", TokenType.ID));
		tokenList.add(new Token(")", TokenType.PAREN_RIGHT));
		tokenList.add(new Token(";", TokenType.SEMICOLON));
		
		tokenList.add(new Token("IF",TokenType.IF));
		tokenList.add(new Token("n",TokenType.ID));
		tokenList.add(new Token("<",TokenType.LESS));
		tokenList.add(new Token("m",TokenType.ID));
		tokenList.add(new Token("THEN",TokenType.THEN));
		
		tokenList.add(new Token("temp",TokenType.ID));
		tokenList.add(new Token(":=",TokenType.ASSIGN));
		tokenList.add(new Token("n",TokenType.ID));
		tokenList.add(new Token(";",TokenType.SEMICOLON));
		
		tokenList.add(new Token("n",TokenType.ID));
		tokenList.add(new Token(":=",TokenType.ASSIGN));
		tokenList.add(new Token("m",TokenType.ID));
		tokenList.add(new Token(";",TokenType.SEMICOLON));
		
		tokenList.add(new Token("m",TokenType.ID));
		tokenList.add(new Token(":=",TokenType.ASSIGN));
		tokenList.add(new Token("temp",TokenType.ID));
		tokenList.add(new Token(";",TokenType.SEMICOLON));
		
		tokenList.add(new Token("END",TokenType.END));
		tokenList.add(new Token(";",TokenType.SEMICOLON));
		
		tokenList.add(new Token("r",TokenType.ID));
		tokenList.add(new Token(":=",TokenType.ASSIGN));
		tokenList.add(new Token("n",TokenType.ID));
		tokenList.add(new Token("-",TokenType.MINUS));
		tokenList.add(new Token("m",TokenType.ID));
		tokenList.add(new Token("*",TokenType.MULTI));
		tokenList.add(new Token("(",TokenType.PAREN_LEFT));
		tokenList.add(new Token("n",TokenType.ID));
		tokenList.add(new Token("/",TokenType.DIVIDE));
		tokenList.add(new Token("m",TokenType.ID));
		tokenList.add(new Token(")",TokenType.PAREN_RIGHT));
		tokenList.add(new Token(";",TokenType.SEMICOLON));
		
		tokenList.add(new Token("WHILE", TokenType.WHILE));
		tokenList.add(new Token("r", TokenType.ID));
		tokenList.add(new Token(">", TokenType.BIG));
		tokenList.add(new Token("0", TokenType.NUMBER));
		tokenList.add(new Token("DO", TokenType.DO));
		tokenList.add(new Token("n", TokenType.ID));
		tokenList.add(new Token(":=", TokenType.ASSIGN));
		tokenList.add(new Token("m", TokenType.ID));
		tokenList.add(new Token(";", TokenType.SEMICOLON));
		
		tokenList.add(new Token("m", TokenType.ID));
		tokenList.add(new Token(":=", TokenType.ASSIGN));
		tokenList.add(new Token("r", TokenType.ID));
		tokenList.add(new Token(";", TokenType.SEMICOLON));
		
		tokenList.add(new Token("r",TokenType.ID));
		tokenList.add(new Token(":=",TokenType.ASSIGN));
		tokenList.add(new Token("n",TokenType.ID));
		tokenList.add(new Token("-",TokenType.MINUS));
		tokenList.add(new Token("m",TokenType.ID));
		tokenList.add(new Token("*",TokenType.MULTI));
		tokenList.add(new Token("(",TokenType.PAREN_LEFT));
		tokenList.add(new Token("n",TokenType.ID));
		tokenList.add(new Token("/",TokenType.DIVIDE));
		tokenList.add(new Token("m",TokenType.ID));
		tokenList.add(new Token(")",TokenType.PAREN_RIGHT));
		tokenList.add(new Token(";",TokenType.SEMICOLON));
		
		tokenList.add(new Token("END",TokenType.END));
		tokenList.add(new Token(";",TokenType.SEMICOLON));
		
		tokenList.add(new Token("WRITE",TokenType.WRITE));
		tokenList.add(new Token("(",TokenType.PAREN_LEFT));
		tokenList.add(new Token("m",TokenType.ID));
		tokenList.add(new Token(")",TokenType.PAREN_RIGHT));
		tokenList.add(new Token(";",TokenType.SEMICOLON));
	
		//실행 끝
		
		tokenList.add(new Token("END.",TokenType.PROGRAMEND));
	}
	
	@Override
	public Token next() {
		Token token = tokenList.get(0);
		if( !tokenList.isEmpty() ) {
			tokenList.remove(0);
		}
		
		return token;
	}
	
	// show next token
	@Override
	public Token peekNext() {
		if( !tokenList.isEmpty() ) {
			return tokenList.get(0);
		}
		return null;
	}
}
