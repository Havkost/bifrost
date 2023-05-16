package ASTVisitor.Parser;

import ASTVisitor.ASTnodes.*;
import ASTVisitor.Exceptions.FileWriterIOException;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ASTVisitor.Parser.AST.*;
import static java.util.Map.entry;
import static ASTVisitor.Parser.AST.DataTypes.*;

public class CCodeGenerator implements Visitor {

    private Map<AST.DataTypes, String> datatypeEnumRepresentationC = Map.ofEntries(
            entry(BOOLSK, "TYPE_BOOL"),
            entry(HELTAL, "TYPE_INTEGER"),
            entry(DECIMALTAL, "TYPE_DOUBLE"),
            entry(TEKST, "TYPE_STRING")
    );

    /**
     * @param print boolean declaring whether the code should be printed in the console.
     */
    public CCodeGenerator(boolean print) {
        this.print = print;
    }

    /**
     * @param print boolean declaring whether the code should be printed in the console.
     * @param writer (Optional) FileWriter to write the code to, if specified
     */
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
            entry(BOOLSK, "%s"),
            entry(TID, "%02d:%02d")
    ));

    Map<AST.DataTypes, String> dataTypeString = new HashMap<>(Map.ofEntries(
            entry(HELTAL, "int"),
            entry(DECIMALTAL, "double"),
            entry(TEKST, "char*"),
            entry(BOOLSK, "bool"),
            entry(TID, "Time")
    ));

    public void emit(String c){
        code = code + c;
    }

    @Override
    public void visit(AssignNode n) {
        String id;
        if (n.getId().getParentId() == null) id = n.getId().getValue();
        else id = n.getId().getParentId() + "." + n.getId().getValue();

        // If the assigned value is a string literal reallocate memory for it
        if (SymbolTable.get(id) != null && SymbolTable.get(id).equals(TEKST) && n.getValue() instanceof TekstLiteral) {
            emit(id + " = ");
            emit("realloc(" + id + ", " + (((TekstLiteral) n.getValue()).getValue().length()+1) + " * sizeof(char));\n");
            indent(blockIndent);
        } // If the value has the type of string, reallocate memory for that
        else if (SymbolTable.get(id) != null && SymbolTable.get(id).equals(TEKST)
                && n.getValue().getType().equals(TEKST)) {
            emit(id + " = ");
            emit("realloc(");
            n.getValue().accept(this);
            emit(");");
            return;
        }

        if (SymbolTable.get(id) != null && SymbolTable.get(id).equals(TEKST)) {
            emit("strcpy(" + id + ", ");
            n.getValue().accept(this);
            emit(");");
        } else {
            emit(id + " = ");
            n.getValue().accept(this);
            emit(";");
        }

        if (n.getId().getParentId() != null) {
            emit("\n");
            indent(blockIndent);
            emit("send_field_to_endpoint("+n.getId().getParentId()+".endpoint__");
            emit(", \""+n.getId().getValue()+"\"");
            if (SymbolTable.get(id) == TEKST)
                emit(", " + id);
            else
                emit(", &"+id);
            emit(", "+datatypeEnumRepresentationC.get(SymbolTable.get(id)));
            emit(");\n");
        }
    }

    @Override
    public void visit(BinaryComputing n) {
        // String concatination
        if(n.getChild1().getType() == TEKST && n.getChild2().getType() == TEKST && n.getOperation() == Operators.PLUS) {
            emit("concat(");
            n.getChild1().accept(this);
            emit(", ");
            n.getChild2().accept(this);
            emit("), customStrLen(");
            n.getChild1().accept(this);
            emit(", ");
            if(n.getChild2() instanceof BinaryComputing)
                n.getChild2().accept(this);
            else {
                emit("strlen(");
                n.getChild2().accept(this);
                emit(")");
            }
            emit(")");
            return;
        } else if (n.getChild1().getType() == TEKST &&
                (n.getOperation() == Operators.EQUALS || n.getOperation() == Operators.NOT_EQUALS)) {
            emit("strcmp(");
            n.getChild1().accept(this);
            emit(", ");
            n.getChild2().accept(this);
            emit(")");
            if (n.getOperation() == Operators.EQUALS)
                emit(" == 0");
            else emit(" == 1");
            return;
        } else if (n.getChild1().getType() == TID) {
            emit("time_compare(");
            n.getChild1().accept(this);
            emit(", ");
            n.getChild2().accept(this);
            emit(")");
            if (n.getOperation() == Operators.EQUALS)
                emit(" == 0");
            else if (n.getOperation() == Operators.NOT_EQUALS)
                emit(" != 0");
            else if (n.getOperation() == Operators.LESS_THAN) {
                emit(" == -1");
            } else emit(" == 1");
            return;
        }
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
    // dot-notation for devices
    public void visit(IdNode n) {
        if (n.getParentId() != null)
            emit(n.getParentId() + "." + n.getValue());
        else emit(n.getValue());
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
        /* Generating a for loop based on the number of repeats with a call to the
        function as the body                                                    */
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
            String id;
            // Generate dot notation if the id is a device field
            if (((IdNode) n.getValue()).getParentId() != null)
                id = ((IdNode) n.getValue()).getParentId() + "." + ((IdNode) n.getValue()).getValue();
            else id = ((IdNode) n.getValue()).getValue();
            emit(formatStrings.get(AST.SymbolTable.get(id)));
        } else {
           emit(formatStrings.get(n.getValue().type));
        }
        emit("\\n\", ");

        if (n.getValue() instanceof TidNode value)
            emit(value.getHour() + ", " + value.getMinute());
        else if (n.getValue() instanceof KlokkenNode)
            emit("klokken.hour, klokken.minute");
        else n.getValue().accept(this);

        if(n.getValue().type == BOOLSK) emit("? \"Sandt\" : \"Falsk\"");
        emit(");");
    }

    @Override
    public void visit(ProgramNode n) {
        boolean containsKlokken = AST.getSymbolTable().containsValue(TID);
        boolean containsString = AST.getSymbolTable().containsValue(TEKST);

        emit("#include \"Lib/Eziot.h\"\n\n");

        n.getChild().stream().filter(ast -> ast instanceof DeviceNode).forEach((device) -> {
            device.accept(this);
        });

        // Declaration of variables
        AST.getSymbolTable().forEach((id, type) -> {
            if (id.contains(".")) {
                // Do nothing
            } else if (type == DEVICE) {
                emit(id.substring(0,1).toUpperCase() +
                        id.substring(1) + " " + id + ";\n");
            }
            else if(type == RUTINE) emit("void " + id + "();\n");
            else emit(dataTypeString.get(type) + " " + id + ";\n");
        });

        emit("\n");

        if (SymbolTable.containsValue(DEVICE)) {
            emit("void update_fields() {\n");
            blockIndent++;
            indent(blockIndent);
            //emit("printf(\"Opdaterer...\\n\");\n");
            SymbolTable.forEach((idString, type) -> {
                if (idString.contains(".")) {
                    List<String> ids = List.of(idString.split("\\."));
                    indent(blockIndent);
                    emit("get_field_from_endpoint(" + ids.get(0) + ".endpoint__");
                    emit(", \"" + ids.get(1)+"\"");
                    emit(", &"+ ids.get(0) + "." + ids.get(1));
                    emit(", " + datatypeEnumRepresentationC.get(type));
                    emit(");\n");
                }
            });
            emit("}\n");
            blockIndent--;
        }



        n.getChild().forEach((ast) -> {
            if (ast instanceof IfNode ifNode) {
                indent(blockIndent);
                emit("bool ifCond" + ifNode.getNum() + "() {\n");
                blockIndent++;
                indent(blockIndent);
                emit("return ");
                ifNode.getExpr().accept(this);
                emit(";\n");
                blockIndent--;
                indent(blockIndent);
                emit("}\n");
                indent(blockIndent);
                emit("void ifBody" + ifNode.getNum() + "() {\n");
                blockIndent++;
                ifNode.getBody().forEach((child) -> {
                    indent(blockIndent);
                    child.accept(this);
                    emit("\n");
                });
                blockIndent--;
                indent(blockIndent);
                emit("}\n");
                indent(blockIndent);
                emit("if_statement " + "ifStatement" + ifNode.getNum() + ";\n");
            }
        });

        // Freeing memory for strings in the program
        if (containsString || containsKlokken) {
            emit("\nint free_memory () {\n");
            blockIndent++;
            AST.getSymbolTable().forEach((id, type) -> {
                if(type == TEKST) {
                    indent(blockIndent);
                    emit("free(" + id + ");\n");
                }
            });
            indent(blockIndent);
            emit("return 0;\n");
            blockIndent--;
            emit("}\n\n");
        } else emit("\n");

        indent(blockIndent);
        emit("pthread_mutex_t thread_count_lock = PTHREAD_MUTEX_INITIALIZER;\n");
        indent(blockIndent);
        emit("bool running = true;\n\n");

        emit("int main() {\n");
        blockIndent++;
        indent(blockIndent);
        emit("int thread_count = 1;\n");
        indent(blockIndent);
        emit("update_klokken();\n");

        indent(blockIndent);
        emit("if_queue task_queue;\n");
        indent(blockIndent);
        emit("init_if_queue(&task_queue);\n");

        n.getChild().forEach((ast) -> {
            if (ast instanceof IfNode ifNode) {
                indent(blockIndent);
                emit("init_if_statement(&ifStatement" + ifNode.getNum() + ", ifCond" + ifNode.getNum()
                        + ", ifBody" + ifNode.getNum() + ", 500);\n");
            }
        });

        for(AST ast : n.getChild()){
            if (ast instanceof DeviceNode) {
                indent(blockIndent);
                emit(("strcpy(" + ((DeviceNode) ast).getId()) + ".endpoint__, \""
                        + ((DeviceNode) ast).getEndpoint() + "\");\n");
                ((DeviceNode) ast).getFields().forEach((field) -> {
                    indent(blockIndent);
                    field.accept(this);
                    emit("\n");
                });
                emit("\n");
            } else if (ast instanceof IfNode ifNode) {


            } else if(!(ast instanceof FuncDclNode)) {
                indent(blockIndent);
                ast.accept(this);
                emit("\n");
            }
        }
        // Loop

        emit("""
                    struct timeval last_time_update;
                    gettimeofday(&last_time_update, NULL);
                    struct timeval tv;
                    while(running) {
                        gettimeofday(&tv, NULL);
                        if(tv.tv_sec > last_time_update.tv_sec) {
                            update_klokken();
                """
        );
        blockIndent += 2;

        if (SymbolTable.containsValue(DEVICE)) {
            indent(blockIndent);
            emit("update_fields();\n");
        }

        indent(blockIndent);
        emit("gettimeofday(&last_time_update, NULL);\n");
        blockIndent--;
        indent(blockIndent);
        emit("}\n");
        n.getChild().forEach((ast -> {
            if (ast instanceof IfNode ifNode) {
                indent(blockIndent);
                emit("update_if_check(&ifStatement" + ifNode.getNum() + ", &task_queue);\n");
            }
        }));
        blockIndent--;

        emit("""
                        while(!is_queue_empty(&task_queue)) {
                            pthread_mutex_lock(&thread_count_lock);
                            if(thread_count >= MAX_THREADS) break;
                            pthread_mutex_unlock(&thread_count_lock);
                            pthread_t thread;
                            run_if_thread_args *args = init_run_if_thread_args(&thread_count,
                                                                get_from_queue(&task_queue), &thread_count_lock);
                            int thread_created = pthread_create(&thread, NULL, (void *) run_if_thread, (void *) args);
                            if(thread_created == 0) {
                                remove_from_queue(&task_queue);
                                pthread_mutex_lock(&thread_count_lock);
                                thread_count++;
                                pthread_mutex_unlock(&thread_count_lock);
                            } else {
                                printf("Error\\n");
                            }
                        }
                    }
                """);

        indent(blockIndent);
        if (AST.getSymbolTable().containsValue(TEKST) || AST.getSymbolTable().containsValue(TID)) {
            emit("free_memory();\n");
            indent(blockIndent);
        }

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
        if (writer != null) {
            try {
                writer.write(code);
                writer.close();
            } catch (IOException e) {
                throw new FileWriterIOException("[FEJL] Kunne ikke skrive til filen " + writer);
            }
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
        n.getId().accept(this);
        emit(" = ");
        emit("malloc(" + (((TekstLiteral) n.getValue()).getValue().length()+1) +
                " * sizeof(char));\n");
        indent(blockIndent);
        emit("strcpy(");
        n.getId().accept(this);
        emit(", ");
        n.getValue().accept(this);
        emit(");");
    }

    @Override
    public void visit(HeltalDcl n) {
        n.getId().accept(this);
        emit(" = ");
        n.getValue().accept(this);
        emit(";");
    }

    @Override
    public void visit(DecimaltalDcl n) {
        n.getId().accept(this);
        emit(" = ");
        n.getValue().accept(this);
        emit(";");
    }

    @Override
    public void visit(BoolskDcl n) {
        n.getId().accept(this);
        emit(" = ");
        n.getValue().accept(this);
        emit(";");
    }

    public void indent(int indents) {
        for (int i = 0; i < indents; i++) {
            emit("    ");
        }
    }
    @Override
    public void visit(DeviceNode n) {
        // Declaring device struct
        emit("typedef struct {\n");
        blockIndent++;
        indent(blockIndent);
        emit("char endpoint__[" + (n.getEndpoint().length()+1) + "];\n");
        n.getFields().forEach((field) -> {
            indent(blockIndent);
            emit(dataTypeString.get(field.getType()) + " " + field.getId().getValue() + ";");
            emit("\n");
        });
        blockIndent--;
        indent(blockIndent);
        emit("} " + n.getId().substring(0,1).toUpperCase()
                + n.getId().substring(1) + ";\n\n");
    }

    @Override
    public void visit(KlokkenNode n) {
        emit("klokken");
    }

    @Override
    public void visit(TidNode n) {
        emit("make_time(" + n.getHour() + ", " + n.getMinute() + ")");
    }

    @Override
    public void visit(TidDcl n) {
        n.getId().accept(this);
        emit(" = make_time(" + ((TidNode) n.getValue()).getHour() + ", "
                + ((TidNode) n.getValue()).getMinute() + ");");
    }

    @Override
    public void visit(CommentNode commentNode) {
        emit("// " + commentNode.getValue() + "\n");
    }

    public String getCode() {
        return code;
    }
}

