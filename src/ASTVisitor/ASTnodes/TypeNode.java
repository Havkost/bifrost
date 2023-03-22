package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.AST;
import ASTVisitor.Parser.Visitor;

public class TypeNode extends AST {

    private String name;

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }

    public TypeNode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
