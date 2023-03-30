package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.AST;
import ASTVisitor.Parser.Visitor;

import java.util.ArrayList;

public class FuncDclNode extends AST {

    private String id;
    private ArrayList<AST> body;

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }

    public FuncDclNode(String id, ArrayList<AST> body) {
        this.id = id;
        this.body = body;
    }

    public String getId() {
        return id;
    }

    public ArrayList<AST> getBody() {
        return body;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof FuncDclNode)) return false;
        FuncDclNode object = (FuncDclNode) obj;
        if (this.id.equals(object.getId()) && this.body.equals(object.getBody())) {
            return true;
        }

        return false;
    }
}
