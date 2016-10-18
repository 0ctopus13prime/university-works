package kdy.compiler.exception;

import kdy.compiler.lexer.Token;
import kdy.compiler.lexer.TokenType;

public class WrongTypeException extends ParseException {
	public WrongTypeException(Token illegalToken) {
		super(illegalToken);
	}
	
	@Override
	public void printError() {
		printError(String.format("%s는 잘못된 타입입니다. 타입은 [ %s | %s | %s ]을 가집니다.",
				illegalToken.data,
				TokenType.CHAR,
				TokenType.INTEGER,
				TokenType.BOOLEAN));
	}
}
