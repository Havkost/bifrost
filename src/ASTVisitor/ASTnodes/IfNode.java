package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.AST;
import ASTVisitor.Parser.Visitor;

import java.util.ArrayList;

public class IfNode extends AST {

    private AST expr;
    private ArrayList<AST> children;


    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }

    public IfNode(AST expr, ArrayList<AST> children) {
        this.expr = expr;
        this.children = children;
    }

    public AST getExpr() {
        return expr;
    }

    public ArrayList<AST> getChildren() {
        return children;
    }

    @Override
    public String toString() {
        return "IfNode{" +
                "expr=" + expr +
                ", children=" + children +
                '}';
    }
}
