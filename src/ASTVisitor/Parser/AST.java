package ASTVisitor.Parser;
import java.util.Hashtable;

public abstract class AST {
    public static Hashtable<String,Integer> SymbolTable = new Hashtable<String,Integer>();

    public abstract void accept(Visitor v);

}

