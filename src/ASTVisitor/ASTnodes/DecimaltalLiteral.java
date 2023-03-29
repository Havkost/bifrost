package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.AST;
import ASTVisitor.Parser.Visitor;

public class DecimaltalLiteral extends AST {

    private String value;

    public DecimaltalLiteral(String value) {
        this.value = value;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof DecimaltalLiteral)) return false;
        DecimaltalLiteral object = (DecimaltalLiteral) obj;
        if (this.value.equals(object.getValue())) {
            return true;
        }

        return false;
    }
}
