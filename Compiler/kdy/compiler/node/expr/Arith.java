package kdy.compiler.node.expr;

import kdy.compiler.lexer.TokenType;
import kdy.compiler.node.Expr;

public class Arith extends Expr {
	protected Expr first;
	protected Expr second;
	protected TokenType arith;
	
	public Arith(TokenType arith, Expr first, Expr second) {
		//arith는 integer타입
		super.type = TokenType.NUMBER;
		this.arith = arith;
		this.first = first;
		this.second = second;
	}

	@Override
	public void genCode() {
		first.genCode();
		second.genCode();
		
		switch(arith) {
		case PLUS :
			formatPrint(null, "ADD", null);
			break;
		case MINUS :
			formatPrint(null, "SUB", null);
			break;
		case MULTI:
			formatPrint(null, "MUL", null);
			break;
		case DIVIDE :
			formatPrint(null, "DIV", null);
			break;
		}
	}
}
