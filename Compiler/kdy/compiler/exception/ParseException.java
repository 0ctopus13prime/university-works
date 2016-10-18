package kdy.compiler.exception;

import kdy.compiler.lexer.Token;

public class ParseException extends RuntimeException {
	protected final Token illegalToken;
	
	public ParseException(Token illegalToken) {
		this.illegalToken = illegalToken;
	}
	
	protected void printError(String msg) {
		System.err.format("[Line : %d, Index : %d] %s", illegalToken.lineNumber + 1,
				illegalToken.wordIdx, msg);
		System.exit(0);
	}
	
	public void printError() {	
	}
	
	public Token getIllegalToken() {
		return illegalToken;
	}
}
