package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.AST;
import ASTVisitor.Parser.Visitor;

public class SymDeclaring extends AST {

    String id;
    AST value;

    public void accept(Visitor v) {
        v.visit(this);
    }

    public String getId() {
        return id;
    }

    public AST getValue() {
        return value;
    }
}