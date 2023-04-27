package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.AST;
import ASTVisitor.Parser.Visitor;

public class FuncNode extends AST {

    private String id;

    public FuncNode(String id, int line) {
        super(line);
        this.id = id;
    }

    public FuncNode(String id) {
        super(0);
        this.id = id;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof FuncNode)) return false;
        FuncNode object = (FuncNode) obj;
        if (this.id.equals(object.getId())) {
            return true;
        }
        return false;
    }
}
