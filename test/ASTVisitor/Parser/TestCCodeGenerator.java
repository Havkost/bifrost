package ASTVisitor.Parser;

import ASTVisitor.ASTnodes.*;
import ASTVisitor.Exceptions.FileWriterIOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestCCodeGenerator {

    CCodeGenerator generator;
    @BeforeEach
    public void beforeEach() {
        AST.clearSymbolTable();
        generator = new CCodeGenerator(false);
    }
    @Test
    public void testEmit() {
        generator.emit("Test text");
        generator.emit(" added.");
        assertEquals("Test text added.", generator.getCode());
    }

    @Test
    public void testExpr1() {
        BinaryComputing expr = new BinaryComputing("er",
                new BinaryComputing(AST.Operators.PLUS, new HeltalLiteral("5"),
                        new DecimaltalLiteral("4,3")),
                new BinaryComputing(AST.Operators.MINUS, new HeltalLiteral("10"),
                        new DecimaltalLiteral("0,7")));
        generator.visit(expr);
        assertEquals("5 + 4.3 == 10 - 0.7", generator.getCode());
    }

    @Test
    public void testExpr2() {
        UnaryComputing expr = new UnaryComputing(AST.Operators.PAREN,
                new BinaryComputing(AST.Operators.PLUS, new HeltalLiteral("5",1),
                        new DecimaltalLiteral("4,3",1),1),1);
        generator.visit(expr);
        assertEquals("(5 + 4.3)", generator.getCode());
    }

    @Test
    public void testExpr3() {
        UnaryComputing expr = new UnaryComputing("ikke",
                new BoolskLiteral("falsk"));
        generator.visit(expr);
        assertEquals("!false", generator.getCode());
    }

    @Test
    public void testStrings() {
        String str = """
                      gem tekst "Haj" som a
                      gem tekst "hallo" som b
                      
                      sæt a til "hej"
                      
                      print a
                      print b
                      """;

        TekstDcl line1 = new TekstDcl(new TekstLiteral("Haj"), new IdNode("a"));
        TekstDcl line2 = new TekstDcl(new TekstLiteral("hallo"), new IdNode("b"));

        AssignNode line3 = new AssignNode(new IdNode("a"), new TekstLiteral("hej"));

        PrintNode line4 = new PrintNode(new IdNode("a"));
        PrintNode line5 = new PrintNode(new IdNode("b"));

        ProgramNode prog = new ProgramNode(Arrays.asList(line1, line2, line3, line4, line5));

        prog.accept(new SymbolTableFilling());
        prog.accept(new TypeChecker());

        prog.accept(generator);

        assertEquals("""
                #include "Lib/Eziot.h"
                                     
                char* a;
                char* b;
                      
                                     
                int free_memory () {
                    free(a);
                    free(b);
                    return 0;
                }
                                
                int thread_count = 1;
                pthread_mutex_t thread_count_lock = PTHREAD_MUTEX_INITIALIZER;
                bool running = true;
                                     
                int main() {
                    if_queue task_queue;
                    init_queue(&task_queue);
                    a = malloc(4 * sizeof(char));
                    strcpy(a, "Haj");
                    b = malloc(6 * sizeof(char));
                    strcpy(b, "hallo");
                    realloc(a, 3 * sizeof(char));
                    a = "hej";
                    printf("%s\\n", a);
                    printf("%s\\n", b);
                    while(!is_queue_empty(&task_queue)) {
                        printf("Tjek 1\\n");
                        if(thread_count >= MAX_THREADS) break;
                        printf("Tjek 2\\n");
                        pthread_t thread;
                        run_if_thread_args *args = init_run_if_thread_args(&thread_count,
                                                            get_from_queue(&task_queue), &thread_count_lock);
                        int thread_created = pthread_create(&thread, NULL, run_if_thread, (void *) args);
                        if(thread_created == 0) {
                            pop_from_queue(&task_queue);
                            pthread_mutex_lock(&thread_count_lock);
                            thread_count++;
                            pthread_mutex_unlock(&thread_count_lock);
                        }
                    }
                    free_memory();
                    return 0;
                }
                                    
                """, generator.getCode());
    }

    @Test
    public void testPrint() {
        PrintNode print = new PrintNode(new HeltalLiteral("3"));
        print.accept(new SymbolTableFilling());
        print.accept(new TypeChecker());
        generator.visit(print);

        assertEquals("printf(\"%d\\n\", 3);", generator.getCode());
    }

    @Test
    public void testPrintField() {
        DeviceNode deviceNode = new DeviceNode("lampe1", List.of(new HeltalDcl(new HeltalLiteral("50"),
                new IdNode("lysstyrke", "lampe1"))), "test");
        PrintNode print = new PrintNode(new IdNode("lysstyrke", "lampe1"));
        ProgramNode prog = new ProgramNode(List.of(deviceNode, print));

        prog.accept(new SymbolTableFilling());
        prog.accept(new TypeChecker());
        generator.visit(prog);

        assertEquals("""
           #include "Lib/Eziot.h"
           
           typedef struct {
               char endpoint__[5];
               int lysstyrke;
           } Lampe1;
           
           Lampe1 lampe1;
           
           
           int thread_count = 1;
           pthread_mutex_t thread_count_lock = PTHREAD_MUTEX_INITIALIZER;
           bool running = true;
           
           int main() {
               if_queue task_queue;
               init_queue(&task_queue);
               strcpy(lampe1.endpoint__, "test");
               lampe1.lysstyrke = 50;
           
               printf("%d\\n", lampe1.lysstyrke);
               while(!is_queue_empty(&task_queue)) {
                   printf("Tjek 1\\n");
                   if(thread_count >= MAX_THREADS) break;
                   printf("Tjek 2\\n");
                   pthread_t thread;
                   run_if_thread_args *args = init_run_if_thread_args(&thread_count,
                                                       get_from_queue(&task_queue), &thread_count_lock);
                   int thread_created = pthread_create(&thread, NULL, run_if_thread, (void *) args);
                   if(thread_created == 0) {
                       pop_from_queue(&task_queue);
                       pthread_mutex_lock(&thread_count_lock);
                       thread_count++;
                       pthread_mutex_unlock(&thread_count_lock);
                   }
               }
               return 0;
           }
           
               """, generator.getCode());
    }

    @Test
    public void testDcls() {
        HeltalDcl dcl1 = new HeltalDcl(new HeltalLiteral("3"), new IdNode("a"));
        TekstDcl dcl2 = new TekstDcl(new TekstLiteral("test"), new IdNode(("b")));
        DecimaltalDcl dcl3 = new DecimaltalDcl(new DecimaltalLiteral("11,5"), new IdNode("c"));
        BoolskDcl dcl4 = new BoolskDcl(new BoolskLiteral("falsk"), new IdNode("d"));

        ProgramNode prog = new ProgramNode(Arrays.asList(dcl1, dcl2, dcl3, dcl4));

        prog.accept(new SymbolTableFilling());
        prog.accept(new TypeChecker());

        generator.visit(prog);
        assertEquals("""
                #include "Lib/Eziot.h"
                                     
                int a;
                char* b;
                double c;
                bool d;
                
                                     
                int free_memory () {
                    free(b);
                    return 0;
                }
                      
                int thread_count = 1;
                pthread_mutex_t thread_count_lock = PTHREAD_MUTEX_INITIALIZER;
                bool running = true;      
                                     
                int main() {
                    if_queue task_queue;
                    init_queue(&task_queue);
                    a = 3;
                    b = malloc(5 * sizeof(char));
                    strcpy(b, "test");
                    c = 11.5;
                    d = false;
                    while(!is_queue_empty(&task_queue)) {
                        printf("Tjek 1\\n");
                        if(thread_count >= MAX_THREADS) break;
                        printf("Tjek 2\\n");
                        pthread_t thread;
                        run_if_thread_args *args = init_run_if_thread_args(&thread_count,
                                                            get_from_queue(&task_queue), &thread_count_lock);
                        int thread_created = pthread_create(&thread, NULL, run_if_thread, (void *) args);
                        if(thread_created == 0) {
                            pop_from_queue(&task_queue);
                            pthread_mutex_lock(&thread_count_lock);
                            thread_count++;
                            pthread_mutex_unlock(&thread_count_lock);
                        }
                    }
                    free_memory();
                    return 0;
                }
                                    
                 """, generator.getCode());
    }

    @Test
    public void testIfNode() {
        IfNode ifNode = new IfNode(new BinaryComputing("er",
                            new HeltalLiteral("3"), new HeltalLiteral("3")),
                            List.of(new FuncNode("test")));
        ifNode.accept(generator);

        assertEquals("""
                    if (3 == 3) {
                        test();
                    }""", generator.getCode());
    }

    @Test
    public void testFuncDcl() {
        FuncDclNode funcDclNode = new FuncDclNode("test", List.of(
                new IfNode(new BinaryComputing("ikke er", new IdNode("a"),
                        new HeltalLiteral("3")),
                    List.of(new AssignNode(new IdNode("a"), new HeltalLiteral("5"))))));
        funcDclNode.accept(generator);

        assertEquals("""
                void test() {
                    if (a != 3) {
                        a = 5;
                    }
                }""", generator.getCode());
    }

    @Test
    void testLoopNode() {
        LoopNode loopNode = new LoopNode("test", new HeltalLiteral("5"));

        loopNode.accept(generator);
        assertEquals("""
                for(int __i = 0; __i < (5); __i++) {
                    test();
                }""", generator.getCode());
    }

    @Test
    void testProg() {
        ProgramNode programNode = new ProgramNode(List.of(
                new HeltalDcl(new HeltalLiteral("3"), new IdNode("a")),
                new FuncDclNode("func", List.of(
                        new AssignNode(new IdNode("a"), new HeltalLiteral("8")),
                        new IfNode(new BinaryComputing("er", new HeltalLiteral("3"),
                                new HeltalLiteral("3")), List.of(new AssignNode(new IdNode("a"), new HeltalLiteral("5"))))
                )),
                new DeviceNode("lampe1", List.of(new HeltalDcl(new HeltalLiteral("70"),
                        new IdNode("lysstyrke", "lampe1"))), "test")
        ));

        programNode.accept(new SymbolTableFilling());
        programNode.accept(new TypeChecker());

        programNode.accept(generator);

        assertEquals("""
                #include "Lib/Eziot.h"
                 
                typedef struct {
                    char endpoint__[5];
                    int lysstyrke;
                } Lampe1;
                
                int a;
                Lampe1 lampe1;
                void func();
                 
                 
                int thread_count = 1;
                pthread_mutex_t thread_count_lock = PTHREAD_MUTEX_INITIALIZER;
                bool running = true;
                 
                int main() {
                    if_queue task_queue;
                    init_queue(&task_queue);
                    a = 3;
                    strcpy(lampe1.endpoint__, "test");
                    lampe1.lysstyrke = 70;
                 
                    while(!is_queue_empty(&task_queue)) {
                        printf("Tjek 1\\n");
                        if(thread_count >= MAX_THREADS) break;
                        printf("Tjek 2\\n");
                        pthread_t thread;
                        run_if_thread_args *args = init_run_if_thread_args(&thread_count,
                                                            get_from_queue(&task_queue), &thread_count_lock);
                        int thread_created = pthread_create(&thread, NULL, run_if_thread, (void *) args);
                        if(thread_created == 0) {
                            pop_from_queue(&task_queue);
                            pthread_mutex_lock(&thread_count_lock);
                            thread_count++;
                            pthread_mutex_unlock(&thread_count_lock);
                        }
                    }
                    return 0;
                }
                 
                void func() {
                    a = 8;
                    if (3 == 3) {
                        a = 5;
                    }
                }
                """, generator.getCode());
    }

    @Test
    void testFileWrite() throws IOException {
        File file = new File("file.c");
        file.createNewFile();
        FileWriter writer;
        writer = new FileWriter(file);

        generator = new CCodeGenerator(false, writer);
        ProgramNode programNode = new ProgramNode(List.of(
                new HeltalDcl(new HeltalLiteral("3"), new IdNode("a")),
                new FuncDclNode("func", List.of(
                        new AssignNode(new IdNode("a"), new HeltalLiteral("8")),
                        new IfNode(new BinaryComputing("er", new HeltalLiteral("3"),
                                new HeltalLiteral("3")), List.of(new AssignNode(new IdNode("a"), new HeltalLiteral("5"))))
                ))
        ));

        programNode.accept(generator);
        assertEquals("""
                #include "Lib/Eziot.h"
                                
                                
                                
                int thread_count = 1;
                pthread_mutex_t thread_count_lock = PTHREAD_MUTEX_INITIALIZER;
                bool running = true;
                                
                int main() {
                    if_queue task_queue;
                    init_queue(&task_queue);
                    a = 3;
                    while(!is_queue_empty(&task_queue)) {
                        printf("Tjek 1\\n");
                        if(thread_count >= MAX_THREADS) break;
                        printf("Tjek 2\\n");
                        pthread_t thread;
                        run_if_thread_args *args = init_run_if_thread_args(&thread_count,
                                                            get_from_queue(&task_queue), &thread_count_lock);
                        int thread_created = pthread_create(&thread, NULL, run_if_thread, (void *) args);
                        if(thread_created == 0) {
                            pop_from_queue(&task_queue);
                            pthread_mutex_lock(&thread_count_lock);
                            thread_count++;
                            pthread_mutex_unlock(&thread_count_lock);
                        }
                    }
                    return 0;
                }
                                
                void func() {
                    a = 8;
                    if (3 == 3) {
                        a = 5;
                    }
                }                
                """, Files.readString(file.toPath()));

    }

    @Test
    void testFileWriteError() throws IOException {
        File file = new File("file.c");
        assertTrue(file.createNewFile());
        FileWriter writer;
        writer = new FileWriter(file);

        writer.close();

        generator = new CCodeGenerator(false, writer);
        ProgramNode programNode = new ProgramNode(List.of(
                new HeltalDcl(new HeltalLiteral("3"), new IdNode("a")),
                new FuncDclNode("func", List.of(
                        new AssignNode(new IdNode("a"), new HeltalLiteral("8")),
                        new IfNode(new BinaryComputing("er", new HeltalLiteral("3"),
                                new HeltalLiteral("3")), List.of(new AssignNode(new IdNode("a"), new HeltalLiteral("5"))))
                ))
        ));

        assertThrows(FileWriterIOException.class, () -> programNode.accept(generator));

        file.deleteOnExit();
    }

    @Test
    void testDeviceDcl() {
        DeviceNode deviceNode = new DeviceNode("test", List.of(), "test");

        deviceNode.accept(generator);

        assertEquals("""
                typedef struct {
                    char endpoint__[5];
                } Test;
                
                """, generator.getCode());
    }

    @Test
    void testStringConcat() {
        TekstDcl tekstDcl = new TekstDcl(new TekstLiteral(""), new IdNode("test"));
        AssignNode assignNode = new AssignNode(new IdNode("test"), new BinaryComputing(AST.Operators.PLUS, new TekstLiteral("hej"),
                new BinaryComputing(AST.Operators.PLUS, new TekstLiteral(" "), new TekstLiteral("verden"))));
        ProgramNode prog = new ProgramNode(List.of(tekstDcl, assignNode));

        prog.accept(new SymbolTableFilling());
        prog.accept(new TypeChecker());
        prog.accept(generator);

        assertEquals("""
                #include "Lib/Eziot.h"
                                
                char* test;
                     
                                
                int free_memory () {
                    free(test);
                    return 0;
                }
                
                int thread_count = 1;
                pthread_mutex_t thread_count_lock = PTHREAD_MUTEX_INITIALIZER;
                bool running = true;
                
                int main() {
                    if_queue task_queue;
                    init_queue(&task_queue);
                    test = malloc(1 * sizeof(char));
                    strcpy(test, "");
                    test = realloc(concat("hej", concat(" ", "verden"), customStrLen(" ", strlen("verden"))), customStrLen("hej", concat(" ", "verden"), customStrLen(" ", strlen("verden"))));
                    while(!is_queue_empty(&task_queue)) {
                        printf("Tjek 1\\n");
                        if(thread_count >= MAX_THREADS) break;
                        printf("Tjek 2\\n");
                        pthread_t thread;
                        run_if_thread_args *args = init_run_if_thread_args(&thread_count,
                                                            get_from_queue(&task_queue), &thread_count_lock);
                        int thread_created = pthread_create(&thread, NULL, run_if_thread, (void *) args);
                        if(thread_created == 0) {
                            pop_from_queue(&task_queue);
                            pthread_mutex_lock(&thread_count_lock);
                            thread_count++;
                            pthread_mutex_unlock(&thread_count_lock);
                        }
                    }
                    free_memory();
                    return 0;
                }
                                
                """, generator.getCode());
    }

    @Test
    void testAssignField() {
        IdNode id = new IdNode("lysstyrke", "lampe1");
        List<VariableDcl> list = new ArrayList<>(List.of(new HeltalDcl(new HeltalLiteral("5"), id)));
        DeviceNode device = new DeviceNode(id.getParentId(), list, "test");
        AssignNode assignNode = new AssignNode(id, new HeltalLiteral("3"));

        ProgramNode prog = new ProgramNode(List.of(device, assignNode));
        prog.accept(new SymbolTableFilling());
        prog.accept(new TypeChecker());
        prog.accept(generator);

        assertEquals("""
                #include "Lib/Eziot.h"
                     
                typedef struct {
                    char endpoint__[5];
                    int lysstyrke;
                } Lampe1;
                 
                Lampe1 lampe1;
                 
                 
                int thread_count = 1;
                pthread_mutex_t thread_count_lock = PTHREAD_MUTEX_INITIALIZER;
                bool running = true; 
                 
                int main() {
                    if_queue task_queue;
                    init_queue(&task_queue);
                    strcpy(lampe1.endpoint__, "test");
                    lampe1.lysstyrke = 5;
                    
                    lampe1.lysstyrke = 3;
                    send_field_to_endpoint(lampe1.endpoint__, "lysstyrke", &lampe1.lysstyrke, TYPE_INTEGER);
                    while(!is_queue_empty(&task_queue)) {
                        printf("Tjek 1\\n");
                        if(thread_count >= MAX_THREADS) break;
                        printf("Tjek 2\\n");
                        pthread_t thread;
                        run_if_thread_args *args = init_run_if_thread_args(&thread_count,
                                                            get_from_queue(&task_queue), &thread_count_lock);
                        int thread_created = pthread_create(&thread, NULL, run_if_thread, (void *) args);
                        if(thread_created == 0) {
                            pop_from_queue(&task_queue);
                            pthread_mutex_lock(&thread_count_lock);
                            thread_count++;
                            pthread_mutex_unlock(&thread_count_lock);
                        }
                    }
                    return 0;
                }
                 
                """, generator.getCode());
    }

    @Test
    public void testIfTekstEqualsTekst() {
        IfNode ifNode = new IfNode(new BinaryComputing("er",
                new TekstLiteral("\"hej\""), new TekstLiteral("\"med dig\"")),
                List.of(new FuncNode("test")));
        ifNode.accept(generator);

        assertEquals("""
                if (strcmp(""hej"", ""med dig"") == 0) {
                    test();
                }""", generator.getCode());
    }

    @Test
    public void testIfTekstNotEqualsTekst() {
        IfNode ifNode = new IfNode(new BinaryComputing("ikke er",
                new TekstLiteral("\"hej\""), new TekstLiteral("\"med dig\"")),
                List.of(new FuncNode("test")));
        ifNode.accept(generator);

        assertEquals("""
                if (strcmp(""hej"", ""med dig"") == 1) {
                    test();
                }""", generator.getCode());
    }

    @Test
    public void testIfTidEqualsTid() {
        IfNode ifNode = new IfNode(new BinaryComputing("er",
                new TidNode(12,20), new KlokkenNode()),
                List.of(new FuncNode("test")));
        ifNode.accept(generator);

        assertEquals("""
                if (time_compare(make_time(12, 20), klokken) == 0) {
                    test();
                }""", generator.getCode());
    }

    @Test
    public void testIfTidNotEqualsTid() {
        IfNode ifNode = new IfNode(new BinaryComputing("ikke er",
                new TidNode(12,20), new KlokkenNode()),
                List.of(new FuncNode("test")));
        ifNode.accept(generator);

        assertEquals("""
                if (time_compare(make_time(12, 20), klokken) != 0) {
                    test();
                }""", generator.getCode());
    }

    @Test
    public void testIfTidLesserThanTid() {
        IfNode ifNode = new IfNode(new BinaryComputing("<",
                new TidNode(12,20), new KlokkenNode()),
                List.of(new FuncNode("test")));
        ifNode.accept(generator);

        assertEquals("""
                if (time_compare(make_time(12, 20), klokken) == -1) {
                    test();
                }""", generator.getCode());
    }

    @Test
    public void testIfTidGreaterThanTid() {
        IfNode ifNode = new IfNode(new BinaryComputing(">",
                new TidNode(12,20), new KlokkenNode()),
                List.of(new FuncNode("test")));
        ifNode.accept(generator);

        assertEquals("""
                if (time_compare(make_time(12, 20), klokken) == 1) {
                    test();
                }""", generator.getCode());
    }

    @Test
    public void testPrintNodeWithTidNode() {
        PrintNode print = new PrintNode(new TidNode(10,10));
        print.accept(generator);

        assertEquals("""
                printf("%02d:%02d\\n", 10, 10);""", generator.getCode());
    }

    @Test
    public void testPrintNodeWithKlokkenNode() {
        PrintNode print = new PrintNode(new KlokkenNode());
        print.accept(new TypeChecker());
        print.accept(generator);

        assertEquals("""
                printf("%02d:%02d\\n", klokken.hour, klokken.minute);""", generator.getCode());
    }

    @Test
    public void testKlokkenNode() {
        KlokkenNode klokken = new KlokkenNode();
        ProgramNode prog = new ProgramNode(List.of(klokken));
        prog.accept(new TypeChecker());
        prog.accept(new SymbolTableFilling());
        prog.accept(generator);

        assertEquals("""
                #include "Lib/Eziot.h"
                                                                     
                Time klokken;
                  
                                
                int free_memory () {
                    return 0;
                }
                
                int thread_count = 1;
                pthread_mutex_t thread_count_lock = PTHREAD_MUTEX_INITIALIZER;
                bool running = true;
                                
                int main() {
                    klokken = time_generator();
                    if_queue task_queue;
                    init_queue(&task_queue);
                    klokken
                    while(!is_queue_empty(&task_queue)) {
                        printf("Tjek 1\\n");
                        if(thread_count >= MAX_THREADS) break;
                        printf("Tjek 2\\n");
                        pthread_t thread;
                        run_if_thread_args *args = init_run_if_thread_args(&thread_count,
                                                            get_from_queue(&task_queue), &thread_count_lock);
                        int thread_created = pthread_create(&thread, NULL, run_if_thread, (void *) args);
                        if(thread_created == 0) {
                            pop_from_queue(&task_queue);
                            pthread_mutex_lock(&thread_count_lock);
                            thread_count++;
                            pthread_mutex_unlock(&thread_count_lock);
                        }
                    }
                    free_memory();
                    return 0;
                }
                                
                """, generator.getCode());
    }

    @Test
    public void testTidDclGenerator() {
        TidDcl tid = new TidDcl(new TidNode(12, 30), new IdNode("tiden"));
        tid.accept(new TypeChecker());
        tid.accept(new SymbolTableFilling());
        tid.accept(generator);

        assertEquals("""
                tiden = make_time(12, 30);""", generator.getCode());
    }

    @Test
    public void testIfNodeProgram() {
        IfNode ifNode = new IfNode(new BinaryComputing("er",
                new HeltalLiteral("3"), new HeltalLiteral("3")),
                List.of(new HeltalDcl(new HeltalLiteral("5"), new IdNode("hej"))));
        ProgramNode prog = new ProgramNode(List.of(ifNode));
        prog.accept(new TypeChecker());
        prog.accept(new SymbolTableFilling());
        prog.accept(generator);

        assertEquals("""
                #include "Lib/Eziot.h"
                                    
                int hej;
                                    
                bool ifCond1() {
                    return 3 == 3;
                }
                void ifBody1() {
                    hej = 5;;
                }
                if_statement ifStatement1;
                                    
                int thread_count = 1;
                pthread_mutex_t thread_count_lock = PTHREAD_MUTEX_INITIALIZER;
                bool running = true;
                                    
                int main() {
                    if_queue task_queue;
                    init_queue(&task_queue);
                    init_if_statement(&ifStatement1, ifCond1, ifBody1);
                    add_to_queue(&task_queue, &ifStatement1);
                    while(!is_queue_empty(&task_queue)) {
                        printf("Tjek 1\\n");
                        if(thread_count >= MAX_THREADS) break;
                        printf("Tjek 2\\n");
                        pthread_t thread;
                        run_if_thread_args *args = init_run_if_thread_args(&thread_count,
                                                            get_from_queue(&task_queue), &thread_count_lock);
                        int thread_created = pthread_create(&thread, NULL, run_if_thread, (void *) args);
                        if(thread_created == 0) {
                            pop_from_queue(&task_queue);
                            pthread_mutex_lock(&thread_count_lock);
                            thread_count++;
                            pthread_mutex_unlock(&thread_count_lock);
                        }
                    }
                    return 0;
                }
                                    
                """, generator.getCode());
    }
}
