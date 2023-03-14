package ASTVisitor.Parser;

public class Computing extends AST {
    String operation;
    AST child1;
    AST child2;

    public Computing(String operation, AST child1, AST child2) {
        this.operation = operation;
        this.child1 = child1;
        this.child2 = child2;
    }

    public void accept(Visitor v) {v.visit(this);}
}
