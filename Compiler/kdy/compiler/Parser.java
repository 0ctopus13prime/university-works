package kdy.compiler;

import static kdy.compiler.lexer.TokenType.*;

import java.util.ArrayList;
import java.util.List;

import kdy.compiler.exception.*;
import kdy.compiler.lexer.LexicalAnalyzer;
import kdy.compiler.lexer.Token;
import kdy.compiler.lexer.TokenType;
import kdy.compiler.node.*;
import kdy.compiler.node.expr.*;
import kdy.compiler.node.stmt.*;
import kdy.compiler.node.word.*;
import kdy.compiler.node.word.Number;

public class Parser {
	private LexicalAnalyzer lexer;
	
	//변수 위치
	private int symbolLoc;
				
	private List<Symbol> undefinedTypeSymbolList;
	private SymbolTable symbolTable;
	
	public Parser(LexicalAnalyzer lexer) {
		this.lexer = lexer; 
		symbolTable = new SymbolTable();
		symbolLoc = 0;
		undefinedTypeSymbolList = new ArrayList<Symbol>();
	}
	
	public void parse() {
		try {
			match(PROGRAM);
			Token programName = match(ID);
			match(SEMICOLON);
			
			emit(null, "BRANCH", programName.data);
			
			//변수 선언 처리
			vars();
			
			//프로그램 이름 심볼 테이블에 삽입
			symbolTable.put(programName.data, 
					new Symbol(programName.data, symbolLoc++, STRING));
			
			emitLabel(programName.data);
			
			//프로그램 실행 코드 시작
			match(BEGIN);
			
			Stmt seq = stmts();
			
			//비어 있지 않다면 실행
			if( seq != null )
				seq.genCode();
			
			match(PROGRAMEND);
			emit(null,"HALT",null);
		} catch(ParseException e) {
			e.printError();
		}
	}
	
	public Seq stmts() {
		Token peek = lexer.peekNext();
		Token curr = null;		
		switch(peek.tokenType) {
			case ID :
				curr = lexer.next();
				Symbol symbol = symbolTable.get(curr);
				ID id = new ID(symbol.name, symbol.location, symbol.type);
				match(ASSIGN);
				Expr ex = expr();
				Assign assign = new Assign(id, ex);
				match(SEMICOLON, ID);
				return new Seq(assign, stmts());
			
			case WHILE : 
				lexer.next();
				Expr ex1 = expr();
				match(DO, WHILE);
				Stmt st = stmts();
				match(END, WHILE);
				match(SEMICOLON, WHILE);
				While w = new While(ex1, st);
				return new Seq(w, stmts());
				
			case IF :
				lexer.next();
				IfSub is = ifsub();	
				IF iif = new IF(is);
				match(END, IF);
				match(SEMICOLON, IF);
				return new Seq(iif, stmts());
				
			case WRITE :
				lexer.next();
				match(PAREN_LEFT, WRITE);
				//write에 들어가는 표현식
				Write wr = new Write(expr() );
				match(PAREN_RIGHT, WRITE);
				match(SEMICOLON, WRITE);
				return new Seq(wr, stmts());
				
			case READ :
				lexer.next();
				match(PAREN_LEFT, READ);
				//read로 읽혀질 변수를 읽는다.
				Token readId = lexer.next();
				if( readId.tokenType != ID ) {
					//read의 파라미터로 무조건 ID가 와야함.
					throw new IllegalReadParameterException(readId);
				}
				Symbol readSymbol = symbolTable.get(readId);
				//그리고 그 변수의 타입과 위치를 넘긴다.
				Read read = new Read(readSymbol.name, readSymbol.type);
				match(PAREN_RIGHT, READ);
				match(SEMICOLON, READ);
				return new Seq(read, stmts());
			default :
				return null;
		}
	}
	
	public Expr expr() {
		Expr f = relexpr();
		
		Token peek = lexer.peekNext();

		switch(peek.tokenType) {
		case OR :
		case AND :
			Token curr = lexer.next();
			Expr s = expr();
			
			if( f.getType() != BOOLEAN || s.getType() != BOOLEAN ) {
				throw new TypeMissMatchException(curr, f.getType(), s.getType(), BOOLEAN);
			}
			
			return new AndOr(peek.tokenType , f, s);
		default : 
			return f;
		}
	}
	
	public Expr relexpr() {
		Expr f = intexpr();
		Token peek = lexer.peekNext();
		
		switch(peek.tokenType) {
			case BIG :
			case LESS :
			case EQL : 
			case NOTEQL : 
			case BIGEQL :
			case LESSEQL :
				Token curr = lexer.next();
				Expr s = intexpr();
				
				if( f.getType() != NUMBER || s.getType() != NUMBER ) {
					throw new TypeMissMatchException(curr, 
							f.getType(), s.getType(), NUMBER);
				}
				
				return new Rel(peek.tokenType, f, s);
			default : 
				return f;
		}
	}
	
