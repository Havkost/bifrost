package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.AST;
import ASTVisitor.Parser.Visitor;

public class VarDclNode extends AST {

    private String type;
    private String value;
    private String id;

    @Override
    public void accept(Visitor v) {

    }

    public VarDclNode(String type, String value, String id) {
        this.type = type;
        this.value = value;
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public String getId() {
        return id;
    }
}
