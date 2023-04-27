package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.AST;
import ASTVisitor.Parser.Visitor;

public class LoopNode extends AST {

    private String id;
    private AST repeatCount;

    public LoopNode(String id, AST repeatCount, int line) {
        super(line);
        this.id = id;
        this.repeatCount = repeatCount;
    }

    public LoopNode(String id, AST repeatCount) {
        super(0);
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

    public AST getRepeats() {
        return repeatCount;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof LoopNode)) return false;
        LoopNode object = (LoopNode) obj;
        if (this.id.equals(object.getId()) && this.repeatCount.equals(object.getRepeats())) {
            return true;
        }
        return false;
    }
}
