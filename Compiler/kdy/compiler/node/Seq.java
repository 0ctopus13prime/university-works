package kdy.compiler.node;


public class Seq extends Stmt {

	private Stmt f;
	private Seq s;
	
	public Seq(Stmt f, Seq s) {
		this.f = f;
		this.s = s;
	}
	
	@Override
	public void genCode() {
		f.genCode();
		if( s != null )
			s.genCode();
	}
}
