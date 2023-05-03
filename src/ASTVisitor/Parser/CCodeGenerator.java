package ASTVisitor.Parser;

import ASTVisitor.ASTnodes.*;
import ASTVisitor.Exceptions.FileWriterIOException;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static ASTVisitor.Parser.AST.*;
import static java.util.Map.entry;
import static ASTVisitor.Parser.AST.DataTypes.*;

public class CCodeGenerator extends Visitor {

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
            emit("realloc(" + id + ", " + ((TekstLiteral) n.getValue()).getValue().length() + " * sizeof(char));\n");
            indent(blockIndent);
        } // If the value has the type of string, reallocate memory for that TODO: Check if this works
        else if (SymbolTable.get(id) != null && SymbolTable.get(id).equals(TEKST)
                && n.getValue().getType().equals(TEKST)) {
            emit(id + " = ");
            emit("realloc(");
            n.getValue().accept(this);
            emit(");");
            return;
        }
        emit(id + " = ");
        n.getValue().accept(this);
        emit(";");
        if (n.getId().getParentId() != null) {
            emit("\n");
            indent(blockIndent);
            emit("send_field_to_endpoint("+n.getId().getParentId()+".endpoint__");
            emit(", \""+n.getId().getValue()+"\"");
            emit(", &"+id);
            emit(", "+datatypeEnumRepresentationC.get(SymbolTable.get(id)));
            emit(");");
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
        boolean containsDevice = AST.getSymbolTable().containsValue(DEVICE);
        emit("#include <stdlib.h>\n");
        emit("#include <stdio.h>\n");
        emit("#include <stdbool.h>\n");
        if(containsKlokken)
            emit("#include <time.h>\n");
        if(containsString || containsKlokken)
            emit("#include <string.h>\n");
        if(containsDevice) {
            emit("#include <curl/curl.h>\n");
            emit("#include <cjson/cJSON.h>\n");
        }
        emit("\n");

        n.getChild().stream().filter(ast -> ast instanceof DeviceNode).forEach((device) -> {
            device.accept(this);
        });

        if (containsKlokken) {
            emit("""
                    typedef struct {
                        int hour;
                        int minute;
                    } Time;
                    
                    """);

            emit("""
                    int time_compare(Time t1, Time t2) {
                        if (t1.hour > t2.hour || (t1.hour == t2.hour && t1.minute > t2.minute)) {
                            return 1;
                        } else if (t1.hour == t2.hour && t1.minute == t2.minute) {
                            return 0;
                        }
                        return -1;
                    }
                    
                    Time make_time(int hour, int minute) {
                        Time res;
                        res.hour = hour;
                        res.minute = minute;
                        
                        return res;
                    }
                    
                    """);
        }

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

        // HTTP communication functions
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
                                    
                      char *protocol = "http://";
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
                                    
                    struct string {
                      char *ptr;
                      size_t len;
                    };
                                        
                    void init_string(struct string *s) {
                      s->len = 0;
                      s->ptr = malloc(s->len+1);
                      if (s->ptr == NULL) {
                        fprintf(stderr, "malloc() failed\\n");
                        exit(EXIT_FAILURE);
                      }
                      s->ptr[0] = '\\0';
                    }
                                        
                    size_t writefunc(void *ptr, size_t size, size_t nmemb, struct string *s)
                    {
                      size_t new_len = s->len + size*nmemb;
                      s->ptr = realloc(s->ptr, new_len+1);
                      if (s->ptr == NULL) {
                        fprintf(stderr, "realloc() failed\\n");
                        exit(EXIT_FAILURE);
                      }
                      memcpy(s->ptr+s->len, ptr, size*nmemb);
                      s->ptr[new_len] = '\\0';
                      s->len = new_len;
                                        
                      return size*nmemb;
                    }
                                        
                    enum Datatype
                    {
                        TYPE_INTEGER,
                        TYPE_DOUBLE,
                        TYPE_STRING,
                        TYPE_BOOL
                    };
                                        
                    int get_field_from_endpoint(char *endpoint, char *field, void *value_ptr, enum Datatype datatype)
                    {
                        // Send to endpoint
                        CURL *curl;
                        CURLcode res;
                        struct string response;
                        init_string(&response);
                                        
                        char *protocol = "http://";
                        char *url = calloc(strlen(protocol) + strlen(endpoint) + 1, sizeof(char));
                        strcpy(url, protocol);
                        strcat(url, endpoint);
                                        
                        curl = curl_easy_init();
                        if (curl)
                        {
                            struct curl_slist *hs = NULL;
                            curl_easy_setopt(curl, CURLOPT_URL, url);
                            curl_easy_setopt(curl, CURLOPT_FOLLOWLOCATION, 1L);
                            curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, writefunc);
                            curl_easy_setopt(curl, CURLOPT_WRITEDATA, &response);
                                        
                            res = curl_easy_perform(curl);
                                        
                            /* Check for errors */
                            if (res != CURLE_OK)
                                fprintf(stderr, "curl_easy_perform() failed: %s\\n",
                                        curl_easy_strerror(res));
                        }
                                        
                        cJSON *root = cJSON_Parse(response.ptr);
                                        
                        // Read data from JSON result
                        switch (datatype) {
                            case TYPE_INTEGER:
                                *((int *) value_ptr) = cJSON_GetObjectItem(root,field)->valueint;
                                break;
                            case TYPE_DOUBLE:
                                *((double *) value_ptr) = cJSON_GetObjectItem(root,field)->valuedouble;
                                break;
                            case TYPE_STRING:
                                value_ptr = cJSON_GetObjectItem(root,field)->valuestring;
                                break;
                            case TYPE_BOOL:
                                *((bool *) value_ptr) = cJSON_GetObjectItem(root,field)->valueint;
                                break;
                        }
                                        
                        // Cleanup
                        curl_easy_cleanup(curl);
                        cJSON_Delete(root);
                        free(url);
                        free(response.ptr);
                                        
                        return 0;
                    }
                    """);
        }
        if(containsKlokken) {
            emit("""
                    Time time_generator()
                    {
                        Time res;
                        struct tm* local;
                        time_t t = time(NULL);
                                        
                        // Get the localtime
                        local = localtime(&t);
                                        
                        // Stringify current time
                        char* time_str = malloc(sizeof(char) * 12);
                        strftime(time_str, 12, "%H", local);
                        res.hour = atoi(time_str);       
                        
                        strftime(time_str, 12, "%M", local);
                        res.minute = atoi(time_str);
                                        
                        free(time_str);
                        return res;
                    }
                    """);
        }

        emit("int main() {\n");
        blockIndent++;
        if (containsKlokken) {
            indent(blockIndent);
            emit("klokken = time_generator();\n");
        }
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

    public String getCode() {
        return code;
    }
}

