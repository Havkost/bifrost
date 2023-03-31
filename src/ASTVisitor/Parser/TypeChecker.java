package ASTVisitor.Parser;

import ASTVisitor.ASTnodes.*;


import javax.xml.crypto.Data;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ASTVisitor.Parser.AST.*;

public class TypeChecker extends Visitor{


    // TODO: Bliver ikke brugt, skal den bruges i 2. iteration?
    @Override
    public void visit(ConvertToFloat n) {
        n.getChild().accept(this);
        n.type = DataTypes.DECIMALTAL;
    }

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
        DataTypes type = generalize(n.getChild1().type, n.getChild2().type);
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
        if(n.getType() != AST.getSymbolTable().get(n.getType())) {
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
    public void visit(SymDeclaring n) {
    }

    // TODO Skal slettes!
    @Override
    public void visit(TypeNode n) {

    }

    @Override
    public void visit(UnaryComputing n) {
        // Refactor if other unary operations are introduced
        n.getChild().accept(this);
        if(n.type != DataTypes.BOOLSK) error("Kan kun bruge 'ikke'-operator på boolske værdier.");
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

    @Override
    public void visit(SymReferencing n) {
        n.type = AST.getSymbolTable().get(n.id);
    }

    // TODO: Better name please + include operator in error msg (perhaps move this code back into BinaryComputing?
    private DataTypes generalize (DataTypes type1, DataTypes type2) {
        ArrayList<DataTypes> types = new ArrayList<>(List.of(type1, type2));
        if (type1 == type2) {
            return type1;
        } else if (types.contains(DataTypes.DECIMALTAL) && types.contains(DataTypes.HELTAL)){
            return DataTypes.DECIMALTAL;
        } else error("Ugyldig operation mellem type " + type1 + " og type " + type2);
        return null;
    }

    // TODO: Bliver ikke brugt, skal den bruges i 2. iteration?
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
