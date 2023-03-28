package ASTVisitor.Parser;

import ASTVisitor.ASTnodes.*;
import ASTVisitor.Lexer.CharStream;
import ASTVisitor.Lexer.Token;
import ASTVisitor.Lexer.TokenStream;
import ASTVisitor.Lexer.TokenType;

import java.util.ArrayList;

import static ASTVisitor.Lexer.TokenType.*;

public class ASTParser {

    private final TokenStream ts;

    public ASTParser(CharStream charStream) {
        this.ts = new TokenStream(charStream);
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
        if(ts.peek() == GEM || ts.peek() == RUTINE ||
                ts.peek() == SAET || ts.peek() == GENTAG ||
                ts.peek() == KOER || ts.peek() == HVIS || ts.peek() == PRINT) {
            AST line = line();
            ArrayList<AST> lines = lines();
            linesList.add(line);
            linesList.addAll(lines);
        } else if (ts.peek() == EOF) {
            // Do nothing (lambda-production)
        } else if (ts.peek() == NEWLINE) {
            expect(NEWLINE);
            linesList.addAll(lines());
        } else error("Forventede gem, rutine, sæt, gentag, kør eller hvis.");
        return linesList;
    }

    private AST line() {
        AST lineAST = null;
        if(ts.peek() == GEM || ts.peek() == RUTINE) {
            lineAST = dcl();
        } else lineAST = stmt();

        if(ts.peek() != EOF) expect(NEWLINE);


        return lineAST;
    }

    private AST dcl() {
        AST dclAST = null;
        if(ts.peek() == GEM){
            expect(GEM);
            dclAST = varDcl();
        } else {
            dclAST = funcDcl();
        }
        return dclAST;
    }

    private AST assign() {
        expect(SAET);
        Token idToken = expect(ID);
        expect(TIL);
        AST value = value();
        return new AssignNode(idToken.getVal(), value);
    }

    private ArrayList<AST> stmts() {
        ArrayList<AST> stmtList = new ArrayList<>();
        if(ts.peek() == GEM || ts.peek() == RUTINE ||
                ts.peek() == SAET || ts.peek() == GENTAG ||
                ts.peek() == KOER || ts.peek() == HVIS || ts.peek() == PRINT) {
            AST stmt = stmt();
            ArrayList<AST> stmts = stmts();
            stmtList.add(stmt);
            if(ts.peek() != BLOCKSLUT) expect(NEWLINE);
            stmtList.addAll(stmts);
        } else if (ts.peek() == NEWLINE) {
            expect(NEWLINE);
            stmtList.addAll(stmts());
        } else if (ts.peek() == BLOCKSLUT) {
            // Do nothing (lambda-production)
        } else error("Forventede deklaration eller kommando. Fik " + ts.peek() + ".");
        return stmtList;
    }

    private AST stmt() {
        AST stmtAST = null;
        if(ts.peek() == SAET) {
            stmtAST = assign();
        } else if(ts.peek() == GENTAG) {
            stmtAST = repeat();
        } else if(ts.peek() == KOER) {
            stmtAST = func();
        } else if (ts.peek() == HVIS) {
            expect(HVIS);
            AST ifExpr = expr();
            expect(BLOCKSTART);
            ArrayList<AST> stmts = stmts();
            expect(BLOCKSLUT);
            stmtAST = new IfNode(ifExpr, stmts);
        } else if (ts.peek() == PRINT) {
            stmtAST = print();
        } else error("Forventede kommando (sæt, gentag, kør eller hvis).");
        return stmtAST;
    }

    public AST expr() {
        AST expr = null;
        if (ts.peek() == ID || ts.peek() == HELTAL_LIT || ts.peek() == IKKE || ts.peek() == LPAREN
                || ts.peek() == TEKST_LIT || ts.peek() == DECIMALTAL_LIT || ts.peek() == BOOLSK_LIT) {
            expr = or_expr();
        } else error("Forventede boolsk udtryk.");

        if(expr == null) error("Forventede udtryk. Fik " + ts.peek());
        return expr;
    }

    // TODO: Kigge på or_expr og resten af expression-delene
    public AST or_expr() {
        AST expr;
        AST andExpr = and_expr();
        AST orExpr = or_expr2();

        if(orExpr != null) expr = new BinaryComputing(ELLER.name(), andExpr, orExpr);
        else expr = andExpr;
        
        return expr;
    }

    public AST or_expr2() {
        AST orExpr = null;
        if (ts.peek() == ELLER) {
            expect(ELLER);
            AST andExpr = and_expr();
            AST ors = or_expr2();
            if(ors != null) orExpr = new BinaryComputing(ELLER.name(), andExpr, ors);
            else orExpr = andExpr;
        } else if (ts.peek() == RPAREN || ts.peek() == BLOCKSTART) {
            // produce nothing
        } else error("Forventede udtryk eller kolon. Fandt " + ts.advance());
        return orExpr;
    }

