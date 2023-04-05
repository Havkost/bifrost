package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.AST;
import ASTVisitor.Parser.Visitor;

public class BoolskDcl extends AST {

    private String id;
    private AST value;

    public BoolskDcl(AST value, String id) {
        this.value = value;
        this.id = id;
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
    public boolean equals(Object obj) {
        if(!(obj instanceof BoolskDcl)) return false;
        BoolskDcl object = (BoolskDcl) obj;
        if (this.id.equals(object.getId()) && this.value.equals(object.getValue())) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "BoolskDcl{" +
                "id='" + id + '\'' +
                ", value=" + value +
                '}';
    }
}