	public Expr intexpr() {
		Expr f = term();
		Expr left = f;
		
		Token peek = null;
		
		while( true ) {
			peek = lexer.peekNext();
			if( peek.tokenType != PLUS && 
				peek.tokenType != MINUS ) {
				break;
			}
			Token curr = lexer.next();
			Expr right = term();
			if( left.getType() != NUMBER || right.getType() != NUMBER ) {
				throw new TypeMissMatchException(curr, 
						left.getType(), right.getType(), NUMBER);
			}
			
			left = new Arith(peek.tokenType, left, right);
		}
		
		return left;
	}
	
	public Expr term() {
		Expr f = factor();
		
		Token peek = lexer.peekNext();
		
		switch(peek.tokenType) {
		case MULTI : 
		case DIVIDE :
			Token curr = lexer.next();
			Expr s = term();
			
			if( f.getType() != NUMBER || s.getType() != NUMBER ) {
				throw new TypeMissMatchException(curr, 
						f.getType(), s.getType(), NUMBER);
			}
			
			return new Arith(peek.tokenType, f, s);
		default : 
			return f;
		}
	}
	
	public Expr factor() {
		Token peek = lexer.peekNext();
		Token curr = null;
		switch(peek.tokenType) {
			case ID :
				curr = lexer.next();
				Symbol symbol = symbolTable.get(curr.data);
				return new ID(symbol.name, symbol.location, ( symbol.type == CHAR ? STRING : ( symbol.type == BOOLEAN ? BOOLEAN : NUMBER )));
			case NUMBER :
				curr = lexer.next();
				return new Number(Integer.parseInt(curr.data) );
			case STRING :
				curr = lexer.next();
				return new Char(curr.data.charAt(0));
			case MINUS :
				lexer.next();
				return new Negate(factor() );
			case NOT :
				lexer.next();
				return new Not(factor() );
			case PAREN_LEFT :
				match(TokenType.PAREN_LEFT);
				Expr ex = expr();
				match(PAREN_RIGHT, PAREN_LEFT);
				return ex;
			case TRUE :
				lexer.next();
				return new True();
			case FALSE :
				lexer.next();
				return new False();
			default : 
				return null;
		}
	}
	
	public ElsIf ifsub() {
		Expr ex = expr();
		match(THEN, IF);
		
		return new ElsIf(ex, stmts(), elseif());
	}
	
	public IfSub elseif() {
		Token peek = lexer.peekNext();
		
		switch(peek.tokenType) {
		case ELSIF :
			lexer.next();
			return ifsub();
		case ELSE : 
			lexer.next();
			return new Else(stmts());
		default : 
			return null;
		}
	}
	
	private void emit(String label, String opcode, String operand) {
		System.out.format("%-8s %-8s %-8s\n", label == null ? "" : label +":"
				, opcode == null ? "" : opcode,
				operand == null ? "" : operand);
	}
	
	private void emitVar(String var, String value) {
		System.out.format("%-8s %8s %-8s\n", var, "", value);
	}
	
	private void emitLabel(String label) {
		emit(label, null, null);
	}
		
	private void vars() {
		if( lexer.peekNext().tokenType != VAR ) {
			//변수 선언 없음
			return;
		}
		
		match(VAR);
		varList();
	}
	
	private void varList() {
		//ID가 아니면 리턴시킴
		if( lexer.peekNext().tokenType != TokenType.ID ) {
			return;
		}
		
		idlist();
		match(COLON);
		Token varType = lexer.next();

		//타입 지정
		for(Symbol symbol : undefinedTypeSymbolList) {
			symbol.type = varType.tokenType;
			//변수 출력
			switch(symbol.type) {
			case INTEGER:
				emitVar(symbol.name, ""+ 0); 
				break;
			case CHAR:
				emitVar(symbol.name, " " + (int)' ');
				break;
			case BOOLEAN:
				emitVar(symbol.name, ""+ 0);  
				break;
			default : 
				throw new WrongTypeException(varType);
			}
		}
		undefinedTypeSymbolList.clear();
		
		match(SEMICOLON, VAR);
		varList();
	}
	
	private void idlist() {
		Token var = match(TokenType.ID);
		Symbol symbol = new Symbol();
		symbol.name = var.data;
		symbol.location = this.symbolLoc++;
		
		//타입은 아직 정해져 있지 않음
		undefinedTypeSymbolList.add(symbol);
		
		if( symbolTable.containsKey(var.data) ) {
			throw new DuplicateIDException(var);
		}
		
		//심볼테이블에 삽입
		symbolTable.put(var.data, symbol);
		
		if( lexer.peekNext().tokenType == COMMA ) {
			match(COMMA);
			idlist();
		}
	}
	
	private Token match(TokenType tokenType, TokenType closeTokenType) {
		try {
			return match(tokenType);
		} catch(MatchFailureException e) {
			throw new PairMatchFailureException(e.getIllegalToken(), 
					e.expected, closeTokenType);
		}
	}
	
	private Token match(TokenType tokenType) {
		Token token = lexer.next();
		if( token != null && token.tokenType != tokenType ) {
			throw new MatchFailureException(token, tokenType);
		}
		return token;
	}
}
