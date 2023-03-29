package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.AST;
import ASTVisitor.Parser.Visitor;

public class ConvertToFloat extends AST {

    private AST child;

    public ConvertToFloat(AST n){
        this.child = n;
    }

    @Override
    public void accept(Visitor v){v.visit(this);
    }

    public AST getChild() {
        return child;
    }

    @Override
    public String toString() {
        return child.toString();
    }
}