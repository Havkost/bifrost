package ASTVisitor.Parser;

import ASTVisitor.ASTnodes.*;

public class CCodeGenerator extends Visitor {

    private String code = "";

    public void emit(String c){
        code = code + c;
    }

    @Override
    void visit(AssignNode n) {
        // TODO Auto-generated method stub
        emit(n.d + " = ");
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

        emit("#include <stdio.h>\n\n");
        emit("int main() {\n");
        for(AST ast : n.getChild()){
            ast.accept(this);
        };
        emit("return 0;");
        emit("\n}");

        System.out.println(code);

    }

    @Override
    void visit(FuncDclNode n) {
        emit("" + getFuncId);
    }

    @Override
    void visit(SymDeclaring n) {
        // TODO Auto-generated method stub

    }

}

