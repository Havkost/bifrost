package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.AST;
import ASTVisitor.Parser.Visitor;

public class TekstLiteral extends AST {

    private String value;

    public TekstLiteral(String value) {
        this.value = value;
    }

    @Override
    public void accept(Visitor v) {

    }

    public String getValue() {
        return value;
    }
}
