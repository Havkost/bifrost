package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.AST;
import ASTVisitor.Parser.Visitor;

public class LoopNode extends AST {

    private AST body;
    private HeltalLiteral repeats;


    public LoopNode(AST body, HeltalLiteral repeats) {
        this.body = body;
        this.repeats = repeats;
    }

    @Override
    public void accept(Visitor v) {

    }

    public AST getBody() {
        return body;
    }

    public HeltalLiteral getRepeats() {
        return repeats;
    }
}
