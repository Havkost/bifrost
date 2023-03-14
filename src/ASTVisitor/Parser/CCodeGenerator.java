package ASTVisitor.Parser;

import ASTVisitor.ASTnodes.*;

public class CCodeGenerator extends Visitor {

    private String code = "";

    public void emit(String c){
        code = code + c;
    }

    @Override
    void visit(Assigning n) {
        // TODO Auto-generated method stub
        emit(n.id + " = ");
        n.child1.accept(this);
        emit(";\n");

    }

    @Override
    void visit(BinaryComputing n) {
        // TODO Auto-generated method stub
        n.getChild1().accept(this);
        emit(" " + n.getOperation() + " ");
        n.getChild2().accept(this);


    }

    @Override
    void visit(ProgramNode n) {
        // TODO Auto-generated method stub

        emit("#include < stdio.h>\n\n");
        emit("void main()\n{\n");
        for(AST ast : n.getChild()){
            ast.accept(this);
        };
        emit("return 0;");
        emit("\n}");

        System.out.println(code);

    }

    @Override
    void visit(SymDeclaring n) {
        // TODO Auto-generated method stub

    }

}

