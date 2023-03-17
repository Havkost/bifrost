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
    }

    public String getVal() {
        return val;
    }
}
