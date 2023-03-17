package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.AST;
import ASTVisitor.Parser.Visitor;

public class LoopNode extends AST {

    private AST child1;
    private int repeats;

    public LoopNode(AST body, int repeats) {
        this.child1 = body;
        this.repeats = repeats;
    }

    @Override
    public void accept(Visitor v) {
    }

    public AST getChild1() {
        return child1;
    }

    public int getRepeats() {
        return repeats;
    }
}
