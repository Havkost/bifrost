package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.AST;
import ASTVisitor.Parser.Visitor;

public class HeltalDcl extends SymDeclaring {

    public HeltalDcl(AST value, String id) {
        this.id = id;
        this.value = value;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }

    @Override
    public String toString() {
        return "HeltalDcl{" +
                "id='" + id + '\'' +
                ", value=" + value +
                '}';
    }
}
