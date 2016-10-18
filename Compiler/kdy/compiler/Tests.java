package kdy.compiler;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import kdy.compiler.lexer.ImplementLexicalAnalyzer;
import kdy.compiler.lexer.LexicalAnalyzer;
import kdy.compiler.lexer.TestLexicalAnalyzer;
import kdy.compiler.lexer.Token;

import org.junit.Test;

public class Tests {
	
	@Test
	public void test1() {
		LexicalAnalyzer analyzer1 = 
				new ImplementLexicalAnalyzer("C:\\Users\\13prime\\workspace\\compiler\\Compiler\\build\\classes\\kdy\\compiler\\euclidSource.txt");
		
		LexicalAnalyzer analyzer2 = 
				new TestLexicalAnalyzer();
		
		for(int i = 0 ; i<100 ; i++ ) {
			Token t1 = analyzer1.peekNext();
			analyzer1.next();
			System.out.println(t1.data + " " + t1.tokenType);
			
			/*
			Token t2 = analyzer2.next();
			
			if( t1 == null || t2 == null ) {
				break;
			}
			
			if( !t1.data.equals(t2.data) ) {
				System.out.println(t1.data + " != " + t2.data);
			}
			if( t1.tokenType != t2.tokenType ) {
				System.out.println(t1.data + " , " + t1.tokenType + 
						" != " + t2.data + " , " + t2.tokenType);
			}
			*/
		}
	}
}
