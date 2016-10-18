package kdy.compiler.exception;

import kdy.compiler.lexer.Token;

public class IllegalReadParameterException extends ParseException {
	
	public IllegalReadParameterException(Token illegalToken) {
		super(illegalToken);
	}
	
	@Override
	public void printError() {
		printError(illegalToken.data + " 는 변수가 아닙니다. READ의 파라미터는 항상 변수여야 합니다.");
	}
}
