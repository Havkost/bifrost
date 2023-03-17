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
    public void visit(BinaryComputing n) {
        // TODO Auto-generated method stub
        n.getChild1().accept(this);
        emit(" " + n.getOperation() + " ");
        n.getChild2().accept(this);
    }

    @Override
    void visit(BoolskLiteral n) {
        emit( " " + n.getVal()  + " ");
    }

    @Override
    void visit(DecimaltalLiteral n) {
        emit(" " + n.getVal() + " ");
    }

    @Override
    public void visit(FuncDclNode n) {
        emit("void " + n.getId() + "() {\n");
        emit("    ");
        n.getChild1().accept(this);
        emit("\n");
        emit("}\n");
    }
    @Override
    void visit(FuncNode n) {
        emit(n.getId() + "();");
    }

    @Override
    void visit(HeltalLiteral n) {
        emit(" " + n.getVal() + " ");
    }

    @Override
    void visit(IdNode n) {
        emit(" " + n.getName() + " ");
    }

    @Override
    void visit(IfNode n) {
        emit("if (" );
        n.getChild1().accept(this);
        emit( ") {\n");
        n.getChild2().accept(this);
        emit("\n}");
    }

    @Override
    void visit(LoopNode n) {
        emit("for(int i = 0; i < " + n.getRepeats() + "; i++) { \n");
        n.getChild1().accept(this);
        emit("\n }");
    }

    void visit(PrintNode n) {
        // TODO: NÅR VI HAR LAVET SYMBOL TABLE SKAL VI HAVE LAVET DENNE.
        emit("printf(\"%s\",");
        n.getId().accept(this);
        emit(")");
    }

    @Override
    void visit(ProgramNode n) {
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
    public void visit(SymDeclaring n) {
        // TODO: MAKE THIS
    }

    @Override
    void visit(TekstLiteral n) {
        emit(" " + n.getValue() + " ");
    }

    @Override
    void visit(TypeNode n) {
        emit(" " + n.getName() + " ");
    }

    @Override
    void visit(UnaryComputing n) {
        emit("!");
        n.getChild().accept(this);
    }

    @Override
    void visit(VarDclNode n) {
        n.getType().accept(this);
        emit(" " + n.getId() + " = " + n.getValue() + ";");
    }
}