    public AST and_expr() {
        AST expr;
        AST eqExpr = equalityExpr();
        AST andExpr = and_expr2();
        if(andExpr != null) expr = new BinaryComputing(OG.name(), eqExpr, andExpr);
        else expr = eqExpr;

        return expr;
    }

    public AST and_expr2() {
        AST andExpr = null;
        if (ts.peek() == OG) {
            expect(OG);
            AST eqExpr = equalityExpr();
            AST ands = and_expr2();
            if(ands != null) andExpr = new BinaryComputing(OG.name(), eqExpr, ands);
            else andExpr = eqExpr;
        } else if (ts.peek() == RPAREN || ts.peek() == BLOCKSTART || ts.peek() == ELLER) {
            // produce nothing
        } else error("Forventede eller, udtryk eller kolon. Fandt " + ts.advance());
        return andExpr;
    }

    public AST equalityExpr() {
        AST expr;
        AST relExpr = relExpr();
        String op = ts.peek().getName();
        AST eqExpr = equalityExpr2();
        if(eqExpr != null) expr = new BinaryComputing(op, relExpr, eqExpr);
        else expr = relExpr;

        return expr;
    }

    public AST equalityExpr2() {
        AST eqExpr = null;
        if (ts.peek() == ER) {
            String op = expect(ER).getType().getName();
            AST relExpr = relExpr();
            AST eqs = equalityExpr2();
            if(eqs != null) eqExpr = new BinaryComputing(op, eqExpr, eqs);
            else eqExpr = relExpr;
        } else if (ts.peek() == IKKE) {
            String op = expect(IKKE).getType().getName();
            op += " " + expect(ER).getType().getName();
            AST relExpr = relExpr();
            AST eqs = equalityExpr2();
            if(eqs != null) eqExpr = new BinaryComputing(op, eqExpr, eqs);
            else eqExpr = relExpr;
        } else if (ts.peek() == RPAREN || ts.peek() == BLOCKSTART || ts.peek() == ELLER || ts.peek() == OG) {
            // produce nothing
        } else error("Forventede og, eller, udtryk eller kolon. Fandt " + ts.advance());
        return eqExpr;
    }

    public AST relExpr() {
        AST expr;
        AST sumExpr = sumExpr();
        String op = ts.peek().getName();
        AST relExpr = relExpr2();
        if(relExpr != null) expr = new BinaryComputing(op, sumExpr, relExpr);
        else expr = sumExpr;

        return expr;
    }

    public AST relExpr2() {
        AST relExpr = null;
        TokenType opType = ts.peek();
        if (opType == GREATER || opType == LESSER) {
            expect(opType);
            AST sumExpr = sumExpr();
            String op = ts.peek().getName();
            AST rels = equalityExpr2();
            if(rels != null) relExpr = new BinaryComputing(op, sumExpr, rels);
            else relExpr = sumExpr;
        } else if (ts.peek() == RPAREN || ts.peek() == BLOCKSTART || ts.peek() == ELLER || ts.peek() == OG
                    || ts.peek() == ER || ts.peek() == IKKE) {
            // produce nothing
        } else error("Forventede og, eller, udtryk eller kolon. Fandt " + ts.advance());
        return relExpr;
    }

    public AST sumExpr() {
        AST expr;
        AST prodExpr = productExpr();
        String op = ts.peek().getName();
        AST sumExpr = sumExpr2();
        if(sumExpr != null) expr = new BinaryComputing(op, prodExpr, sumExpr);
        else expr = prodExpr;

        return expr;
    }

    public AST sumExpr2() {
        AST sumExpr = null;
        TokenType opType = ts.peek();
        if (opType == PLUS || opType == MINUS) {
            expect(opType);
            AST prodExpr = productExpr();
            String op = ts.peek().getName();
            AST sums = sumExpr2();
            if(sums != null) sumExpr = new BinaryComputing(op, prodExpr, sums);
            else sumExpr = prodExpr;
        } else if (ts.peek() == RPAREN || ts.peek() == BLOCKSTART || ts.peek() == ELLER || ts.peek() == OG
                    || ts.peek() == ER || ts.peek() == IKKE || ts.peek() == LESSER || ts.peek() == GREATER) {
            // produce nothing
        } else error("Forventede og, eller, udtryk eller kolon. Fandt " + ts.advance());
        return sumExpr;
    }

    public AST productExpr() {
        AST expr;
        AST notExpr = not_expr();
        String op = ts.peek().getName();
        AST prodExpr = productExpr2();
        if(prodExpr != null) expr = new BinaryComputing(op, notExpr, prodExpr);
        else expr = notExpr;

        return expr;
    }

