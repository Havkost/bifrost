package ASTVisitor.Parser;

import ASTVisitor.ASTnodes.*;
import ASTVisitor.Lexer.CharStream;
import ASTVisitor.Lexer.Token;
import ASTVisitor.Lexer.TokenStream;
import ASTVisitor.Lexer.TokenType;
import com.sun.jdi.Value;

import java.util.ArrayList;

import static ASTVisitor.Lexer.TokenType.*;

public class ASTParser {

    private TokenStream tokenStream;

    public ASTParser(CharStream charStream) {
        this.tokenStream = new TokenStream(charStream);
    }

    public AST prog() {
        ProgramNode progAST = new ProgramNode(new ArrayList<>());
        ArrayList<AST> linesList = lines();
        expect(EOF);
        if(linesList != null) progAST.getChild().addAll(linesList);
        return progAST;
    }

    public ArrayList<AST> lines() {
        ArrayList<AST> linesList = new ArrayList<>();
        if(tokenStream.peek() == GEM || tokenStream.peek() == RUTINE ||
                tokenStream.peek() == SAET || tokenStream.peek() == GENTAG ||
                tokenStream.peek() == KOER || tokenStream.peek() == HVIS) {
            AST line = line();
            ArrayList<AST> lines = lines();
            linesList.add(line);
            linesList.addAll(lines);
        } else if (tokenStream.peek() == EOF) {
            // Do nothing (lambda-production)
        } else error("Forventede gem, rutine, sæt, gentag, kør eller hvis.");
        return linesList;
    }

    private AST line() {
        AST lineAST = null;
        if(tokenStream.peek() == GEM || tokenStream.peek() == RUTINE) {
            lineAST = dcl();
        } else lineAST = stmt();

        return lineAST;
    }

    private AST dcl() {
        AST dclAST = null;
        if(tokenStream.peek() == GEM){
            expect(GEM);
            dclAST = varDcl();
        } else {
            expect(RUTINE);
            dclAST = funcDcl();
        }
        return dclAST;
    }

    private AST stmt() {
        AST stmtAST = null;
        if(tokenStream.peek() == SAET) {
            expect(SAET);
            Token idToken = expect(ID);
            expect(TIL);
            AST value = value();
            stmtAST = new AssignNode(idToken.getVal(), value);
        } else if(tokenStream.peek() == GENTAG) {
            expect(GENTAG);
            // func call
            Token funcCall = expect(ID);
            Token intToken = expect(HELTAL_LIT);
            expect(GANGE);
            stmtAST = new LoopNode(new FuncNode(funcCall.getVal()),
                    new HeltalLiteral(intToken.getVal()));

        } else if(tokenStream.peek() == KOER) {
            //stmtAST = funcStmt();
        } else if (tokenStream.peek() == HVIS) {
            //stmtAST = ifStmt();
        } else error("Forventede kommando (sæt, gentag, kør eller hvis).");
        return stmtAST;
    }


    private AST varDcl(){
        AST dclAST = null;
        if(tokenStream.peek() == TEKST_DCL){
            expect(TEKST_DCL);
            AST value = value();
            expect(SOM);
            Token idToken = expect(ID);
        } else if(tokenStream.peek() == HELTAL_DCL){
            expect(HELTAL_DCL);
            AST value = value();
            expect(SOM);
            Token idToken = expect(ID);
        } else if(tokenStream.peek() == DECIMALTAL_DCL){
            expect(DECIMALTAL_DCL);
            AST value = value();
            expect(SOM);
            expect(ID);
        } else if (tokenStream.peek() == BOOLSK_DCL) {
            expect(BOOLSK_DCL);
            expect(BOOLSK_LIT);
            expect(SOM);
        } else error("Forventede type deklaration (tekst, heltal, decimaltal eller boolsk).");
        return dclAST;
    }

    private AST value() {
        AST valueAST = null;
        if (tokenStream.peek() == TEKST_LIT) {
            Token tekstToken = expect(TEKST_LIT);
            valueAST = new TekstLiteral(tekstToken.getVal());
        } else if (tokenStream.peek() == HELTAL_LIT) {
            Token heltalToken = expect(HELTAL_LIT);
            valueAST = new HeltalLiteral(heltalToken.getVal());
        } else if (tokenStream.peek() == DECIMALTAL_LIT) {
            Token decimaltalToken = expect(DECIMALTAL_LIT);
            valueAST = new TekstLiteral(decimaltalToken.getVal());
        } else if (tokenStream.peek() == BOOLSK_LIT) {
            Token boolskToken = expect(BOOLSK_LIT);
            valueAST = new TekstLiteral(boolskToken.getVal());
        }

        return valueAST;
    }

    private AST funcDcl(){
        return null;
    }

    private AST assStmt() {
        expect(ID);
        return null;
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
