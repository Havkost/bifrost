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

    public ArrayList<AST> getChildren() {
        return body;
    }

    public void setExpr(AST expr) {
        this.expr = expr;
    }

    public void setChildren(ArrayList<AST> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "IfNode{" +
                "expr=" + expr +
                ", children=" + body +
                '}';
    }
}
