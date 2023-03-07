package ASTvisitor;

import ASTvisitor.Visitor;

import java.util.Hashtable;

public abstract class AST {

    public static Hashtable<String, TokenType> SymbolTable = new Hashtable<>();
    AST() {
        //CODE HERE EVENTUALLY
    }

    public abstract void accept(Visitor v);
}
