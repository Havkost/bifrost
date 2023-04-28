package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.AST;
import ASTVisitor.Parser.Visitor;

public class DecimaltalDcl extends VariableDcl {

    private String id;
    private AST value;
    private String parentId;

    public DecimaltalDcl(AST value, String id, int line) {
        super(value, id, line);
    }

    public DecimaltalDcl(AST value, String id) {
        super(value, id);
    }

    // FIELD CONSTRUCTORS
    public DecimaltalDcl(AST value, String id, String parentId, int line) {
        super(value, id, parentId, line);
    }

    public DecimaltalDcl(AST value, String id, String parentId) {
        super(value, id, parentId);
    }

    public void accept(Visitor v) {
        v.visit(this);
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof DecimaltalDcl)) return false;
        DecimaltalDcl object = (DecimaltalDcl) obj;
        if (this.getId().equals(object.getId()) && this.getValue().equals(object.getValue())) {
            return true;
        }
        return false;
    }

}