    public AST productExpr2() {
        AST prodExpr = null;
        if (ts.peek() == TIMES || ts.peek() == DIVIDE) {
            expect(ER);
            AST notExpr = not_expr();
            String op = ts.peek().getName();
            AST prods = productExpr2();
            if(prods != null) prodExpr = new BinaryComputing(op, notExpr, prods);
            else prodExpr = notExpr;
        } else if (ts.peek() == RPAREN || ts.peek() == BLOCKSTART || ts.peek() == ELLER || ts.peek() == OG
                || ts.peek() == ER || ts.peek() == IKKE || ts.peek() == LESSER || ts.peek() == GREATER ||
                ts.peek() == PLUS || ts.peek() == MINUS) {
            // produce nothing
        } else error("Forventede og, eller, udtryk eller kolon. Fandt " + ts.advance());
        return prodExpr;
    }

    public AST not_expr() {
        AST expr;
        if(ts.peek() == IKKE) {
            expect(IKKE);
            AST factor = factor();
            expr = new UnaryComputing(IKKE.name(), factor);
        } else expr = factor();
        return expr;
    }

    //TODO skriv ordentlig fejlbesked
    public AST factor() {
        AST expr = null;
        if (ts.peek() == LPAREN) {
            expect(LPAREN);
            expr = expr();
            expect(RPAREN);
        } else if (ts.peek() == HELTAL_LIT || ts.peek() == TEKST_LIT || ts.peek() == DECIMALTAL_LIT || ts.peek() == BOOLSK_LIT) {
            expr = value();
        } else if (ts.peek() == ID) {
            expr = new IdNode(expect(ID).getVal());
        } else error("FORVENTEDE (, heltal, decimaltal, tekst ELLER boolsk");

        return expr;
    }


    private AST varDcl(){
        AST dclAst = null;
        if(ts.peek() == TEKST_DCL){
            expect(TEKST_DCL);
            AST value = value();
            expect(SOM);
            Token idToken = expect(ID);
            dclAst = new TekstDcl(value, idToken.getVal());
        } else if(ts.peek() == HELTAL_DCL){
            expect(HELTAL_DCL);
            AST value = value();
            expect(SOM);
            Token idToken = expect(ID);
            dclAst = new HeltalDcl(value, idToken.getVal());
        } else if(ts.peek() == DECIMALTAL_DCL){
            expect(DECIMALTAL_DCL);
            AST value = value();
            expect(SOM);
            Token idToken = expect(ID);
            dclAst = new DecimaltalDcl(value, idToken.getVal());
        } else if (ts.peek() == BOOLSK_DCL) {
            expect(BOOLSK_DCL);
            AST value = value();
            expect(SOM);
            Token idToken = expect(ID);
            dclAst = new BoolskDcl(value, idToken.getVal());
        } else error("Forventede type deklaration (tekst, heltal, decimaltal eller boolsk).");


        return dclAst;
    }

    private AST value() {
        AST valueAST = null;
        if (ts.peek() == TEKST_LIT) {
            valueAST = new TekstLiteral(expect(TEKST_LIT).getVal());
        } else if (ts.peek() == HELTAL_LIT) {
            valueAST = new HeltalLiteral(expect(HELTAL_LIT).getVal());
        } else if (ts.peek() == DECIMALTAL_LIT) {
            valueAST = new DecimaltalLiteral(expect(DECIMALTAL_LIT).getVal());
        } else if (ts.peek() == BOOLSK_LIT) {
            valueAST = new BoolskLiteral(expect(BOOLSK_LIT).getVal());
        }

        return valueAST;
    }

    private AST funcDcl(){
        expect(RUTINE);
        Token idToken = expect(ID);
        expect(BLOCKSTART);
        ArrayList<AST> body = stmts();
        expect(BLOCKSLUT);

        return new FuncDclNode(idToken.getVal(), body);
    }

    private AST func() {
        expect(KOER);
        Token idToken = expect(ID);

        return new FuncNode(idToken.getVal());
    }

    private AST print() {
        AST printAST = null;
        expect(PRINT);
        if(ts.peek() == ID) {
            Token idToken = expect(ID);
            printAST = new PrintNode(new IdNode(idToken.getVal()));
        } else if (ts.peek() == HELTAL_LIT || ts.peek() == DECIMALTAL_LIT ||
                ts.peek() == TEKST_LIT || ts.peek() == BOOLSK_LIT) {
            printAST = value();
        } else error("Forventede id eller værdi.");
        return printAST;
    }

    private AST repeat() {
        expect(GENTAG);
        // func call
        Token funcCall = expect(ID);
        Token intToken = expect(HELTAL_LIT);
        expect(GANGE);
        return new LoopNode(new FuncNode(funcCall.getVal()),
                new HeltalLiteral(intToken.getVal()));
    }

    private AST assStmt() {
        expect(ID);
        return null;
    }

    private Token expect(TokenType type) {
        Token token = ts.advance();
        if (token.getType() != type) {
            throw new Error("Forventede typen " +
                    type +
                    " men fik typen " +
                    token.getType());
        }
        return token;
    }

    private void error(String message) {
        throw new Error(message);
    }
}