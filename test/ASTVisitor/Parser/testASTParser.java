package ASTVisitor.Parser;

import ASTVisitor.ASTnodes.*;
import ASTVisitor.ASTnodes.ProgramNode;
import ASTVisitor.Lexer.CharStream;
import ASTVisitor.Lexer.CodeScanner;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.CharArrayReader;

import static ASTVisitor.Lexer.TokenType.*;
import static java.util.Arrays.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class testASTParser {

    CharArrayReader reader;
    CharStream charStream;
    ASTParser p;

    String inputString = """
            gem heltal 3 som x
            gem decimaltal 5,5 som y
            gem tekst "test" som eksempel
            gem boolsk falsk som livsLyst
            
            hvis x er 3:
                sæt x til 1
                print eksempel
            .
            
            hvis y > 5:
                kør test
            .
            
            hvis y < 5:
                gentag
            
            hvis livsLyst er falsk:
                gentag print "test" 3 gange
            .
            
            
            rutine test:
                gentag print 2 gange
            .
            """;

    public ASTParser makeASTParser(String str){
        CharArrayReader reader = new CharArrayReader(str.toCharArray());
        CharStream charStream = new CharStream(reader);

        CodeScanner.initialize(charStream);

        ASTParser p = new ASTParser(charStream);

        return p;
    }

    @Test
    public void testProg(){
        ASTParser parser = makeASTParser("gem heltal 3 som a");
        AST dcl = new HeltalDcl(new HeltalLiteral("3"), "a");
        AST ast = new ProgramNode(asList(dcl));
        assertEquals(ast, parser.prog());
    }

    @Test
    public void testLines() {
        ASTParser parser = makeASTParser("gem heltal 3 som a");
        AST dcl = new HeltalDcl(new HeltalLiteral("3"), "a");
        //AST ast = new
    }
}
