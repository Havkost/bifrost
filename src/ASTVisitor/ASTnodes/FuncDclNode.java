package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.AST;
import ASTVisitor.Parser.Visitor;
import java.util.List;

public class FuncDclNode extends AST {

    private String id;
    private List<AST> body;

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }

    public FuncDclNode(String id, List<AST> body, int line) {
        super(line);
        this.id = id;
        this.body = body;
    }

    public FuncDclNode(String id, List<AST> body) {
        super(0);
        this.id = id;
        this.body = body;
    }

    public String getId() {
        return id;
    }

    public List<AST> getBody() {
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
