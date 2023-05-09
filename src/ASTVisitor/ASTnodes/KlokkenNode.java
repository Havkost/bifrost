package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.AST;
import ASTVisitor.Parser.Visitor;

public class KlokkenNode extends AST {

    public KlokkenNode(int line) {
        super(line);
        this.type = DataTypes.TID;
    }

    public KlokkenNode() {
        super(0);
    }
    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof KlokkenNode object;
    }
}
