package ASTVisitor.Parser;

import ASTVisitor.ASTnodes.*;


import javax.xml.crypto.Data;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static ASTVisitor.Parser.AST.*;

public class TypeChecker extends Visitor{

    @Override
    public void visit(ConvertToFloat n) {
        n.getChild().accept(this);
        n.type = DataTypes.DECIMALTAL;
    }

    @Override
    public void visit(AssignNode n) {
        n.getVal().accept(this);
        DataTypes lhsType = AST.getSymbolTable().get(n.getId());
        DataTypes rhsType = generalize(n.getVal().type, lhsType);
        n.setVal(convert(n.getVal(), lhsType));
        n.type = rhsType;
    }

    @Override
    public void visit(BinaryComputing n) {
        n.getChild1().accept(this);
        n.getChild2().accept(this);
        DataTypes type = generalize(n.getChild1().type, n.getChild2().type);
        if (type != null) {
            n.type = type;
        } else error("The type from generalize returned: " + type);
    }

    @Override
    public void visit(BoolskLiteral n) {
        n.type = DataTypes.BOOLSK;
    }

    @Override
    public void visit(HeltalLiteral n) {
        n.type = DataTypes.HELTAL;
    }

    @Override
    public void visit(TekstLiteral n) {
        n.type = DataTypes.TEKST;
    }

    @Override
    public void visit(DecimaltalLiteral n) {
        n.type = DataTypes.DECIMALTAL;
    }

    @Override
    public void visit(FuncDclNode n) {
    }

    @Override
    public void visit(FuncNode n) {
    }

    @Override
    public void visit(IdNode n) {
        n.type = AST.getSymbolTable().get(n.getName());
    }

    @Override
    public void visit(IfNode n) {
        n.getExpr().accept(this);
        for(AST ast : n.getChildren()) {
            ast.accept(this);
        }
    }

    @Override
    public void visit(LoopNode n) {

    }

    @Override
    public void visit(PrintNode n) {
        //n.type = AST.getSymbolTable().get(n.getValue());
    }

    @Override
    public void visit(ProgramNode n) {
        for(AST ast : n.getChild()) {
            ast.accept(this);
        }
    }

    @Override
    public void visit(SymDeclaring n) {

    }

    @Override
    public void visit(TypeNode n) {

    }
    @Override
    public void visit(UnaryComputing n) {

    }

    @Override
    public void visit(TekstDcl n) {
        n.getValue().accept(this);
        if (n.getValue().type != DataTypes.TEKST) {
            error("Værdien " + n.getValue().toString() + " er ikke af typen " + DataTypes.TEKST);
        }
    }

    @Override
    public void visit(HeltalDcl n) {
        n.getValue().accept(this);
        if (n.getValue().type != DataTypes.HELTAL) {
            error("Værdien " + n.getValue().toString() + " er ikke af typen " + DataTypes.HELTAL);
        }
    }

    @Override
    public void visit(DecimaltalDcl n) {
        n.getValue().accept(this);
        if (n.getValue().type != DataTypes.DECIMALTAL) {
            error("Værdien " + n.getValue().type.toString() + " er ikke af typen " + DataTypes.DECIMALTAL);
        }
    }

    @Override
    public void visit(BoolskDcl n) {
        n.getValue().accept(this);
        if (n.getValue().type != DataTypes.BOOLSK) {
            error("Værdien " + n.getValue() + " er af typen: " + n.getValue().type + ", og det skulle have været af typen " + DataTypes.BOOLSK);
        }
    }

    @Override
    public void visit(SymReferencing n) {
        n.type = AST.getSymbolTable().get(n.id);
    }

    // TODO: de her if statements er ret grimme, så måske lige fix
    private DataTypes generalize (DataTypes type1, DataTypes type2) {
        ArrayList<DataTypes> types = new ArrayList<>(List.of(type1, type2));
        if (type1 == type2) {
            return type1;
        } else if (types.contains(DataTypes.DECIMALTAL) && types.contains(DataTypes.HELTAL)){
            return DataTypes.DECIMALTAL;
        } else error("Ugyldig operation mellem type " + type1.toString() + " og type " + type2.toString());
        return null;
    }

    private AST convert(AST n, DataTypes type) {
        if (n.type == DataTypes.DECIMALTAL && type == DataTypes.HELTAL) {
            error("Ikke muligt at konvertere decimaltal til heltal");
        } else if (n.type == DataTypes.HELTAL && type == DataTypes.DECIMALTAL){
            return new ConvertToFloat(n);
        }
        return n;
    }

    private void error(String message) {
        throw new Error(message);
    }
}
