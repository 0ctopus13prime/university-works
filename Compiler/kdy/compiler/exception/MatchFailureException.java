package kdy.compiler.exception;

import kdy.compiler.lexer.Token;
import kdy.compiler.lexer.TokenType;

public class MatchFailureException extends ParseException {
	public final TokenType expected;
	
	public MatchFailureException(Token illegalToken, TokenType expected) {
		super(illegalToken);
		this.expected = expected;		
	}
	
	@Override
	public void printError() {
		printError(illegalToken.data + " 은 잘못된 토큰입니다. " + 
				( expected.getValue() == null ? expected : expected.getValue() ) + " 가 와야합니다.");
	}
}
