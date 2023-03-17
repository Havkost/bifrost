package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.AST;
import ASTVisitor.Parser.Visitor;

public class PrintNode extends AST {

    private String val;

    private AST id;

    public String getVal() {
        return val;
    }

    public AST getId() {
        return id;
    }

    public PrintNode(String val, AST id) {
        this.val = val;
        this.id = id;
    }

    @Override
    public void accept(Visitor v) {
    }
}
