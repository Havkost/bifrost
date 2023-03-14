package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.AST;
import ASTVisitor.Parser.Visitor;

public class VarDclNode extends AST {

    private String type;
    private String value;
    private String id;

    @Override
    public void accept(Visitor v) {

    }
}
