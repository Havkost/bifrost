package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.AST;
import ASTVisitor.Parser.Visitor;

public class IdNode extends AST {

    private String id;
    private String parentId;


    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }

    public IdNode(String id, int line) {
        super(line);
        this.id = id;
    }

    public IdNode(String name) {
        super(0);
        this.id = name;
    }

    // FIELD CONSTRUCTORS
    public IdNode(String id, String parentId, int line) {
        super(line);
        this.id = id;
        this.parentId = parentId;
    }

    public IdNode(String name, String parentId) {
        super(0);
        this.id = name;
        this.parentId = parentId;
    }

    public String getId() {
        return id;
    }

    public String getParentId() {
        return parentId;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof IdNode object)) return false;
        return this.id.equals(object.getId());
    }

    @Override
    public String toString() {
        return "IdNode{" +
                "name='" + id + '\'' +
                '}';
    }
}
