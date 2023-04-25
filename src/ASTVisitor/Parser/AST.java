package ASTVisitor.Parser;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class AST {

    public static HashMap<String,DataTypes> SymbolTable = new HashMap<String,DataTypes>();

    public enum DataTypes {
        DECIMALTAL,
        HELTAL,
        TEKST,
        BOOLSK,
        RUTINE
    }
    public enum Operators {
        PLUS("+", "+"),
        MINUS("-", "-"),
        TIMES("*", "*"),
        DIVISION("/", "/"),
        OR("eller", "||"),
        AND("og", "&&"),
        LESS_THAN("<", "<"),
        GREATER_THAN(">", ">"),
        EQUALS("er", "=="),
        NOT_EQUALS("ikke er", "!="),
        NOT("ikke", "!"),
        PAREN("paren", null);

        public final String textual;
        public final String Cversion;

        Operators(String textual, String Cversion){
            this.textual = textual;
            this.Cversion = Cversion;
        }
    }

    public DataTypes type = null;

    private static final DataTypes[][] operationResultType;

    static {
        operationResultType = new DataTypes[Operators.values().length][DataTypes.values().length];
        // Plus operations mapped to the datatype result
        operationResultType[Operators.PLUS.ordinal()][DataTypes.HELTAL.ordinal()] = DataTypes.HELTAL;
        operationResultType[Operators.PLUS.ordinal()][DataTypes.DECIMALTAL.ordinal()] = DataTypes.DECIMALTAL;
        operationResultType[Operators.PLUS.ordinal()][DataTypes.TEKST.ordinal()] = DataTypes.TEKST;
        // Minus operations mapped to the datatype result
        operationResultType[Operators.MINUS.ordinal()][DataTypes.HELTAL.ordinal()] = DataTypes.HELTAL;
        operationResultType[Operators.MINUS.ordinal()][DataTypes.DECIMALTAL.ordinal()] = DataTypes.DECIMALTAL;
        // Times operations mapped to the datatype result
        operationResultType[Operators.TIMES.ordinal()][DataTypes.HELTAL.ordinal()] = DataTypes.HELTAL;
        operationResultType[Operators.TIMES.ordinal()][DataTypes.DECIMALTAL.ordinal()] = DataTypes.DECIMALTAL;
        // Division operations mapped to the datatype result
        operationResultType[Operators.DIVISION.ordinal()][DataTypes.HELTAL.ordinal()] = DataTypes.DECIMALTAL;
        operationResultType[Operators.DIVISION.ordinal()][DataTypes.DECIMALTAL.ordinal()] = DataTypes.DECIMALTAL;
        // Less than operations mapped to the datatype result
        operationResultType[Operators.LESS_THAN.ordinal()][DataTypes.HELTAL.ordinal()] = DataTypes.BOOLSK;
        operationResultType[Operators.LESS_THAN.ordinal()][DataTypes.DECIMALTAL.ordinal()] = DataTypes.BOOLSK;
        // Greater than operations mapped to the datatype result
        operationResultType[Operators.GREATER_THAN.ordinal()][DataTypes.HELTAL.ordinal()] = DataTypes.BOOLSK;
        operationResultType[Operators.GREATER_THAN.ordinal()][DataTypes.DECIMALTAL.ordinal()] = DataTypes.BOOLSK;
        // Equals operations mapped to the datatype result
        operationResultType[Operators.EQUALS.ordinal()][DataTypes.HELTAL.ordinal()] = DataTypes.BOOLSK;
        operationResultType[Operators.EQUALS.ordinal()][DataTypes.DECIMALTAL.ordinal()] = DataTypes.BOOLSK;
        operationResultType[Operators.EQUALS.ordinal()][DataTypes.TEKST.ordinal()] = DataTypes.BOOLSK;
        operationResultType[Operators.EQUALS.ordinal()][DataTypes.BOOLSK.ordinal()] = DataTypes.BOOLSK;
        // Not equals operations mapped to the datatype result
        operationResultType[Operators.NOT_EQUALS.ordinal()][DataTypes.HELTAL.ordinal()] = DataTypes.BOOLSK;
        operationResultType[Operators.NOT_EQUALS.ordinal()][DataTypes.DECIMALTAL.ordinal()] = DataTypes.BOOLSK;
        operationResultType[Operators.NOT_EQUALS.ordinal()][DataTypes.TEKST.ordinal()] = DataTypes.BOOLSK;
        operationResultType[Operators.NOT_EQUALS.ordinal()][DataTypes.BOOLSK.ordinal()] = DataTypes.BOOLSK;
        // Not operations mapped to the datatype result
        operationResultType[Operators.NOT.ordinal()][DataTypes.BOOLSK.ordinal()] = DataTypes.BOOLSK;
        // Parenthesis operations mapped to the datatype result
        operationResultType[Operators.PAREN.ordinal()][DataTypes.HELTAL.ordinal()] = DataTypes.HELTAL;
        operationResultType[Operators.PAREN.ordinal()][DataTypes.DECIMALTAL.ordinal()] = DataTypes.DECIMALTAL;
        operationResultType[Operators.PAREN.ordinal()][DataTypes.TEKST.ordinal()] = DataTypes.TEKST;
        operationResultType[Operators.PAREN.ordinal()][DataTypes.BOOLSK.ordinal()] = DataTypes.BOOLSK;
        // Or operations mapped to the datatype result
        operationResultType[Operators.OR.ordinal()][DataTypes.BOOLSK.ordinal()] = DataTypes.BOOLSK;
        // And operations mapped to the datatype result
        operationResultType[Operators.AND.ordinal()][DataTypes.BOOLSK.ordinal()] = DataTypes.BOOLSK;
    }
    public abstract void accept(Visitor v);

    public DataTypes getType() {
        return type;
    }

    public void setType(DataTypes type) {
        this.type = type;
    }

    public static HashMap<String, DataTypes> getSymbolTable() {
        return SymbolTable;
    }

    public static void setSymbolTable(HashMap<String, DataTypes> symbolTable) {
        SymbolTable = symbolTable;
    }

    public static DataTypes getOperationResultType(Operators operator, DataTypes type) {
        return operationResultType[operator.ordinal()][type.ordinal()];
    }

    public static void clearSymbolTable() {
        setSymbolTable(new HashMap<String, DataTypes>());
    }

}
