package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.AST;
import ASTVisitor.Parser.Visitor;

public class LoopNode extends AST {

    private AST child1;
    private HeltalLiteral repeats;

    public LoopNode(AST body, HeltalLiteral repeats) {
        this.child1 = body;
        this.repeats = repeats;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }

    public AST getChild1() {
        return child1;
    }

    public HeltalLiteral getRepeats() {
        return repeats;
    }
}
