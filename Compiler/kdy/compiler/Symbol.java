package kdy.compiler;

import kdy.compiler.lexer.TokenType;

class Symbol {	
	String name;
	int location;
	TokenType type;
	
	public Symbol(
			String name,
			int location, 
			TokenType type ) {
		this.name = name;
		this.type = type;
		this.location = location;
	}
	
	public Symbol() { }
}