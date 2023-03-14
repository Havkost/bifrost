package ASTVisitor.Parser;
import java.util.Hashtable;

public abstract class Node {


    public static Hashtable<String,Integer> symbolTable = new Hashtable<String,Integer>();

    Node(){

    }

    public abstract void accept(Visitor v);

}

