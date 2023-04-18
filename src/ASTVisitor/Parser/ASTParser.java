package ASTVisitor.Parser;

import ASTVisitor.ASTnodes.*;
import ASTVisitor.Exceptions.UnexpectedLineStart;
import ASTVisitor.Exceptions.UnexpectedTokenException;
import ASTVisitor.Lexer.CharStream;
import ASTVisitor.Lexer.Token;
import ASTVisitor.Lexer.TokenStream;
import ASTVisitor.Lexer.TokenType;

import java.util.ArrayList;

import ASTVisitor.Lexer.TokenType;
import ASTVisitor.Parser.AST.Operators;

public class ASTParser {

    private final TokenStream ts;

    public ASTParser(CharStream charStream) {
        this.ts = new TokenStream(charStream);
    }

    public AST prog() {
        ProgramNode progAST = new ProgramNode(new ArrayList<>());
        ArrayList<AST> linesList = lines();
        expect(TokenType.EOF);
        if(linesList != null) progAST.getChild().addAll(linesList);
        return progAST;
    }

    public ArrayList<AST> lines() {
        ArrayList<AST> linesList = new ArrayList<>();
        if(ts.peek() == TokenType.GEM || ts.peek() == TokenType.RUTINE ||
                ts.peek() == TokenType.SAET || ts.peek() == TokenType.GENTAG ||
                ts.peek() == TokenType.KOER || ts.peek() == TokenType.HVIS || ts.peek() == TokenType.PRINT) {
            AST line = line();
            ArrayList<AST> lines = lines();
            linesList.add(line);
            linesList.addAll(lines);
        } else if (ts.peek() == TokenType.EOF) {
            // Do nothing (lambda-production)
        } else if (ts.peek() == TokenType.NEWLINE) {
            expect(TokenType.NEWLINE);
            linesList.addAll(lines());
        } else throw new UnexpectedLineStart(ts.peek());
        return linesList;
    }

    public AST line() {
        AST lineAST = null;
        if(ts.peek() == TokenType.GEM || ts.peek() == TokenType.RUTINE) {
            lineAST = dcl();
        } else lineAST = stmt();

        if(ts.peek() != TokenType.EOF) expect(TokenType.NEWLINE);

        return lineAST;
    }

    public AST dcl() {
        AST dclAST = null;
        if(ts.peek() == TokenType.GEM){
            expect(TokenType.GEM);
            dclAST = varDcl();
        } else {
            dclAST = funcDcl();
        }
        return dclAST;
    }

    public AST assign() {
        expect(TokenType.SAET);
        Token idToken = expect(TokenType.ID);
        expect(TokenType.TIL);
        AST value = expr();
        return new AssignNode(idToken.getVal(), value);
    }

    public ArrayList<AST> stmts() {
        ArrayList<AST> stmtList = new ArrayList<>();
        if(ts.peek() == TokenType.SAET || ts.peek() == TokenType.GENTAG ||
                ts.peek() == TokenType.KOER || ts.peek() == TokenType.HVIS || ts.peek() == TokenType.PRINT) {
            AST stmt = stmt();
            ArrayList<AST> stmts = stmts();
            stmtList.add(stmt);
            if(ts.peek() != TokenType.BLOCKSLUT) expect(TokenType.NEWLINE);
            stmtList.addAll(stmts);
        } else if (ts.peek() == TokenType.NEWLINE) {
            expect(TokenType.NEWLINE);
            stmtList.addAll(stmts());
        } else if (ts.peek() == TokenType.BLOCKSLUT) {
            // Do nothing (lambda-production)
        } else error("Forventede deklaration eller kommando. Fik " + ts.peek() + ".");
        return stmtList;
    }

    public AST stmt() {
        AST stmtAST = null;
        if(ts.peek() == TokenType.SAET) {
            stmtAST = assign();
        } else if(ts.peek() == TokenType.GENTAG) {
            stmtAST = repeat();
        } else if(ts.peek() == TokenType.KOER) {
            stmtAST = func();
        } else if (ts.peek() == TokenType.HVIS) {
            expect(TokenType.HVIS);
            AST ifExpr = expr();
            expect(TokenType.BLOCKSTART);
            ArrayList<AST> stmts = stmts();
            expect(TokenType.BLOCKSLUT);
            stmtAST = new IfNode(ifExpr, stmts);
        } else if (ts.peek() == TokenType.PRINT) {
            stmtAST = print();
        } else error("Forventede kommando (sæt, gentag, kør eller hvis).");
        return stmtAST;
    }

