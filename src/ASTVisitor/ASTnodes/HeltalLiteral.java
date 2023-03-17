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

    }

    public String getValue() {
        return value;
    }
}
