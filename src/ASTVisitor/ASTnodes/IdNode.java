package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.AST;
import ASTVisitor.Parser.Visitor;

public class IdNode extends AST {

    private String value;
    private String parentId;


    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }

    public IdNode(String value, int line) {
        super(line);
        this.value = value;
    }

    public IdNode(String value) {
        super(0);
        this.value = value;
    }

    // FIELD CONSTRUCTORS
    public IdNode(String value, String parentId, int line) {
        super(line);
        this.value = value;
        this.parentId = parentId;
    }

    public IdNode(String value, String parentId) {
        super(0);
        this.value = value;
        this.parentId = parentId;
    }

    public String getValue() {
        return value;
    }

    public String getParentId() {
        return parentId;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof IdNode object)) return false;
        return this.value.equals(object.getValue());
    }

    @Override
    public String toString() {
        return "IdNode{" +
                "name='" + value + '\'' +
                '}';
    }
}
