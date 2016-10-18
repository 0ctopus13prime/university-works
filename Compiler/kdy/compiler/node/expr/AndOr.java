package kdy.compiler.node.expr;

import kdy.compiler.lexer.TokenType;
import kdy.compiler.node.Expr;

public class AndOr extends Expr {
	protected Expr first;
	protected Expr second;
	protected TokenType andor;
	protected static int shortKit = 0;
	
	public AndOr(TokenType andor, Expr first, Expr second) {
		super.type = TokenType.BOOLEAN;
		this.first = first;
		this.second = second;
		this.andor = andor;
	}
	
	protected String getShortKit() {
		return "S" + shortKit++;
	}
	
	@Override
	public void genCode() {
		first.genCode();
		
		String sk1 = getShortKit();
		String sk2 = getShortKit();
		
		switch( andor ) {
		case AND :
			formatPrint(null, "BREQ", sk1);
			break;
		case OR :
			formatPrint(null, "BRGT", sk1);
		}
		
		second.genCode();
		formatPrint(null, "BRANCH", sk2);
		
		switch( andor ) {
		case AND :
			//단락회로 false
			formatPrint(sk1, "PUSHC", "" + 0);
			break;
		case OR :
			//단락회로 true
			formatPrint(sk1, "PUSHC", "" + 1);
			break;
		}

		//단락회로 아닐 경우
		genLabel(sk2);
	}
}
