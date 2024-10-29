package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.AST;
import ASTVisitor.Parser.Visitor;

public class CommentNode extends AST {
    private String value;
    public CommentNode(String value, int line) {
        super(line);
        this.value = value;
    }

    public CommentNode(String value) {
        super(0);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
