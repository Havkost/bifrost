package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.Visitor;

public class HeltalDcl extends SymDeclaring {

    public HeltalDcl(String i) {
        id = i;
    }

    public void accept(Visitor v){v.visit(this);}

}
