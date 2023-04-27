package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.AST;
import ASTVisitor.Parser.Visitor;

public class IdNode extends AST {

    private String name;

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }

    public IdNode(String name, int line) {
        super(line);
        this.name = name;
    }

    public IdNode(String name) {
        super(0);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof IdNode object)) return false;
        return this.name.equals(object.getName());
    }

    @Override
    public String toString() {
        return "IdNode{" +
                "name='" + name + '\'' +
                '}';
    }
}
