package ASTVisitor.Parser;
import javax.xml.crypto.Data;
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

    public static void setSymbolTable(HashMap<String, DataTypes> symbolTable) {
        SymbolTable = symbolTable;
    }

    public DataTypes getType() {
        return type;
    }

    public void setType(DataTypes type) {
        this.type = type;
    }

    public static HashMap<String, DataTypes> getSymbolTable() {
        return SymbolTable;
    }

    @Override
    public String toString() {
        return "AST{" +
                "type=" + type +
                '}';
    }
}
