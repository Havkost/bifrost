package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.AST;
import ASTVisitor.Parser.Visitor;

public class DecimaltalDcl extends AST {

    private String id;
    private AST value;


    public DecimaltalDcl(AST value, String id, int line) {
        super(line);
        this.id = id;
        this.value = value;
    }

    public DecimaltalDcl(AST value, String id) {
        super(0);
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
    public boolean equals(Object obj) {
        if(!(obj instanceof DecimaltalDcl)) return false;
        DecimaltalDcl object = (DecimaltalDcl) obj;
        if (this.id.equals(object.getId()) && this.value.equals(object.getValue())) {
            return true;
        }
        return false;
    }

}
