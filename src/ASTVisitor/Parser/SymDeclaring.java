package ASTVisitor.Parser;

public class SymDeclaring extends Node {

    String id;

    public void accept(Visitor v) {
        v.visit(this);
    }
}
