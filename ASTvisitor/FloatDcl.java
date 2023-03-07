package ASTvisitor;

import ASTvisitor.SymDeclaring;
import ASTvisitor.Visitor;

public class FloatDcl extends SymDeclaring {
    FloatDcl(String i) { id = i; }

    public void accept(Visitor v) {
        v.visit(this);
    }
}
