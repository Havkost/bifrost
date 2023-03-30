package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.AST;
import ASTVisitor.Parser.Visitor;

public class BoolskDcl extends SymDeclaring {

    public BoolskDcl(AST value, String id) {
        this.value = value;
        this.id = id;
    }

    public void accept(Visitor v){
        v.visit(this);
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
}
