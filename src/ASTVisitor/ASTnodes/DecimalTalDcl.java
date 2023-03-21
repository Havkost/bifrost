package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.Visitor;

public class DecimalTalDcl extends SymDeclaring {


    public DecimalTalDcl(String i) {
        id = i;
    }

    public void accept(Visitor v){v.visit(this);}

}
