package kdy.compiler.lexer;

public enum TokenType {
	PROGRAM, STRING,
	// Types
	ID, VAR, INTEGER, CHAR, BOOLEAN,
	// VALUES
	NUMBER, 
	// Program blocks
	BEGIN, END, PROGRAMEND("END."),
	// Condition, Recursion
	DO, IF, THEN, ELSIF, ELSE, WHILE,
	// Datas
	WRITE, READ,
	// True or False
	NOT, TRUE, FALSE,
	// Ect symbols
	SEMICOLON(";"), COMMA(","), ASSIGN(":="), PAREN_LEFT("("), 
	PAREN_RIGHT(")"), COLON(":"), POINT("."), 
	// Arithmatic operator
	PLUS("+"), MINUS("-"), MULTI("*"), DIVIDE("/"),
	// Comparison operator
	LESS("<"), BIG(">"), EQL("="), LESSEQL("<="), BIGEQL(">="), NOTEQL("<>"),
	//Boolean Expression
	OR, AND;
	
	String value;
	
	TokenType() {
	}
	
	TokenType(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
}
