package kdy.compiler.node.word;

import kdy.compiler.lexer.TokenType;
import kdy.compiler.node.Node;
import kdy.compiler.node.Word;

public class Number extends Word {
	private int number;
	
	public Number(int number) {
		this.number = number;
		this.type = TokenType.NUMBER;
	}

	@Override
	public void genCode() {
		formatPrint(null, "PUSHC", ""+ number);
	}
}
