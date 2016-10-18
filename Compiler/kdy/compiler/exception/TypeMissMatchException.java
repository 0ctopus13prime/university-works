package kdy.compiler.exception;

import kdy.compiler.lexer.Token;
import kdy.compiler.lexer.TokenType;

public class TypeMissMatchException extends ParseException {
	private TokenType was1;
	private TokenType was2;
	private TokenType expected;
	
	public TypeMissMatchException(Token illegalToken, 
			TokenType was1, 
			TokenType was2, 
			TokenType expected) {
		super(illegalToken);
		this.was1 = was1;
		this.was2 = was2;
		this.expected = expected;
	}
	
	
	@Override
	public void printError() {
		printError(String.format("타입이 일치하지 않습니다! [%s, %s]은 양립할 수 없습니다. %s로 같아야 합니다.",
					 was1, was2, expected));
	}
}
