package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.AST;
import ASTVisitor.Parser.Visitor;

public class KlokkenNode extends AST {

    private String value;

    public KlokkenNode(String value, int line) {
        super(line);
        this.value = value;
        this.type = DataTypes.TID;
    }

    public KlokkenNode(String value) {
        super(0);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof KlokkenNode object)) return false;
        return this.value.equals(object.getValue());
    }
}
