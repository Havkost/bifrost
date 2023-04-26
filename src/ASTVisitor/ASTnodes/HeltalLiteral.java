package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.AST;
import ASTVisitor.Parser.Visitor;

public class HeltalLiteral extends AST {

    private String value;

    public HeltalLiteral(String value, int line) {
        super(line);
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
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
         if(!(obj instanceof HeltalLiteral)) return false;
        HeltalLiteral object = (HeltalLiteral) obj;
        if(this.value.equals(object.getValue())) return true;

        return false;
    }
}
