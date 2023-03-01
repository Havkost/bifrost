package ASTvisitor;

import ASTvisitor.Visitor;

public abstract class AST {
    AST() {
        //CODE HERE EVENTUALLY
    }

    public abstract void accept(Visitor v);
}
