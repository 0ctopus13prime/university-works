package kdy.compiler.exception;

import kdy.compiler.lexer.Token;

public class NoSuchIDException extends ParseException {
	
	public NoSuchIDException(Token illegalId) {
		super(illegalId);
	}
	
	@Override
	public void printError() {
		printError(illegalToken.data + " 는 선언되지 않은 변수 입니다.");
	}
}
