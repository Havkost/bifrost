package ASTVisitor.Parser;

import ASTVisitor.ASTnodes.*;
import ASTVisitor.Exceptions.FileWriterIOException;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import static ASTVisitor.Parser.AST.Operators;
import static ASTVisitor.Parser.AST.SymbolTable;
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

    private boolean containsString;
    private boolean containsDevice;

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
        String id;
        if (n.getId() instanceof IdNode) id = ((IdNode) n.getId()).getId();
        else id = ((FieldNode) n.getId()).getParentId() + "." + ((FieldNode) n.getId()).getId();
        if (SymbolTable.get(id) != null && SymbolTable.get(id).equals(TEKST) && n.getValue() instanceof TekstLiteral) {
            emit("realloc(" + id + ", " + ((TekstLiteral) n.getValue()).getValue().length() + " * sizeof(char));\n");
            indent(blockIndent);
        } else if (SymbolTable.get(id) != null && SymbolTable.get(id).equals(TEKST) && n.getValue().getType().equals(TEKST)) {
            emit(id + " = ");
            emit("realloc(");
            n.getValue().accept(this);
            emit(");");
            return;
        }
        emit(id + " = ");
        n.getValue().accept(this);
        emit(";");
    }

    @Override
    public void visit(BinaryComputing n) {
        if(n.getChild1().getType() == TEKST && n.getChild2().getType() == TEKST) {
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
    public void visit(IdNode n) {
        emit(n.getId());
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
            emit(formatStrings.get(AST.SymbolTable.get(((IdNode) n.getValue()).getId())));
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
        this.containsString = AST.getSymbolTable().containsValue(TEKST);
        this.containsDevice = AST.getSymbolTable().containsValue(DEVICE);
        emit("#include <stdlib.h>\n");
        emit("#include <stdio.h>\n");
        emit("#include <stdbool.h>\n");
        if(containsString)
            emit("#include <string.h>\n");
        if(containsDevice) {
            emit("#include <curl/curl.h>\n");
            emit("#include <cjson/cJSON.h>\n");
        }
        emit("\n");

        n.getChild().stream().filter(ast -> ast instanceof DeviceNode).forEach((device) -> {
            device.accept(this);
        });

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

        if (containsString) {
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

            emit("""
                char* concat(char* str1, char* str2) {
                    size_t len = strlen(str1) + strlen(str2) + 1;
                    char* res = malloc(len);
                    strcpy(res, str1);
                    strcat(res, str2);
                                
                    return res;
                }
                
                """);

            emit("""
                int customStrLen(char* str1, int len2) {
                    return strlen(str1) + len2;
                }
                
                """);
        } else emit("\n");

        if (containsDevice) {
            emit("""
                enum Datatype {
                  TYPE_INTEGER,
                  TYPE_DOUBLE,
                  TYPE_STRING,
                  TYPE_BOOL
                };
                
                int send_field_to_endpoint(char *endpoint, char *field, void *value_ptr, enum Datatype datatype) {
                  char *json;
                  cJSON *root;
                                
                  // Create JSON from field and value
                  root = cJSON_CreateObject();
                  switch(datatype) {
                    case TYPE_INTEGER:
                    cJSON_AddItemToObject(root, field, cJSON_CreateNumber((double) *((int *) value_ptr)));
                    break;
                    case TYPE_DOUBLE:
                    cJSON_AddItemToObject(root, field, cJSON_CreateNumber(*((double *) value_ptr)));
                    break;
                    case TYPE_STRING:
                    cJSON_AddItemToObject(root, field, cJSON_CreateString((char *) value_ptr));
                    break;
                    case TYPE_BOOL:
                    cJSON_AddItemToObject(root, field, cJSON_CreateBool(*((bool *) value_ptr)));
                    break;
                  }
                  json = cJSON_Print(root);
                                
                  // Send to endpoint
                  CURL *curl;
                  CURLcode res;
                                
                  char *protocol = "http://"; \s
                  char *url = calloc(strlen(protocol)+strlen(endpoint)+1, sizeof(char));
                  strcpy(url, protocol);
                  strcat(url, endpoint);
                                
                  curl = curl_easy_init();
                  if(curl) {
                    struct curl_slist *hs=NULL;
                    hs = curl_slist_append(hs, "Content-Type: application/json");
                    curl_easy_setopt(curl, CURLOPT_HTTPHEADER, hs);
                    curl_easy_setopt(curl, CURLOPT_URL, url);
                    // curl_easy_setopt(curl, CURLOPT_FOLLOWLOCATION, 1L);
                    curl_easy_setopt(curl, CURLOPT_POST, 1);
                    curl_easy_setopt(curl, CURLOPT_POSTFIELDS, json);
                \s
                    res = curl_easy_perform(curl);
                    /* Check for errors */
                    if(res != CURLE_OK)
                      fprintf(stderr, "curl_easy_perform() failed: %s\\n",
                              curl_easy_strerror(res));
                  }
                                
                  // Cleanup
                  curl_easy_cleanup(curl);
                  free(json);
                  cJSON_Delete(root);
                  free(url);
                                
                  return 0;
                }
                """);
        }

        emit("int main() {\n");
        blockIndent++;
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
            } else if(!(ast instanceof FuncDclNode)) {
                indent(blockIndent);
                ast.accept(this);
                emit("\n");
            }
        }
        indent(blockIndent);

        if (AST.getSymbolTable().containsValue(TEKST)) {
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
        if (n.getParentId() != null)
            emit(n.getParentId() + "." + n.getId() + " = ");
        else emit(n.getId() + " = ");
        emit("malloc(" + (((TekstLiteral) n.getValue()).getValue().length()+1) +
                " * sizeof(char));\n");
        indent(blockIndent);
        emit("strcpy(");
        if (n.getParentId() != null)
            emit(n.getParentId() + "." + n.getId() + ", ");
        else emit(n.getId() + ", ");
        n.getValue().accept(this);
        emit(");");
    }

    @Override
    public void visit(HeltalDcl n) {
        if (n.getParentId() != null)
            emit(n.getParentId() + "." + n.getId() + " = ");
        else emit(n.getId() + " = ");
        n.getValue().accept(this);
        emit(";");
    }

    @Override
    public void visit(DecimaltalDcl n) {
        if (n.getParentId() != null)
            emit(n.getParentId() + "." + n.getId() + " = ");
        else emit(n.getId() + " = ");
        n.getValue().accept(this);
        emit(";");
    }

    @Override
    public void visit(FieldDclNode n) {
        String id = n.getParentId() + "." + n.getId();
        emit(id+ " = ");
        if (n.type == TEKST) {
            emit("malloc((strlen(");
            n.getValue().accept( this);
            emit(")+1) * sizeof(char));\n");
            indent(blockIndent);
            emit("strcpy(" + id + ", ");
            n.getValue().accept(this);
            emit(");");
            return;
        }
        n.getValue().accept(this);
        emit(";");
    }

    @Override
    public void visit(FieldNode n) {
        emit(n.getParentId() + "." + n.getId());
    }


    @Override
    public void visit(BoolskDcl n) {
        if (n.getParentId() != null)
            emit(n.getParentId() + "." + n.getId() + " = ");
        else emit(n.getId() + " = ");
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
        emit("typedef struct {\n");
        blockIndent++;
        indent(blockIndent);
        emit("char endpoint__[" + (n.getEndpoint().length()+1) + "];\n");
        n.getFields().forEach((field) -> {
            indent(blockIndent);
            emit(dataTypeString.get(field.getType()) + " " + field.getId() + ";");
            emit("\n");
        });
        blockIndent--;
        indent(blockIndent);
        emit("} " + n.getId().substring(0,1).toUpperCase()
                + n.getId().substring(1) + ";\n\n");
    }

    public String getCode() {
        return code;
    }
}

