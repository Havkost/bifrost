package ASTVisitor.Parser;

import ASTVisitor.ASTnodes.*;
import ASTVisitor.Exceptions.*;
import ASTVisitor.Lexer.CharStream;
import ASTVisitor.Lexer.Token;
import ASTVisitor.Lexer.TokenStream;
import ASTVisitor.Lexer.TokenType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ASTVisitor.Parser.AST.Operators;

public class ASTParser {

    private final TokenStream ts;
    private int line;

    public ASTParser(CharStream charStream) {
        this.ts = new TokenStream(charStream);
        this.line = 1;
    }

    public AST prog() {
        ProgramNode progAST = new ProgramNode(new ArrayList<>(), line);
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
        } else throw new UnexpectedLineStart(ts.peek(), line);
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
        String parentId = null;
        expect(TokenType.SAET);
        String id = expect(TokenType.ID).getVal();

        // Fields
        if (ts.peek() == TokenType.FOR) {
            expect(TokenType.FOR);
            parentId = expect(TokenType.ID).getVal();
        }
        expect(TokenType.TIL);
        AST value = expr(0);
        if (parentId != null)
            return new AssignNode(new IdNode(id, parentId, line), value, line);
        return new AssignNode(new IdNode(id, line), value, line);
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
        } else throw new IllegalStatementException(ts.peek(), line);
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
            AST ifExpr = expr(0);
            expect(TokenType.NEWLINE);
            ArrayList<AST> stmts = stmts();
            expect(TokenType.BLOCKSLUT);
            stmtAST = new IfNode(ifExpr, stmts, line);
        } else if (ts.peek() == TokenType.PRINT) {
            stmtAST = print();
        } else throw new IllegalStatementException(ts.peek(), line);
        return stmtAST;
    }

    public AST expr(int p) {
        AST lhs = factor();

        while (isBinaryOperator(ts.peek()) && getBinaryOperator(ts.peek(), false).prec >= p) {
            Operators op =  getBinaryOperator(ts.peek(), true);
            int q = 1 + op.prec;
            AST rhs = expr(q);
            lhs = new BinaryComputing(op, lhs, rhs);
        }

        return lhs;
    }

    boolean isBinaryOperator(TokenType type) {
        switch (type) {
            case ELLER, OG, ER, IKKE, DIVIDE, TIMES, PLUS, MINUS, LESS_THAN, GREATER_THAN -> {
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    Operators getBinaryOperator(TokenType type, boolean consume) {
        Operators op;
        switch (type) {
            case ELLER -> op = Operators.OR;
            case OG -> op = Operators.AND;
            case ER -> op = Operators.EQUALS;
            case IKKE -> {
                op = Operators.NOT_EQUALS;
                if (consume)
                    expect(TokenType.IKKE);
            }
            case DIVIDE -> op = Operators.DIVISION;
            case TIMES -> op = Operators.TIMES;
            case PLUS -> op = Operators.PLUS;
            case MINUS -> op = Operators.MINUS;
            case LESS_THAN -> op = Operators.LESS_THAN;
            case GREATER_THAN -> op = Operators.GREATER_THAN;
            default -> throw new UnexpectedExpressionToken(type, line);
        }
        if (consume)
            expect(ts.peek());
        return op;
    }

    public AST factor() {
        AST expr;
        if (ts.peek() == TokenType.IKKE) {
            expect(TokenType.IKKE);
            expr = new UnaryComputing(Operators.NOT, expr(Operators.NOT.prec), line);
        } else if (ts.peek() == TokenType.LPAREN) {
            expect(TokenType.LPAREN);
            expr = new UnaryComputing("paren", expr(0), line);
            expect(TokenType.RPAREN);
        } else if (ts.peek() == TokenType.HELTAL_LIT || ts.peek() == TokenType.DECIMALTAL_LIT ||
                   ts.peek() == TokenType.BOOLSK_LIT || ts.peek() == TokenType.TEKST_LIT ||
                   ts.peek() == TokenType.KLOKKEN ||
                   ts.peek() == TokenType.TID || ts.peek() == TokenType.MINUS) {
            expr = value();
        } else if (ts.peek() == TokenType.ID) {
            expr = field();
        } else {
            throw new UnexpectedExpressionToken(ts.peek(), line);
        }

        return expr;
    }

    public AST varDcl(){
        AST dclAst = null;
        boolean negative = false;
        if(ts.peek() == TokenType.TEKST_DCL){
            expect(TokenType.TEKST_DCL);
            AST value = expr(0);
            expect(TokenType.SOM);
            Token idToken = expect(TokenType.ID);
            return new TekstDcl(value, new IdNode(idToken.getVal()), line);
        } if (ts.peek() == TokenType.DEVICE_DCL) {
            expect(TokenType.DEVICE_DCL);
            return deviceDcl();
        } if (ts.peek() == TokenType.TID_DCL) {
            expect(TokenType.TID_DCL);
            AST value = expr(0);
            expect(TokenType.SOM);
            Token idToken = expect(TokenType.ID);
            return new TidDcl(value, new IdNode(idToken.getVal()), line);
        } if(ts.peek() == TokenType.HELTAL_DCL){
            expect(TokenType.HELTAL_DCL);
            AST value = expr(0);
            expect(TokenType.SOM);
            Token idToken = expect(TokenType.ID);
            return new HeltalDcl(value, new IdNode(idToken.getVal()), line);
        } if(ts.peek() == TokenType.DECIMALTAL_DCL){
            expect(TokenType.DECIMALTAL_DCL);
            AST value = expr(0);
            expect(TokenType.SOM);
            Token idToken = expect(TokenType.ID);
            return new DecimaltalDcl(value, new IdNode(idToken.getVal()), line);
        } if (ts.peek() == TokenType.BOOLSK_DCL) {
            expect(TokenType.BOOLSK_DCL);
            AST value = expr(0);
            expect(TokenType.SOM);
            Token idToken = expect(TokenType.ID);
            return new BoolskDcl(value, new IdNode(idToken.getVal()), line);
        }
        throw new UnexpectedDeclarationToken(ts.peek(), line);
    }

    public AST deviceDcl() {
        String endpoint = expect(TokenType.TEKST_LIT).getVal();
        expect(TokenType.MED);
        expect(TokenType.NEWLINE);
        List<VariableDcl> fields = fields();
        expect(TokenType.SOM);
        String id = expect(TokenType.ID).getVal();

        DeviceNode device = new DeviceNode(id, fields, endpoint, line);
        device.getFields().forEach((field) -> {
            field.getId().setParentId(id);
        });

        return device;
    }

    public List<VariableDcl> fields() {
        List<VariableDcl> fields = new ArrayList<>();
        while (ts.peek() == TokenType.HELTAL_DCL || ts.peek() == TokenType.DECIMALTAL_DCL || ts.peek() == TokenType.TEKST_DCL || ts.peek() == TokenType.BOOLSK_DCL) {
            IdNode id;
            AST value;
            switch (ts.peek()) {
                case BOOLSK_DCL -> {
                    expect(TokenType.BOOLSK_DCL);
                    value = expr(0);
                    expect(TokenType.SOM);
                    id = new IdNode(expect(TokenType.ID).getVal());
                    expect(TokenType.NEWLINE);
                    fields.add(new BoolskDcl(value, id, line));
                }
                case HELTAL_DCL -> {
                    expect(TokenType.HELTAL_DCL);
                    value = expr(0);
                    expect(TokenType.SOM);
                    id = new IdNode(expect(TokenType.ID).getVal());
                    expect(TokenType.NEWLINE);
                    fields.add(new HeltalDcl(value, id, line));
                }
                case DECIMALTAL_DCL -> {
                    expect(TokenType.DECIMALTAL_DCL);
                    value = expr(0);
                    expect(TokenType.SOM);
                    id = new IdNode(expect(TokenType.ID).getVal());
                    expect(TokenType.NEWLINE);
                    fields.add(new DecimaltalDcl(value, id, line));
                }
                case TEKST_DCL -> {
                    expect(TokenType.TEKST_DCL);
                    value = expr(0);
                    expect(TokenType.SOM);
                    id = new IdNode(expect(TokenType.ID).getVal());
                    expect(TokenType.NEWLINE);
                    fields.add(new TekstDcl(value, id, line));
                }
            }
        }

        return fields;
    }

    AST value() {
        boolean negative = false;
        if (ts.peek() == TokenType.TEKST_LIT) {
            return new TekstLiteral(expect(TokenType.TEKST_LIT).getVal(), line);
        } if (ts.peek() == TokenType.BOOLSK_LIT) {
            return new BoolskLiteral(expect(TokenType.BOOLSK_LIT).getVal(), line);
        } if (ts.peek() == TokenType.TID) {
            List<String> value = Arrays.stream(expect(TokenType.TID).getVal().split(":")).toList();
            return new TidNode(Integer.parseInt(value.get(0)), Integer.parseInt(value.get(1)), line);
        } if (ts.peek() == TokenType.KLOKKEN) {
            expect(TokenType.KLOKKEN);
            return new KlokkenNode(line);
        } if (ts.peek() == TokenType.MINUS) {
            expect(TokenType.MINUS);
            negative = true;
        } if (ts.peek() == TokenType.HELTAL_LIT) {
            String num = expect(TokenType.HELTAL_LIT).getVal();
            if (negative)
                return new HeltalLiteral("-" + num, line);
            return new HeltalLiteral(num, line);
        } if (ts.peek() == TokenType.DECIMALTAL_LIT) {
            String num = expect(TokenType.DECIMALTAL_LIT).getVal();
            if (negative)
                return new DecimaltalLiteral("-" + num, line);
            return new DecimaltalLiteral(num, line);
        }
        throw new UnexpectedExpressionToken(ts.peek(), line);
    }

    private AST funcDcl(){
        expect(TokenType.RUTINE);
        Token idToken = expect(TokenType.ID);
        expect(TokenType.NEWLINE);
        ArrayList<AST> body = stmts();
        expect(TokenType.BLOCKSLUT);

        return new FuncDclNode(idToken.getVal(), body, line);
    }

    public AST func() {
        expect(TokenType.KOER);
        Token idToken = expect(TokenType.ID);

        return new FuncNode(idToken.getVal(), line);
    }

    public AST print() {
        expect(TokenType.PRINT);
        AST value = expr(0);

        return new PrintNode(value, line);
    }

    public AST repeat() {
        expect(TokenType.GENTAG);
        Token funcCall = expect(TokenType.ID);
        AST repeatCount = expr(0);
        expect(TokenType.GANGE);

        return new LoopNode(funcCall.getVal(), repeatCount, line);
    }

    public AST field() {
        AST res;
        String id = expect(TokenType.ID).getVal();
        String parent = parent();

        if (parent != null) {
            res = new IdNode(id, parent, line);
        } else {
            res = new IdNode(id, line);
        }

        return res;
    }

    public String parent() {
        String res = null;
        if(ts.peek() == TokenType.FOR) {
            expect(TokenType.FOR);
            res = expect(TokenType.ID).getVal();
        }
        return res;
    }

    /**
     * Checks an expected TokenType against the type of the next token in the stream, if they match,
     * the token is returned. Otherwise, an error is thrown.
     * @param type expected type
     * @return <strong><code>Token</code></strong> from stream
     */
    public Token expect(TokenType type) {
        Token token = ts.advance();
        if (token.getType() != type) {
            System.out.println(ts.getTokenList());
            throw new UnexpectedTokenException(type, token.getType(), line);
        }
        if (type.equals(TokenType.NEWLINE)) line++;
        return token;
    }
}