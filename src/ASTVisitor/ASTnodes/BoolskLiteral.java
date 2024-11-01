package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.AST;
import ASTVisitor.Parser.Visitor;

public class BoolskLiteral extends AST {

    private String value;

    public BoolskLiteral(String value, int line) {
        super(line);
        this.value = value;
        this.type = DataTypes.BOOLSK;
    }

    public BoolskLiteral(String value) {
        super(0);
        this.value = value;
        this.type = DataTypes.BOOLSK;
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
        if (!(obj instanceof BoolskLiteral)) return false;
        BoolskLiteral object = (BoolskLiteral) obj;
        if (this.value.equals(object.getValue())) {
            return true;
        }

        return false;
    }

    public String toString() {
        return "BoolskLiteral{" +
                "value=" + this.value + "}";
    }
}
