package kdy.compiler.node.word;

import kdy.compiler.lexer.TokenType;
import kdy.compiler.node.Expr;
import kdy.compiler.node.Word;

public class Not extends Word {
	private Expr bool;
	
	public Not(Expr bool) {
		this.bool = bool;
		this.type = TokenType.BOOLEAN;
	}

	@Override
	public void genCode() {
		bool.genCode();
		formatPrint(null, "CMPZR", null);
	}
}
