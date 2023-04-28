package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.AST;
import ASTVisitor.Parser.Visitor;

import java.util.List;


public class DeviceNode extends AST {

    private String id;

    private List<VariableDcl> fields;
    private String endpoint;

    public DeviceNode(String id, List<VariableDcl> fields, String endpoint, int line) {
        super(line);
        this.id = id;
        this.fields = fields;
        this.endpoint = endpoint;
    }

    public DeviceNode(String id, List<VariableDcl> fields, String endpoint) {
        super(0);
        this.id = id;
        this.fields = fields;
        this.endpoint = endpoint;
    }

    public String getId() {
        return id;
    }

    public List<VariableDcl> getFields() {
        return fields;
    }

    public String getEndpoint() {
        return endpoint;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DeviceNode object)) return false;
        return this.id.equals(object.getId()) && this.fields.equals(object.getFields()) && this.endpoint.equals(object.getEndpoint());
    }

    @Override
    public String toString() {
        return "DeviceNode{" +
                "id='" + id + '\'' +
                ", fields=" + fields +
                ", endpoint='" + endpoint + '\'' +
                ", type=" + type +
                '}';
    }
}
