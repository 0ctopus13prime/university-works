package kdy.compiler;

import java.util.HashMap;

import kdy.compiler.exception.NoSuchIDException;
import kdy.compiler.lexer.Token;

public class SymbolTable extends HashMap<String, Symbol> {
	public Symbol get(Token id) {
		Symbol symbol = super.get(id.data);
		if( symbol == null ) {
			throw new NoSuchIDException(id);
		}
		return symbol;
	}
}
