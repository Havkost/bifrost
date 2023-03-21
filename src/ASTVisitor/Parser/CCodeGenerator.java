package ASTVisitor.Parser;

import ASTVisitor.ASTnodes.*;
// TODO: Mangler newlines, altså en solid formatting
public class CCodeGenerator extends Visitor {

    private String code = "";

    public void emit(String c){
        code = code + c;
    }

    @Override
    public void visit(AssignNode n) {
        emit(n.getId() + " = ");
        n.getVal().accept(this);
        emit(";\n");
    }

    @Override
    public void visit(BinaryComputing n) {
        n.getChild1().accept(this);
        emit(" " + n.getOperation() + " ");
        n.getChild2().accept(this);
    }

    @Override
    public void visit(BoolskLiteral n) {
        emit( " " + n.getVal()  + " ");
    }

    @Override
    public void visit(DecimaltalLiteral n) {
        emit(" " + n.getVal() + " ");
    }

    @Override
    public void visit(FuncDclNode n) {
        emit("void " + n.getId() + "() {\n");
        emit("\t");
        n.getChild1().accept(this);
        emit("\n");
        emit("}\n");
    }

    @Override
    public void visit(FuncNode n) {
        emit(n.getId() + "();");
    }

    @Override
    public void visit(HeltalLiteral n) {
        emit(" " + n.getVal() + " ");
    }

    @Override
    public void visit(IdNode n) {
        emit(" " + n.getName() + " ");
    }

    @Override
    public void visit(IfNode n) {
        emit("if (" );
        n.getExpr().accept(this);
        emit( ") {\n");
        for (AST child : n.getChildren()) {
            child.accept(this);
        }
        emit("\n}");
    }

    @Override
    public void visit(LoopNode n) {
        // TODO: Overvej hvad variablen skal hedde i forloops
        emit("for(int __ = 0; __ < " + n.getRepeats() + "; __++) { \n");
        n.getChild1().accept(this);
        emit("\n }");
    }

    public void visit(PrintNode n) {
        // TODO: NÅR VI HAR LAVET SYMBOL TABLE SKAL VI HAVE LAVET DENNE.
        emit("printf(\"%s\", ");
        n.getId().accept(this);
        emit(");");
    }

    @Override
    public void visit(ProgramNode n) {
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
    public void visit(TekstLiteral n) {
        emit(" " + n.getVal() + " ");
    }

    @Override
    public void visit(TypeNode n) {
        emit(" " + n.getName() + " ");
    }

    @Override
    public void visit(UnaryComputing n) {
        // TODO: Hvis flere operatorer der er unær, så skriv til en getOperator i stedet.
        emit("!");
        n.getChild().accept(this);
    }

    @Override
    public void visit(TekstDcl n) {

    }

    @Override
    public void visit(HeltalDcl n) {

    }

    @Override
    public void visit(DecimaltalDcl n) {

    }

    @Override
    public void visit(BoolskDcl n) {

    }

    @Override
    public void visit(SymReferencing n) {

    }
}

