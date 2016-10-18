package kdy.compiler.node.word;

import kdy.compiler.lexer.TokenType;
import kdy.compiler.node.Node;
import kdy.compiler.node.Word;

public class ID extends Word {

	private int location;
	private String name;
	
	public ID(String name, int location, TokenType type) {
		this.name = name;
		this.location = location;
		this.type = type;
	}
	
	@Override
	public void genCode() {
		formatPrint(null, "PUSH", name);
	}

	public String getName() {
		return name;
	}
	
	public int getLocation() {
		return location;
	}
}
