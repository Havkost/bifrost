package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.AST;
import ASTVisitor.Parser.Visitor;

public class DecimaltalDcl extends SymDeclaring {

    public DecimaltalDcl(AST value, String id) {
        this.id = id;
        this.value = value;
    }

    public void accept(Visitor v){
        v.visit(this);
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
