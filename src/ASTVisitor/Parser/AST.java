package ASTVisitor.Parser;
import java.util.Hashtable;

public abstract class AST {
    public static Hashtable<String,DataTypes> SymbolTable = new Hashtable<String,DataTypes>();

    enum DataTypes {
        DECIMALTAL,
        HELTAL,
        TEKST,
        BOOLSK
    }

    public abstract void accept(Visitor v);

    public static Hashtable<String, DataTypes> getSymbolTable() {
        return SymbolTable;
    }
}
