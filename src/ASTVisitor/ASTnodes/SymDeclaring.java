package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.Visitor;

public class SymDeclaring extends Node {

    String id;

    public void accept(Visitor v) {
        v.visit(this);
    }
}