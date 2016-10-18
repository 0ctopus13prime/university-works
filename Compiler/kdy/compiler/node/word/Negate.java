package kdy.compiler.node.word;

import kdy.compiler.lexer.TokenType;
import kdy.compiler.node.Expr;
import kdy.compiler.node.Word;

public class Negate extends Word {
	private Expr number;
	
	public Negate(Expr number) {
		this.number = number;
		this.type = TokenType.NUMBER;
	}

	@Override
	public void genCode() {
		number.genCode();
		formatPrint(null, "NEG", null);
	}
}
