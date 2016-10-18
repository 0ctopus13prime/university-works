package kdy.compiler.node.stmt;

import kdy.compiler.node.Expr;
import kdy.compiler.node.Stmt;
import kdy.compiler.node.word.ID;

public class Assign extends Stmt {

	private ID id;
	private Expr expr;
	
	public Assign(ID id, Expr expr) {
		this.id = id;
		this.expr = expr;
	}
	
	@Override
	public void genCode() {
		expr.genCode();
		formatPrint(null, "POPC", id.getName());
	}
}
