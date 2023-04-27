package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.AST;
import ASTVisitor.Parser.Visitor;

public class PrintNode extends AST {

    private AST value;

    public AST getValue() {
        return value;
    }

    public PrintNode(AST value, int line) {
        super(line);
        this.value = value;
    }

    public PrintNode(AST value) {
        super(0);
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

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PrintNode)) return false;
        PrintNode object = (PrintNode) obj;
        if (this.value.equals(object.getValue())) {
            return true;
        }
        return false;
    }
}
