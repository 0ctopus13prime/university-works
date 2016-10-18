package kdy.compiler.node.stmt;

import kdy.compiler.node.Expr;
import kdy.compiler.node.Stmt;

public class IF extends Stmt {
	
	private IfSub ifsub;
	
	public IF(IfSub ifsub) {
		this.ifsub = ifsub;
	}
	
	@Override
	public void genCode() {
		String label = getLabel();
		
		if( ifsub instanceof ElsIf) {
			((ElsIf)ifsub).setEscapeLabel(label);
		}
		
		ifsub.genCode();
		genLabel(label);
	}

}
