package kdy.compiler.node.stmt;

import kdy.compiler.lexer.Token;
import kdy.compiler.lexer.TokenType;
import kdy.compiler.node.API;

public class Read extends API {
	private TokenType type;
	private String name;
	
	public Read(String name, TokenType type) {
		this.type = type;
		this.name = name;
	}
	
	@Override
	public void genCode() {
		switch(type) {
		case INTEGER : 
		case NUMBER : 
			formatPrint(null, "RDINT", null);
			formatPrint(null, "POPC", name);
			break;
		case CHAR : 
		case STRING :
			formatPrint(null, "RDCHR", null);
			formatPrint(null, "POPC", name);
			break;
		}
	}
}
