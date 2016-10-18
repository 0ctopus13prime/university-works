package kdy.compiler.exception;

import kdy.compiler.lexer.Token;

public class IllegalStatementException extends ParseException {
	
	public IllegalStatementException(Token illegalToken) {
		super(illegalToken);
	}
	
	@Override
	public void printError() {
		printError(illegalToken.data + "...은 올바르지 않는 문장 형태 입니다. 문장은 [배정문|WHILE|IF|WRITE|READ]로 시작해야 합니다.");
	}
}
