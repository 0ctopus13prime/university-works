package kdy.compiler.node;

import kdy.compiler.lexer.TokenType;

abstract public class Expr extends Node {
	protected TokenType type;

	public TokenType getType() {
		return type;
	}

	public void setType(TokenType type) {
		this.type = type;
	}
}