    public AST expr() {
        AST expr = null;
        if (ts.peek() == TokenType.ID || ts.peek() == TokenType.HELTAL_LIT || ts.peek() == TokenType.IKKE ||
                ts.peek() == TokenType.LPAREN || ts.peek() == TokenType.DECIMALTAL_LIT || ts.peek() == TokenType.BOOLSK_LIT || ts.peek() == TokenType.TEKST_LIT) {
            expr = or_expr();
        } else error("Forventede boolsk udtryk. Fik " + ts.peek());

        if(expr == null) error("Forventede udtryk. Fik " + ts.peek());
        return expr;
    }

    public AST or_expr() {
        AST expr;
        AST andExpr = and_expr();
        AST orExpr = or_expr2();

        if(orExpr != null) expr = new BinaryComputing(TokenType.ELLER.name(), andExpr, orExpr);
        else expr = andExpr;
        
        return expr;
    }

    public AST or_expr2() {
        AST orExpr = null;
        if (ts.peek() == TokenType.ELLER) {
            expect(TokenType.ELLER);
            AST andExpr = and_expr();
            AST ors = or_expr2();
            if(ors != null) orExpr = new BinaryComputing(TokenType.ELLER.name(), andExpr, ors);
            else orExpr = andExpr;
        }
        return orExpr;
    }

    public AST and_expr() {
        AST expr;
        AST eqExpr = equalityExpr();
        AST andExpr = and_expr2();
        if(andExpr != null) expr = new BinaryComputing(TokenType.OG.name(), eqExpr, andExpr);
        else expr = eqExpr;

        return expr;
    }

    public AST and_expr2() {
        AST andExpr = null;
        if (ts.peek() == TokenType.OG) {
            expect(TokenType.OG);
            AST eqExpr = equalityExpr();
            AST ands = and_expr2();
            if(ands != null) andExpr = new BinaryComputing(TokenType.OG.name(), eqExpr, ands);
            else andExpr = eqExpr;
        }
        return andExpr;
    }

    public AST equalityExpr() {
        AST expr;
        AST.Operators op = null;
        AST relExpr = relExpr();
        if (ts.peek() == TokenType.ER) {
            op = Operators.EQUALS;
        } else if (ts.peek() == TokenType.IKKE) {
            op = Operators.NOT_EQUALS;
        }
        AST eqExpr = equalityExpr2();
        if(eqExpr != null) expr = new BinaryComputing(op, relExpr, eqExpr);
        else expr = relExpr;

        return expr;
    }

    public AST equalityExpr2() {
        AST eqExpr = null;
        if (ts.peek() == TokenType.ER) {
            expect(TokenType.ER);
            Operators op = Operators.EQUALS;
            AST relExpr = relExpr();
            AST eqs = equalityExpr2();
            if(eqs != null) eqExpr = new BinaryComputing(op, relExpr, eqs);
            else eqExpr = relExpr;
        } else if (ts.peek() == TokenType.IKKE) {
            expect(TokenType.IKKE);
            expect(TokenType.ER);
            Operators op = Operators.NOT_EQUALS;
            AST relExpr = relExpr();
            AST eqs = equalityExpr2();
            if(eqs != null) eqExpr = new BinaryComputing(op, relExpr, eqs);
            else eqExpr = relExpr;
        }
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
        if (opType == TokenType.GREATER_THAN || opType == TokenType.LESS_THAN) {
            expect(opType);
            AST sumExpr = sumExpr();
            String op = ts.peek().getName();
            AST rels = equalityExpr2();
            if(rels != null) relExpr = new BinaryComputing(op, sumExpr, rels);
            else relExpr = sumExpr;
        }
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
        if (opType == TokenType.PLUS || opType == TokenType.MINUS) {
            expect(opType);
            AST prodExpr = productExpr();
            String op = ts.peek().getName();
            AST sums = sumExpr2();
            if(sums != null) sumExpr = new BinaryComputing(op, prodExpr, sums);
            else sumExpr = prodExpr;
        }
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
        TokenType opType = ts.peek();
        if (opType == TokenType.TIMES || opType == TokenType.DIVIDE) {
            expect(opType);
            AST notExpr = not_expr();
            String op = ts.peek().getName();
            AST prods = productExpr2();
            if(prods != null) prodExpr = new BinaryComputing(op, notExpr, prods);
            else prodExpr = notExpr;
        }
        return prodExpr;
    }

    public AST not_expr() {
        AST expr;
        if(ts.peek() == TokenType.IKKE) {
            expect(TokenType.IKKE);
            AST factor = factor();
            expr = new UnaryComputing(TokenType.IKKE.name(), factor);
        } else expr = factor();
        return expr;
    }

