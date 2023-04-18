package ASTVisitor.Parser;

import ASTVisitor.ASTnodes.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import static ASTVisitor.Parser.AST.Operators;
import static java.util.Map.entry;
import static ASTVisitor.Parser.AST.DataTypes.*;

public class CCodeGenerator extends Visitor {

    public CCodeGenerator(boolean print) {
        this.print = print;
    }

    public CCodeGenerator(boolean print, FileWriter writer) {
        this.print = print;
        this.writer = writer;
    }

    private final boolean print;
    private FileWriter writer;

    int blockIndent = 0;
    private String code = "";

    Map<AST.DataTypes, String> formatStrings = new HashMap<>(Map.ofEntries(
            entry(HELTAL, "%d"),
            entry(DECIMALTAL, "%lf"),
            entry(TEKST, "%s"),
            entry(BOOLSK, "%s")
    ));

    Map<AST.DataTypes, String> dataTypeString = new HashMap<>(Map.ofEntries(
            entry(HELTAL, "int"),
            entry(DECIMALTAL, "double"),
            entry(TEKST, "char*"),
            entry(BOOLSK, "bool")
    ));

    public void emit(String c){
        code = code + c;
    }

    @Override
    public void visit(AssignNode n) {
        emit(n.getId() + " = ");
        n.getValue().accept(this);
        emit(";");
    }

    @Override
    public void visit(BinaryComputing n) {
        n.getChild1().accept(this);
         emit(" " + n.getOperation().Cversion + " ");
        n.getChild2().accept(this);
    }

    @Override
    public void visit(BoolskLiteral n) {
        emit(n.getValue().equals("sandt") ? "true" : "false");
    }

    @Override
    public void visit(DecimaltalLiteral n) {
        emit(n.getValue().replace(',', '.'));
    }

    @Override
    public void visit(TekstLiteral n) {
        emit("\"" + n.getValue() + "\"");
    }

    @Override
    public void visit(HeltalLiteral n) {
        emit(n.getValue());
    }

    @Override
    public void visit(FuncDclNode n) {
        emit("void " + n.getId() + "() {");
        blockIndent++;
        for (AST stmt : n.getBody()) {
            emit("\n");
            indent(blockIndent);
            stmt.accept(this);
        }
        emit("\n");
        blockIndent--;
        indent(blockIndent);
        emit("}");
    }

    @Override
    public void visit(FuncNode n) {
        emit(n.getId() + "();");
    }

    @Override
    public void visit(IdNode n) {
        emit(n.getName());
    }

    @Override
    public void visit(IfNode n) {
        emit("if (" );
        n.getExpr().accept(this);
        emit( ") {");
        blockIndent++;
        for (AST child : n.getBody()) {
            emit("\n");
            indent(blockIndent);
            child.accept(this);
        }
        blockIndent--;
        emit("\n");
        indent(blockIndent);
        emit("}");
    }

    @Override
    public void visit(LoopNode n) {
        emit("for(int __i = 0; __i < (");
        n.getRepeats().accept(this);
        emit("); __i++) {\n");
        blockIndent++;
        indent(blockIndent);
        emit(n.getId() + "();\n");
        blockIndent--;
        indent(blockIndent);
        emit("}");
    }

    public void visit(PrintNode n) {
        emit("printf(\"");
        if(n.getValue() instanceof IdNode) {
            emit(formatStrings.get(AST.SymbolTable.get(((IdNode) n.getValue()).getName())));
        } else {
           emit(formatStrings.get(n.getValue().type));
        }
        emit("\\n\", ");

        n.getValue().accept(this);
        if(n.getValue().type == BOOLSK) emit("? \"Sandt\" : \"Falsk\"");
        emit(");");
    }

    @Override
    public void visit(ProgramNode n) {
        emit("#include <string.h>\n");
        emit("#include <stdlib.h>\n");
        emit("#include <stdio.h>\n");
        emit("#include <stdbool.h>\n");
        emit("\n");

        AST.getSymbolTable().forEach((id, type) -> {
            if(type == RUTINE) emit("void " + id + "();\n");
            else emit(dataTypeString.get(type) + " " + id + ";\n");
        });

        emit("\nint free_memory () {\n");
        blockIndent++;
        AST.getSymbolTable().forEach((id, type) -> {
            if(type == TEKST) {
                indent(blockIndent);
                emit("free(" + id + ");\n");
            }
        });
        blockIndent--;
        emit("}\n\n");

        emit("int main() {\n");
        blockIndent++;
        for(AST ast : n.getChild()){
            if(!(ast instanceof FuncDclNode)) {
                indent(blockIndent);
                ast.accept(this);
                emit("\n");
            }
        }
        indent(blockIndent);
        emit("free_memory();\n");
        indent(blockIndent);
        emit("return 0;");
        blockIndent--;
        emit("\n");
        indent(blockIndent);
        emit("}\n\n");

        for (AST ast : n.getChild()) {
            if(ast instanceof FuncDclNode) {
                indent(blockIndent);
                ast.accept(this);
                emit("\n");
            }
        }

        if (print) System.out.println(code);
        try {
            writer.write(code);
            writer.close();
        } catch (IOException e) {
            System.out.println("[FEJL] Kunne ikke skrive til filen " + writer);
        }

    }

    @Override
    public void visit(UnaryComputing n) {
        if (n.getOperation().equals(Operators.PAREN)) {
            emit("(");
            n.getChild().accept(this);
            emit(")");
            return;
        }
        emit(n.getOperation().Cversion);
        n.getChild().accept(this);
    }

    @Override
    public void visit(TekstDcl n) {
        if(n.getValue() instanceof TekstLiteral) {
            emit(n.getId() + " = malloc(" +
                    (((TekstLiteral) n.getValue()).getValue().length()+1) +
                    " * sizeof(char));\n");
            indent(blockIndent);
            emit("strcpy(" + n.getId() + ", ");
            n.getValue().accept(this);
            emit(");");
        }
    }

    @Override
    public void visit(HeltalDcl n) {
        emit(n.getId() + " = ");
        n.getValue().accept(this);
        emit(";");
    }

    @Override
    public void visit(DecimaltalDcl n) {
        emit(n.getId() + " = ");
        n.getValue().accept(this);
        emit(";");
    }

    @Override
    public void visit(BoolskDcl n) {
        emit (n.getId() + " = ");
         n.getValue().accept(this);
         emit(";");
    }

    public void indent(int indents) {
        for (int i = 0; i < indents; i++) {
            emit("    ");
        }
    }

    public String getCode() {
        return code;
    }
}

