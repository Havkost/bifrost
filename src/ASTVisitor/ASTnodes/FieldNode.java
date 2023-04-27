package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.AST;
import ASTVisitor.Parser.Visitor;

public class FieldNode extends AST {

    private String id;
    private String parentId;

    public FieldNode(String id, String parentId, int line) {
        super(line);
        this.id = id;
        this.parentId = parentId;
    }

    public FieldNode(String id, String parentId) {
        super(0);
        this.id = id;
        this.parentId = parentId;
    }

    public String getId() {
        return id;
    }

    public String getParentId() {
        return parentId;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
