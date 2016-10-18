package kdy.compiler.node.stmt;

import kdy.compiler.node.Stmt;

public class Else extends IfSub {

	private Stmt stmt;
	
	public Else(Stmt stmt) {
		this.stmt = stmt;
	}
	
	@Override
	public void genCode() {
		stmt.genCode();
	}
}
