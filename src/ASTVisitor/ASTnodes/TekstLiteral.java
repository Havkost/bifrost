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
        v.visit(this);
    }

    public String getVal() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TekstLiteral)) return false;
        TekstLiteral object = (TekstLiteral) obj;
        if (this.value.equals(object.getVal())) {
            return true;
        }
        return false;
    }
}
