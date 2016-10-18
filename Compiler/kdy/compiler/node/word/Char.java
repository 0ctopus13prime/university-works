package kdy.compiler.node.word;

import kdy.compiler.lexer.TokenType;
import kdy.compiler.node.Node;
import kdy.compiler.node.Word;

public class Char extends Word {
	private char character;
	
	public Char(char character) {
		this.character = character;
		this.type = TokenType.STRING;
	}

	@Override
	public void genCode() {
		formatPrint(null, "PUSHC", "'" + character);
	}
}
