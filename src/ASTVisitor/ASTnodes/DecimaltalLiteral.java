package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.AST;
import ASTVisitor.Parser.Visitor;

import java.lang.management.ThreadInfo;

public class DecimaltalLiteral extends AST {

    private String value;

    public DecimaltalLiteral(String value, int line) {
        super(line);
        this.value = value;
        this.type = DataTypes.DECIMALTAL;
    }

    public DecimaltalLiteral(String value) {
        super(0);
        this.value = value;
        this.type = DataTypes.DECIMALTAL;
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
