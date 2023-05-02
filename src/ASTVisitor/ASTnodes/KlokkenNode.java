package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.AST;
import ASTVisitor.Parser.Visitor;

public class KlokkenNode extends AST {

    private String value;

    public KlokkenNode(String value, int line) {
        super(line);
        this.value = value;
        this.type = DataTypes.KLOKKEN;
    }

    public KlokkenNode(String value) {
        super(0);
        this.value = value;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
