package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.AST;
import ASTVisitor.Parser.Visitor;

public class BoolskLiteral extends AST {

    private String val;

    public BoolskLiteral(String val) {
        this.val = val;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }


    public String getVal() {
        return val;
    }

    @Override
    public String toString() {
        return val;
    }
}
