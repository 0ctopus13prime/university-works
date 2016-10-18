package kdy.compiler.node.stmt;

import kdy.compiler.node.Expr;
import kdy.compiler.node.Stmt;

public class While extends Stmt {

	private Expr expr;
	private Stmt stmt;
	
	public While(Expr expr, Stmt stmt) {
		this.expr = expr;
		this.stmt = stmt;
	}
		
	@Override
	public void genCode() {
		String loopLabel = getLabel();
		String escapeLabel = getLabel();
		
		genLabel(loopLabel);
		expr.genCode();
		formatPrint(null, "BREQ", escapeLabel);
		stmt.genCode();
		formatPrint(null, "BRANCH", loopLabel);
		genLabel(escapeLabel);
	}
}
