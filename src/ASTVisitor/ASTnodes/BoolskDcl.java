package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.Visitor;

public class BoolskDcl extends SymDeclaring {
    public BoolskDcl(String i) {
        id = i;
    }

    public void accept(Visitor v){v.visit(this);}

}
