package kdy.compiler.lexer;

public interface  LexicalAnalyzer {
	public Token next();
	
	// show next token
	public Token peekNext();
}
