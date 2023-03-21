package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.Visitor;

public class TekstDcl extends SymDeclaring {


    public TekstDcl(String i) {
        id = i;
    }
    public void accept(Visitor v){v.visit(this);}

}
