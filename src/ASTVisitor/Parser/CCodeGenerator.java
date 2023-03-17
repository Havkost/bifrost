package ASTVisitor.Parser;

import ASTVisitor.ASTnodes.*;

public class CCodeGenerator extends Visitor {

    private String code = "";

    public void emit(String c){
        code = code + c;
    }

    @Override
    public void visit(AssignNode n) {
        // TODO Auto-generated method stub
        emit(n.getId() + " = ");
        n.getVal().accept(this);
        emit(";\n");
    }

    @Override
    public void visit(UnaryComputing n) {
        //TODO
    }

    @Override
    public void visit(SymReferencing n) {
        //TODO
    }

    @Override
    public void visit(BinaryComputing n) {
        // TODO Auto-generated method stub
        n.getChild1().accept(this);
        emit(" " + n.getOperation() + " ");
        n.getChild2().accept(this);
    }

    @Override
    public void visit(IfNode n) {
        emit("if (" + n.getChild1() + ") {\n");
        n.getChild2().accept(this);
        emit("}");
    }

    @Override
    public void visit(LoopNode n) {
        emit("gentag");
        n.getChild1().accept(this);
        n.getRepeats().accept(this);
        emit("gange");
    }

    public void visit(BoolskLiteral n) {
        n.accept(this);
    }

    @Override
    public void visit(ProgramNode n) {
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

    public void visit(FuncNode n) {
        emit(n.getId() + "();");
    }

    @Override
    public void visit(FuncDclNode n) {
        emit("void " + n.getId() + "() {\n");
        emit("  " + n.getChild1() + "\n");
        emit("}\n");
    }

    @Override
    public void visit(SymDeclaring n) {
        // TODO Auto-generated method stub
    }
}

