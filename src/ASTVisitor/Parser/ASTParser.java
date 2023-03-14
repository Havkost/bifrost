package ASTVisitor.Parser;

import ASTVisitor.Lexer.CharStream;
import ASTVisitor.Lexer.Token;
import ASTVisitor.Lexer.TokenStream;
import ASTVisitor.Lexer.TokenType;

import java.util.ArrayList;

import static ASTVisitor.Lexer.TokenType.*;

public class ASTParser {

    private TokenStream tokenStream;

    public ASTParser(CharStream charStream) {
        this.tokenStream = new TokenStream(charStream);
    }

    public Node prog() {
        Prog progAST = new Prog(new ArrayList<>());
        ArrayList<Node> linesList = lines();
        expect(EOF);
        if(linesList != null) progAST.child.addAll(linesList);
        return progAST;
    }

    public ArrayList<Node> lines() {
        ArrayList<Node> linesList = new ArrayList<>();
        if(tokenStream.peek() == GEM || tokenStream.peek() == RUTINE ||
                tokenStream.peek() == SAET || tokenStream.peek() == GENTAG ||
                tokenStream.peek() == KOER || tokenStream.peek() == HVIS) {
            Node line = line();
            ArrayList<Node> lines = lines();
            linesList.add(line);
            linesList.addAll(lines);
        } else if (tokenStream.peek() == EOF) {
            // Do nothing (lambda-production)
        } else error("Forventede gem, rutine, sæt, gentag, kør eller hvis.");
        return linesList;
    }

    private Node line() {
        Node lineAST = null;
        if(tokenStream.peek() == GEM || tokenStream.peek() == RUTINE) {
            lineAST = dcl();
        } else lineAST = stmt();

        return lineAST;
    }

    private Node dcl() {
        Node dclAST = null;
        if(tokenStream.peek() == GEM){
            expect(GEM);
            dclAST = varDcl();
        } else {
            expect(RUTINE);
            dclAST = funcDcl();
        }
        return dclAST;
    }

    private Node stmt() {
        Node stmtAST = null;
        if(tokenStream.peek() == SAET) {
            expect(SAET);
            Token idToken = expect(ID);
            expect(TIL);
            Node value = value();
            stmtAST = new Assigning(idToken.getVal(), value);

        } else if(tokenStream.peek() == GENTAG) {
            stmtAST = loopStmt();
        } else if(tokenStream.peek() == KOER) {
            stmtAST = funcStmt();
        } else if (tokenStream.peek() == HVIS) {
            stmtAST = ifStmt();
        } else error("Forventede sæt, gentag, kør eller hvis");
        return stmtAST;
    }


    private Node varDcl(){
        if(tokenStream.peek() == TEKST_DCL){
            expect(TEKST_DCL);
            Node value = value();
            expect(SOM);
            Token idToken = expect(ID);
        } else if(tokenStream.peek() == HELTAL_DCL){
            expect(HELTAL_DCL);
            Node value = value();
            expect(SOM);
            Token idToken = expect(ID);
        } else if(tokenStream.peek() == DECIMALTAL_DCL){
            expect(DECIMALTAL_DCL);
            expect(DECIMALTAL_LIT);
            expect(SOM);
            expect(ID);
        } else if (tokenStream.peek() == BOOLSK_DCL) {
            expect(BOOLSK_DCL);
            expect(BOOLSK_LIT);
            expect(SOM)
        } else error("Forventede type deklaration (tekst, heltal, decimaltal eller boolsk).");
    }

    private Node funcDcl(){

    }

    private Node assStmt() {
        expect(ID);
        expect();
    }

    private Token expect(TokenType type) {
        Token token = tokenStream.advance();
        if (token.getType() != type) {
            throw new Error("Forventede typen " +
                    token.getType() +
                    "men fik typen " +
                    type);
        }
        return token;
    }

    private void error(String message) {
        throw new Error(message);
    }
}
