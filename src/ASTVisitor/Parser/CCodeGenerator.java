package ASTVisitor.Parser;

import ASTVisitor.ASTnodes.*;

import ASTVisitor.Parser.AST.Operators;
import java.util.HashMap;
import java.util.Map;
import static java.util.Map.entry;
import static ASTVisitor.Parser.AST.DataTypes.*;

public class CCodeGenerator extends Visitor {
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
            entry(BOOLSK, "boolean")
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
            //emit("%d"); // TODO: Use symbol table to look up type when the table has been constructed
        } else {
           emit(formatStrings.get(n.getValue().type));
        }
        emit("\\n\", (");

        n.getValue().accept(this);
        if(n.getValue().type == BOOLSK) emit(")? \"Sandt\" : \"Falsk\"");
        emit(");");
    }

    @Override
    public void visit(ProgramNode n) {
        emit("#include <string.h>\n");
        emit("#include <stdlib.h>\n");
        emit("#include <stdio.h>\n\n");

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

        System.out.println(code);
    }

    @Override
    public void visit(SymDeclaring n) {
    }



    @Override
    public void visit(TypeNode n) {
        emit(n.getName());
    }

    @Override
    public void visit(UnaryComputing n) {
        emit("!");
        //emit(operators.get(n.getOperation()));
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
        // TODO: It already does this, but how? - Jack
        /** We assume here, that we are going to use a boolean library
        emit ("boolean " + n.getId) + " = ");
         n.getValue().accept(this);;
         emit(";");
        */
    }

    @Override
    public void visit(ConvertToFloat n) {
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

