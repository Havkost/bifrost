package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.AST;
import ASTVisitor.Parser.Visitor;

import java.util.List;

public class IfNode extends AST {

    private AST expr;
    private List<AST> body;

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }

    public IfNode(AST expr, List<AST> children) {
        this.expr = expr;
        this.body = children;
    }

    public AST getExpr() {
        return expr;
    }

    public List<AST> getBody() {
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
