package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.AST;
import ASTVisitor.Parser.Visitor;

public class FuncDclNode extends AST {

    private String id;
    private AST child1;

    @Override
    public void accept(Visitor v) {

    }
}