    public AST factor() {
        AST expr = null;
        if (ts.peek() == TokenType.LPAREN) {
            expect(TokenType.LPAREN);
            expr = new UnaryComputing("paren", expr());
            expect(TokenType.RPAREN);
        } else if (ts.peek() == TokenType.HELTAL_LIT || ts.peek() == TokenType.DECIMALTAL_LIT || ts.peek() == TokenType.BOOLSK_LIT || ts.peek() == TokenType.TEKST_LIT) {
            expr = value();
        } else if (ts.peek() == TokenType.ID) {
            expr = new IdNode(expect(TokenType.ID).getVal());
        } else {
            /*
            for (int i = 0; i < ts.getTokenList().size(); i++) {
                System.out.println(i + ". " + ts.getTokenList().get(i));
            }*/
            error("FORVENTEDE (, heltal, decimaltal, tekst ELLER boolsk. Index: " + ts.getIndex());
        }

        return expr;
    }

    public AST varDcl(){
        AST dclAst = null;
        if(ts.peek() == TokenType.TEKST_DCL){
            expect(TokenType.TEKST_DCL);
            AST value = expr();
            expect(TokenType.SOM);
            Token idToken = expect(TokenType.ID);
            dclAst = new TekstDcl(value, idToken.getVal());
        } else if(ts.peek() == TokenType.HELTAL_DCL){
            expect(TokenType.HELTAL_DCL);
            AST value = expr();
            expect(TokenType.SOM);
            Token idToken = expect(TokenType.ID);
            dclAst = new HeltalDcl(value, idToken.getVal());
        } else if(ts.peek() == TokenType.DECIMALTAL_DCL){
            expect(TokenType.DECIMALTAL_DCL);
            AST value = expr();
            expect(TokenType.SOM);
            Token idToken = expect(TokenType.ID);
            dclAst = new DecimaltalDcl(value, idToken.getVal());
        } else if (ts.peek() == TokenType.BOOLSK_DCL) {
            expect(TokenType.BOOLSK_DCL);
            AST value = expr();
            expect(TokenType.SOM);
            Token idToken = expect(TokenType.ID);
            dclAst = new BoolskDcl(value, idToken.getVal());
        } else error("Forventede type deklaration (tekst, heltal, decimaltal eller boolsk). Fik: " + ts.peek());

        return dclAst;
    }

    private AST value() {
        AST valueAST = null;
        if (ts.peek() == TokenType.TEKST_LIT) {
            valueAST = new TekstLiteral(expect(TokenType.TEKST_LIT).getVal());
        } else if (ts.peek() == TokenType.HELTAL_LIT) {
            valueAST = new HeltalLiteral(expect(TokenType.HELTAL_LIT).getVal());
        } else if (ts.peek() == TokenType.DECIMALTAL_LIT) {
            valueAST = new DecimaltalLiteral(expect(TokenType.DECIMALTAL_LIT).getVal());
        } else if (ts.peek() == TokenType.BOOLSK_LIT) {
            valueAST = new BoolskLiteral(expect(TokenType.BOOLSK_LIT).getVal());
        }

        return valueAST;
    }

    private AST funcDcl(){
        expect(TokenType.RUTINE);
        Token idToken = expect(TokenType.ID);
        expect(TokenType.BLOCKSTART);
        ArrayList<AST> body = stmts();
        expect(TokenType.BLOCKSLUT);

        return new FuncDclNode(idToken.getVal(), body);
    }

    public AST func() {
        expect(TokenType.KOER);
        Token idToken = expect(TokenType.ID);

        return new FuncNode(idToken.getVal());
    }

    public AST print() {
        expect(TokenType.PRINT);
        AST value = expr();

        return new PrintNode(value);
    }

    public AST repeat() {
        expect(TokenType.GENTAG);
        Token funcCall = expect(TokenType.ID);
        AST repeatCount = expr();
        expect(TokenType.GANGE);

        return new LoopNode(funcCall.getVal(), repeatCount);
    }

    public Token expect(TokenType type) {
        Token token = ts.advance();
        if (token.getType() != type) {
            /*
            for (int i = 0; i < ts.getTokenList().size(); i++) {
                System.out.println(i + ". " + ts.getTokenList().get(i));
            }
            System.out.println(ts.getIndex());*/
            throw new UnexpectedTokenException(type, token.getType());
        }
        return token;
    }

    private void error(String message) {
        throw new Error(message);
    }
}