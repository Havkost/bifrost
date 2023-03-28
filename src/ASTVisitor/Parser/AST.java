package ASTVisitor.Parser;
import java.util.HashMap;

public abstract class AST {
    public static HashMap<String,DataTypes> SymbolTable = new HashMap<String,DataTypes>();

    public enum DataTypes {
        DECIMALTAL,
        HELTAL,
        TEKST,
        BOOLSK,
        RUTINE
    }
    public DataTypes type = null;
    public abstract void accept(Visitor v);

    public static HashMap<String, DataTypes> getSymbolTable() {
        return SymbolTable;
    }
}
