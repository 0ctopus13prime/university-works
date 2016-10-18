package kdy.compiler.node.expr;

import kdy.compiler.lexer.TokenType;
import kdy.compiler.node.Expr;

public class Rel extends Expr {
	private Expr first;
	private Expr second;
	private TokenType rel;
	
	public Rel(TokenType rel, Expr first, Expr second) {
		super.type = TokenType.BOOLEAN;
		this.first = first;
		this.second = second;
		this.rel = rel;
	}
	
	@Override
	public void genCode() {
		
		/*
		 * 4 > 5라면
		 * 스택에 
		 * -
		 * 4
		 * 5
		 * -
		 * 로 들어가 있어야 >를 쓸 수 있음
		 * 그래서 역순으로 생성 
		 */
		second.genCode();
		first.genCode();
		
		switch(rel) {
			case BIG :
				formatPrint(null, "CMPGT", null);
				break;
			case LESS :
				formatPrint(null, "CMPLT", null);
				break;
			case EQL :
				formatPrint(null, "CMPEQ", null);
				break;
			case NOTEQL :
				formatPrint(null, "CMPNE", null);
				break;
			case BIGEQL :
				formatPrint(null, "CMPGE", null);
				break;
			case LESSEQL :
				formatPrint(null, "CMPLE", null);
				break;
		}
	}
}
