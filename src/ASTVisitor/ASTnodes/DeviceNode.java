package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.AST;
import ASTVisitor.Parser.Visitor;

import java.util.List;

public class DeviceNode extends AST {

    private String id;

    private List<FieldDclNode> fields;
    private String endpoint;

    public DeviceNode(String id, List<FieldDclNode> fields, String endpoint, int line) {
        super(line);
        this.id = id;
        this.fields = fields;
        this.endpoint = endpoint;
    }

    public String getId() {
        return id;
    }

    public List<FieldDclNode> getFields() {
        return fields;
    }

    public String getEndpoint() {
        return endpoint;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
