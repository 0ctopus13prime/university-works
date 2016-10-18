package kdy.compiler.node.word;

import kdy.compiler.lexer.TokenType;
import kdy.compiler.node.Word;

public class False extends Word  {

	public False() {
		type = TokenType.BOOLEAN;
	}
	
	@Override
	public void genCode() {
		formatPrint(null, "PUSHC", "0");
	}

}
