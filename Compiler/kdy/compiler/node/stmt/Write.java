package kdy.compiler.node.stmt;

import kdy.compiler.lexer.Token;
import kdy.compiler.lexer.TokenType;
import kdy.compiler.node.API;
import kdy.compiler.node.Expr;

public class Write extends API {
	private Expr expr;
	
	public Write(Expr expr) {
		this.expr = expr;
	}
	
	@Override
	public void genCode() {
		expr.genCode();
		switch(expr.getType() ) {
		case NUMBER :
			formatPrint(null, "WRINT", null);
			break;
		case STRING :
			formatPrint(null, "WRCHR", null);
			break;
		}
	}
}
