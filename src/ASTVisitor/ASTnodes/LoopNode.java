package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.AST;
import ASTVisitor.Parser.Visitor;

public class LoopNode extends AST {

    private String id;
    private HeltalLiteral repeatCount;

    public LoopNode(String id, HeltalLiteral repeatCount) {
        this.id = id;
        this.repeatCount = repeatCount;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }

    public String getId() {
        return id;
    }

    public HeltalLiteral getRepeats() {
        return repeatCount;
    }
}
