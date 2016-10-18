package kdy.compiler.lexer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Predictive parsing Recursive-descent parsing
 * 
 * @author ICo
 *
 */
public class ImplementLexicalAnalyzer implements LexicalAnalyzer {
   private File sourceFile;
   private FileInputStream sourceFIS;
   private char c = ' ';
   private Token nowToken = new Token();
   private Token nextToken = nowToken;

   private int lineNumber = 0;
   private int wordIdx = 0;
   
   public ImplementLexicalAnalyzer() {
      setDefault("testfile.txt");
   }

   public ImplementLexicalAnalyzer(String fileName) {
      setDefault(fileName);
   }

   // set default data when Lexical Analysis constructors called
   public void setDefault(String fileName) {
      try {
         sourceFile = new File(fileName);
         sourceFIS = new FileInputStream(sourceFile);
      } catch (FileNotFoundException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

   }

   // read 1 character from file
   public char readNextChar() throws IOException {
      if (sourceFIS.available() != 0) {
         // if remain character to read
         wordIdx++;
         return (char) sourceFIS.read();
      } else {
         // if there are no character to read
         return '\0';
      }
   }

   // get next token
   public Token next() {
      if (nowToken.equals(nextToken)) {
         nowToken = getToken();
         nextToken = nowToken;
      } else {
         nowToken = nextToken;
      }
      return nowToken;
   }

   // show next token
   public Token peekNext() {
      if(nowToken.equals(nextToken)){
         // first time read next token;
         nextToken = getToken();
      }
      return nextToken;

   }

   public Token getToken() {
      Token reToken = null;

         // erace command and blank t
         eraseBlank();
         eraseCommand();
         if (Character.isDigit(c)) {
            // if character is digit
            reToken = getNumber();
         } else if (Character.isAlphabetic(c)) {
            // else character is char
            reToken = getCharacter();
         } else if (c == '\'') {
            reToken = getString();
         } else {
            reToken = getOperatorString();
         }

      return reToken;
   }

   public void eraseCommand() {
      try {
         if(c == '{'){
            while (c != '}') {
               c = readNextChar();
            }
            c = readNextChar();
            eraseBlank();
         }
      } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

   }

   public void eraseBlank() {
      try {
         while (blankCharacter(c)) {
            c = readNextChar();
         }
         eraseCommand();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   public boolean blankCharacter(char inC) {
      if (inC == ' ' || inC == '\t' || inC == '\r') {
         return true;
      } else if(inC == '\n'){ 
         wordIdx = 0;
         lineNumber++;
         return true;
      } else {
         return false;
      }
   }

   public Token getCharacter() {
      Token reToken = new Token();
      String str = "";
      reToken.lineNumber = lineNumber;
      reToken.wordIdx = wordIdx;

      // tokenizing
      try {
         str += c;
         while (true) {
            c = readNextChar();
            if (blankCharacter(c) || (c == ';')) {
               break;
            } else if (!Character.isAlphabetic(c) && !Character.isDigit(c)) {
               break;
            } else {
               str += c;
            }
         }
      } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

      if (str.equals("PROGRAM")) {
         reToken.tokenType = TokenType.PROGRAM;
      } else if (str.equals("BEGIN")) {
         reToken.tokenType = TokenType.BEGIN;
      } else if (str.equals("END")) {
         if(c == '.'){
            reToken.tokenType = TokenType.PROGRAMEND;
            str += c;
            try{
               c = readNextChar();
            } catch(IOException e){
               e.printStackTrace();
            }
         }else{
         reToken.tokenType = TokenType.END;
         }
      } else if (str.equals("WHILE")) {
         reToken.tokenType = TokenType.WHILE;
      } else if (str.equals("DO")) {
         reToken.tokenType = TokenType.DO;
      } else if (str.equals("IF")) {
         reToken.tokenType = TokenType.IF;
      } else if (str.equals("THEN")) {
         reToken.tokenType = TokenType.THEN;
      } else if (str.equals("ELSIF")) {
         reToken.tokenType = TokenType.ELSIF;
      } else if (str.equals("ELSE")) {
         reToken.tokenType = TokenType.ELSE;
      } else if (str.equals("VAR")) {
         reToken.tokenType = TokenType.VAR;
      } else if (str.equals("INTEGER")) {
         reToken.tokenType = TokenType.INTEGER;
      } else if (str.equals("CHAR")) {
         reToken.tokenType = TokenType.CHAR;
      } else if (str.equals("BOOLEAN")) {
         reToken.tokenType = TokenType.BOOLEAN;
      } else if (str.startsWith("\'")) {
         reToken.tokenType = TokenType.CHAR;
      } else if (str.equals("AND")) {
         reToken.tokenType = TokenType.AND;
      } else if (str.equals("OR")) {
         reToken.tokenType = TokenType.OR;
      } else if (str.equals("NOT")) {
         reToken.tokenType = TokenType.NOT;
      } else if (str.equals("WRITE")){
         reToken.tokenType = TokenType.WRITE;
      } else if (str.equals("READ")) {
         reToken.tokenType = TokenType.READ;
      } else if (str.equals("TRUE")) {
          reToken.tokenType = TokenType.TRUE;
      } else if (str.equals("FALSE")) {
          reToken.tokenType = TokenType.FALSE;
      } else {
         reToken.tokenType = TokenType.ID;
      }
      reToken.data = str;
      return reToken;
   }

   public Token getNumber() {
      Token reToken = new Token();
      String str = "";
      reToken.lineNumber = lineNumber;
      reToken.wordIdx = wordIdx;
      // tokenizing
      try {
         str += c;
         while (true) {
            c = readNextChar();
            if (blankCharacter(c) || (c == ';')) {
               break;
            } else {
               if (Character.isDigit(c)) {
                  str += c;
               } else {
                  break;
               }
            }
         }
      } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

      reToken.data = str;
      reToken.tokenType = TokenType.NUMBER;
      return reToken;

   }
   

   public Token getString() {
      // 臾몄옄�뿴
      Token reToken = new Token();
      reToken.lineNumber = lineNumber;
      reToken.wordIdx = wordIdx;
      String str = "";

      // tokenizing
      try {
         while (true) {
            c = readNextChar();
            if (c == '\'') {
               c = readNextChar();
               break;
            } else {
               str += c;

            }
         }
      } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

      reToken.data = str;
      reToken.tokenType = TokenType.STRING;

      return reToken;
   }

   public Token getOperatorString() {
      Token reToken = new Token();
      String str = "";
      reToken.lineNumber = lineNumber;
      reToken.wordIdx = wordIdx;

      str += c;
      if (str.equals("=")) {
         reToken.tokenType = TokenType.EQL;
      } else if (str.equals(">")) {
         try{
            c = readNextChar();
         } catch(IOException e){
            e.printStackTrace();
         }
         
         if(c == '='){
            reToken.tokenType = TokenType.BIGEQL;
            str += c;
         }else{
            reToken.tokenType = TokenType.BIG;
            reToken.data = str;
            return reToken;
         }
         reToken.data = str;
      } else if (str.equals("<")) {
         try{
            c = readNextChar();
         } catch(IOException e){
            e.printStackTrace();
         }
         
         if(c == '='){
            reToken.tokenType = TokenType.LESSEQL;
            str += c;
         } else if (c == '>'){
            reToken.tokenType = TokenType.NOTEQL;
            str += c;
         } else{
            reToken.tokenType = TokenType.LESS;
            reToken.data = str;
            return reToken;
         }
         reToken.data = str;
      } else if (str.equals(";")) {
         reToken.tokenType = TokenType.SEMICOLON;
      } else if (str.equals(":")) {
         try{
            c = readNextChar();
         } catch(IOException e){
            e.printStackTrace();
         }
         
         if(c == '='){
            reToken.tokenType = TokenType.ASSIGN;
            str += c;
         }else{
            reToken.tokenType = TokenType.COLON;
            reToken.data = str;
            return reToken;
         }
      } else if (str.equals("+")) {
         reToken.tokenType = TokenType.PLUS;
      } else if (str.equals("-")) {
         reToken.tokenType = TokenType.MINUS;
      } else if (str.equals("*")) {
         reToken.tokenType = TokenType.MULTI;
      } else if (str.equals("/")) {
         reToken.tokenType = TokenType.DIVIDE;
      } else if (str.equals("(")) {
         reToken.tokenType = TokenType.PAREN_LEFT;
      } else if (str.equals(")")) {
         reToken.tokenType = TokenType.PAREN_RIGHT;
      } else if (str.equals(",")) {
         reToken.tokenType = TokenType.COMMA;
      } else if (str.equals(".")) {
         reToken.tokenType = TokenType.POINT;
      } 
         reToken.data = str;
         try{
            c = readNextChar();
         } catch(IOException e){
            e.printStackTrace();
         }
      return reToken;
   }

}