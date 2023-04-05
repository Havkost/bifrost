package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.AST;
import ASTVisitor.Parser.Visitor;

public class HeltalDcl extends AST{

    private String id;
    private AST value;

    public HeltalDcl(AST value, String id) {
        this.id = id;
        this.value = value;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }

    public String getId() {
        return id;
    }

    public AST getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "HeltalDcl{" +
                "id='" + id + '\'' +
                ", value=" + value +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof HeltalDcl)) return false;
        HeltalDcl object = (HeltalDcl) obj;
        if (this.id.equals(object.getId()) && this.value.equals(object.getValue())) {
            return true;
        }
        return false;
    }
}
