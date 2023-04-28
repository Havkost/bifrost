package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.AST;
import ASTVisitor.Parser.Visitor;

import java.util.Objects;

public class FieldDclNode extends AST {

    private String id;
    private AST value;

    private String parentId;

    public FieldDclNode(String id, AST value, int line) {
        super(line);
        this.id = id;
        this.value = value;
    }

    public FieldDclNode(String id, AST value) {
        super(0);
        this.id = id;
        this.value = value;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getId() {
        return id;
    }

    public AST getValue() {
        return value;
    }

    public String getParentId() {
        return parentId;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof FieldDclNode object)) return false;
        return this.id.equals(object.getId()) && Objects.equals(this.parentId, object.getParentId()) && this.value.equals(object.getValue());
    }

    @Override
    public String toString() {
        return "FieldDclNode{" +
                "id='" + id + '\'' +
                ", value=" + value +
                ", parentId='" + parentId + '\'' +
                ", type=" + type +
                '}';
    }
}
