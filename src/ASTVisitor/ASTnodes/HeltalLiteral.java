package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.AST;
import ASTVisitor.Parser.Visitor;

public class HeltalLiteral extends AST {

    private String value;

    public HeltalLiteral(String value) {
        this.value = value;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }

    public String getVal() {
        return value;
    }

    @Override
    public String toString() {
        return "HeltalLiteral{" +
                "val='" + value + "'" +
                '}';
    }
}
