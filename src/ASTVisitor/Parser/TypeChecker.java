package ASTVisitor.Parser;

import ASTVisitor.ASTnodes.*;
import ASTVisitor.Exceptions.*;

import java.util.ArrayList;
import java.util.List;

import static ASTVisitor.Parser.AST.*;

public class TypeChecker extends Visitor{

    private boolean stringConcatenationAllowed = true;
    // TODO: Check at parenteser bliver checket korrekt
    @Override
    public void visit(AssignNode n) {
        n.getValue().accept(this);
        if (AST.getSymbolTable().get(n.getId()) != n.getValue().getType()){
            throw new IllegalTypeAssignmentException(n.getId(), AST.getSymbolTable().get(n.getId()), n.getValue());
        }
    }

    @Override
    public void visit(BinaryComputing n) {
        n.getChild1().accept(this);
        n.getChild2().accept(this);
        DataTypes type = findCommonDataType(n.getChild1().type, n.getChild2().type, n.getOperation());
        DataTypes resultType = getOperationResultType(n.getOperation(), type);
        if(resultType != null && resultType != DataTypes.TEKST || stringConcatenationAllowed) {
            n.setType(resultType);
        } else if (resultType == DataTypes.TEKST) {
            throw new IllegalStringConcatenationException();
        } else throw new IllegalOperationTypeException(n.getOperation(), type);
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
        if (AST.getSymbolTable().get(n.getId()) != DataTypes.RUTINE) {
            throw new UnknownSubroutineException(n.getId());
        }
    }

    @Override
    public void visit(IdNode n) {
        n.type = AST.getSymbolTable().get(n.getName());
    }

    @Override
    public void visit(IfNode n) {
        stringConcatenationAllowed = false;
        n.getExpr().accept(this);
        stringConcatenationAllowed = true;
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
        } else if(n.getRepeats().type != DataTypes.HELTAL) {
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

    public DataTypes findCommonDataType(DataTypes type1, DataTypes type2, Operators operator) {
        ArrayList<DataTypes> types = new ArrayList<>(List.of(type1, type2));
        if (type1 == type2) {
            return type1;
        } else if (types.contains(DataTypes.DECIMALTAL) && types.contains(DataTypes.HELTAL)){
            return DataTypes.DECIMALTAL;
        } else throw new Error("Ugyldig operation: " + operator + " mellem type " + type1 + " og type " + type2);
    }

    public void error(String message) {
        throw new Error(message);
    }
}
