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
        emit(n.getId() + " = ");
        n.getVal().accept(this);
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
    void visit(IfNode n) {
        emit("if (" + n.getChild1() + ") {\n");
        n.getChild2().accept(this);
        emit("}");
    }

    @Override
    void visit(LoopNode n) {
        emit("gentag");
        n.getChild1().accept(this);
        n.getRepeats().accept(this);
        emit("gange");
    }

    void visit(BoolskLiteral n) {
        n.getVal().accept(this);
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

    void visit(FuncNode n) {
        emit(n.getId() + "();");
    }

    @Override
    void visit(FuncDclNode n) {
        emit("void " + n.getId() + "() {\n");
        emit("  " + n.getChild1() + "\n");
        emit("}\n");
    }

    @Override
    void visit(SymDeclaring n) {
        // TODO Auto-generated method stub
    }
}

