package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.AST;
import ASTVisitor.Parser.Visitor;

public class DecimaltalLiteral extends AST {

    private String value;

    public DecimaltalLiteral(String value, int line) {
        super(line);
        this.value = value;
    }

    public DecimaltalLiteral(String value) {
        super(0);
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
        if (!(obj instanceof DecimaltalLiteral object)) return false;
        return this.value.equals(object.getValue());
    }
    public String toString() {
        return value;
    }
}
