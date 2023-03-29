package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.AST;
import ASTVisitor.Parser.Visitor;

import java.util.ArrayList;

public class IfNode extends AST {

    private AST expr;
    private ArrayList<AST> body;

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }

    public IfNode(AST expr, ArrayList<AST> children) {
        this.expr = expr;
        this.body = children;
    }

    public AST getExpr() {
        return expr;
    }

    public ArrayList<AST> getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "IfNode{" +
                "expr=" + expr +
                ", children=" + body +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof IfNode)) return false;
        IfNode object = (IfNode) obj;
        if (this.expr.equals(object.getExpr()) && this.body.equals(object.getBody())) {
            return true;
        }

        return false;
    }
}
