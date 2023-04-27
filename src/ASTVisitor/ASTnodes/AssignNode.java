package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.AST;
import ASTVisitor.Parser.Visitor;

public class AssignNode extends AST {

    private AST id;
    private AST value;

    public AssignNode(IdNode id, AST value, int line) {
        super(line);
        this.id = id;
        this.value = value;
    }

    public AssignNode(FieldNode id, AST value, int line) {
        super(line);
        this.id = id;
        this.value = value;
    }

    public void setValue(AST value) {
        this.value = value;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }

    public AST getId() {
        return id;
    }

    public AST getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "AssignNode{" +
                "id='" + id + '\'' +
                ", val=" + value +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof AssignNode)) return false;
        AssignNode object = (AssignNode) obj;
        if (this.id.equals(object.getId()) && this.value.equals(object.getValue())) {
            return true;
        }

        return false;
    }
}
