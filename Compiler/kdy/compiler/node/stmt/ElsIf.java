package kdy.compiler.node.stmt;

import kdy.compiler.node.Expr;
import kdy.compiler.node.Stmt;
import kdy.compiler.node.expr.AndOr;

public class ElsIf extends IfSub {

	private Expr expr;
	private Stmt stmt;
	private IfSub ifsub;
	private String escapeLabel;
		
	public void setEscapeLabel(String label) {
		escapeLabel = label;
	}
	
	public ElsIf(Expr expr, Stmt stmt, IfSub ifsub) {
		this.expr = expr;
		this.stmt = stmt;
		this.ifsub = ifsub;
	}
	
	@Override
	public void genCode() {
		if( ifsub != null && ifsub instanceof ElsIf )
			((ElsIf)ifsub).setEscapeLabel(escapeLabel);
				
		String elseifLabel = getLabel();
		expr.genCode();
		formatPrint(null, "BREQ", elseifLabel);
		
		stmt.genCode();
		formatPrint(null, "BRANCH", escapeLabel);
		genLabel(elseifLabel);
		
		if( ifsub != null ) {
			ifsub.genCode();
		}
	}
}
