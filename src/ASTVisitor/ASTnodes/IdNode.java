package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.AST;
import ASTVisitor.Parser.Visitor;

public class IdNode extends AST {

    private String name;

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }

    public IdNode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
