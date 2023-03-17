package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.AST;
import ASTVisitor.Parser.Visitor;

public class FuncNode extends AST {

    private String id;

    public FuncNode(String id) {
        this.id = id;
    }

    @Override
    public void accept(Visitor v) {

    }

    public String getId() {
        return id;
    }
}
