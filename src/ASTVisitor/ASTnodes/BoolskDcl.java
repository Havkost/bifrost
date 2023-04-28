package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.AST;
import ASTVisitor.Parser.Visitor;

public class BoolskDcl extends VariableDcl {

    private String id;
    private AST value;
    private String parentId;

    public BoolskDcl(AST value, String id, int line) {
        super(value, id, line);
    }

    public BoolskDcl(AST value, String id) {
        super(value, id);
    }

    // FIELD CONSTRUCTORS
    public BoolskDcl(AST value, String id, String parentId, int line) {
        super(value, id, parentId, line);
    }

    public BoolskDcl(AST value, String id, String parentId) {
        super(value, id, parentId);
    }

    public void accept(Visitor v) {
        v.visit(this);
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof BoolskDcl)) return false;
        BoolskDcl object = (BoolskDcl) obj;
        if (this.getId().equals(object.getId()) && this.getValue().equals(object.getValue())) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "BoolskDcl{" +
                "id='" + this.getId() + '\'' +
                ", value=" + this.getValue() +
                '}';
    }
}
