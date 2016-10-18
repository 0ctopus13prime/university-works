package kdy.compiler.exception;

import kdy.compiler.lexer.Token;

public class DuplicateIDException extends ParseException {

	public DuplicateIDException(Token illegalToken) {
		super(illegalToken);
	}
	
	@Override
	public void printError() {
		printError(illegalToken.data + "는 이미 변수 정의가 존재합니다.");
	}
	
}
