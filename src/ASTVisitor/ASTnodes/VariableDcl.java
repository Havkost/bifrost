package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.AST;
import ASTVisitor.Parser.Visitor;

public abstract class VariableDcl extends AST {

    protected String id;
    private final AST value;

    private String parentId;

    public VariableDcl(AST value, String id, int line) {
        super(line);
        this.id = id;
        this.value = value;
    }

    public VariableDcl(AST value, String id) {
        super(0);
        this.id = id;
        this.value = value;
    }

    // FIELD CONSTRUCTORS
    public VariableDcl(AST value, String id, String parentId, int line) {
        super(line);
        this.id = id;
        this.value = value;
        this.parentId = parentId;
    }

    public VariableDcl(AST value, String id, String parentId) {
        super(0);
        this.id = id;
        this.value = value;
        this.parentId = parentId;
    }


    public String getId() {
        return id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public AST getValue() {
        return value;
    }
}
