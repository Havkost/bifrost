package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.AST;
import ASTVisitor.Parser.Visitor;

public class SymDeclaring extends AST {

    String id;

    public void accept(Visitor v) {
        v.visit(this);
    }
}
