package kdy.compiler.node.word;

import kdy.compiler.lexer.TokenType;
import kdy.compiler.node.Word;

public class True extends Word {

	public True() {
		type = TokenType.BOOLEAN;
	}
	
	@Override
	public void genCode() {
		formatPrint(null, "PUSHC", "1");
	}

}
