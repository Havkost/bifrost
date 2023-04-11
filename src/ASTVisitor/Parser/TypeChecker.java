package ASTVisitor.Parser;

import ASTVisitor.ASTnodes.*;
import java.util.ArrayList;
import java.util.List;

import static ASTVisitor.Parser.AST.*;

public class TypeChecker extends Visitor{

    // TODO: Check at parenteser bliver checket korrekt
    @Override
    public void visit(AssignNode n) {
        n.getValue().accept(this);
        if (AST.getSymbolTable().get(n.getId()) != n.getValue().getType()){
            error("Kan ikke sætte typen: "+ AST.getSymbolTable().get(n.getId()) + " på ID: " + n.getId() + " til at have værdien: " +  n.getValue().getType());
        }
    }

    @Override
    public void visit(BinaryComputing n) {
        n.getChild1().accept(this);
        n.getChild2().accept(this);
        DataTypes type = findCommonDataType(n.getChild1().type, n.getChild2().type, n.getOperation());
        if (type != null) {
            DataTypes resultType = getOperationResultType(n.getOperation(), type);
            if(resultType != null) {
                n.setType(resultType);
            } else error("Kan ikke bruge operatoren '" + n.getOperation() + "' på typen " + type);
        } else error("Der opstod en fejl, din computationstype er: " + type);
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
        for(AST ast : n.getBody()) {
            ast.accept(this);
        }
    }

    @Override
    public void visit(FuncNode n) {
        if (AST.getSymbolTable().get(n.getId()) != DataTypes.RUTINE){
            error("Rutine kaldet med navn: " + n.getId() + " er ikke en rutine");
        }
    }

    @Override
    public void visit(IdNode n) {
        n.type = AST.getSymbolTable().get(n.getName());
    }

    @Override
    public void visit(IfNode n) {
        n.getExpr().accept(this);
        if (n.getExpr().type != DataTypes.BOOLSK) {
            error("Typen på expression skal være boolsk, og må ikke være " + n.getExpr().type);
        }
        for(AST ast : n.getBody()) {
            ast.accept(this);
        }
    }

    @Override
    public void visit(LoopNode n) {
        n.getRepeats().accept(this);
        if(AST.getSymbolTable().get(n.getId()) != DataTypes.RUTINE) {
            error("Id'et: " + n.getId() + " er ikke af typen: " + DataTypes.RUTINE);
        }
        if(n.getRepeats().type != DataTypes.HELTAL) {
            error("Værdien: " + n.getRepeats() + " er ikke af typen: " + DataTypes.HELTAL);
        }
    }

    @Override
    public void visit(PrintNode n) {
        n.getValue().accept(this);
    }

    @Override
    public void visit(ProgramNode n) {
        for(AST ast : n.getChild()) {
            ast.accept(this);
        }
    }

    @Override
    public void visit(UnaryComputing n) {
        n.getChild().accept(this);
        DataTypes resultType = getOperationResultType(n.getOperation(), n.getChild().type);
        if(resultType != null) {
            n.setType(resultType);
        } else error("Kan ikke bruge operatoren '" + n.getOperation() + "' på typen " + n.getChild().type);
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
            error("Værdien " + n.getValue().type.toString() + " er ikke af typen " + DataTypes.BOOLSK);
        }
    }

    private DataTypes findCommonDataType(DataTypes type1, DataTypes type2, Operators operator) {
        ArrayList<DataTypes> types = new ArrayList<>(List.of(type1, type2));
        if (type1 == type2) {
            return type1;
        } else if (types.contains(DataTypes.DECIMALTAL) && types.contains(DataTypes.HELTAL)){
            return DataTypes.DECIMALTAL;
        } else error("Ugyldig operation: " + operator + " mellem type " + type1 + " og type " + type2);
        return null;
    }

    private void error(String message) {
        throw new Error(message);
    }
}
