package kdy.compiler.lexer;

public class Token {
   public Token() {
   }
   
   public Token(String data, TokenType tokenType) {
      this.data = data;
      this.tokenType = tokenType;
   }
   
   public TokenType tokenType;
   public String data;
   public int lineNumber;
   public int wordIdx;
}