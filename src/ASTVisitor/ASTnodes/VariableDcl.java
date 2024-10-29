package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.AST;
import ASTVisitor.Parser.Visitor;

public abstract class VariableDcl extends AST {

    private final AST value;
    private IdNode id;


    public VariableDcl(AST value, IdNode id, int line) {
        super(line);
        this.id = id;
        this.value = value;
    }

    public VariableDcl(AST value, IdNode id) {
        super(0);
        this.id = id;
        this.value = value;
    }

    public IdNode getId() {
        return id;
    }

    public AST getValue() {
        return value;
    }
}
