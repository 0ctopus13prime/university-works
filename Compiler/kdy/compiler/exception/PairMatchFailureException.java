package kdy.compiler.exception;

import kdy.compiler.lexer.Token;
import kdy.compiler.lexer.TokenType;

public class PairMatchFailureException extends MatchFailureException {
	public final TokenType closeToken; 
	
	public PairMatchFailureException(Token illegalToken, 
			TokenType expected,
			TokenType closeToken) {
		super(illegalToken, expected);
		this.closeToken = closeToken;
	}
	
	@Override
	public void printError() {
		printError("잘못된 토큰 " + illegalToken.data + "입니다. " + 
				( expected.getValue() == null ? expected : expected.getValue() ) + 
				"로 예상되며 " + closeToken + "과 매치 되어야 합니다.");
	}
}
