package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.AST;
import ASTVisitor.Parser.Visitor;

public class PrintNode extends AST {

    private AST value;

    public AST getValue() {
        return value;
    }

    public PrintNode(AST value) {
        this.value = value;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }

    @Override
    public String toString() {
        return "PrintNode{" +
                "value=" + value +
                '}';
    }
}
