package ASTVisitor.Parser;

import ASTVisitor.ASTnodes.*;


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
        System.out.println(n.type + " " + n.getVal().toString());
    }

    @Override
    public void visit(BinaryComputing n) {
        n.getChild1().accept(this);
        n.getChild2().accept(this);
        DataTypes type = generalize(n.getChild1().type, n.getChild2().type);
        n.setChild1(convert(n.getChild1(), type));
        n.setChild2(convert(n.getChild2(), type));
        n.type = type;
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

    }

    @Override
    public void visit(IfNode n) {
        n.getExpr().accept(this);
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
            error("Værdien " + n.getValue().toString() + " er ikke af typen " + DataTypes.DECIMALTAL);
        }
    }

    @Override
    public void visit(BoolskDcl n) {
        n.getValue().accept(this);
        if (n.getValue().type != DataTypes.BOOLSK) {
            error("Værdien " + n.getValue().toString() + " er ikke af typen " + DataTypes.BOOLSK);
        }
    }

    @Override
    public void visit(SymReferencing n) {
        n.type = AST.getSymbolTable().get(n.id);
    }

    private DataTypes generalize (DataTypes type1, DataTypes type2) {
        if (type1 == DataTypes.DECIMALTAL || type2 == DataTypes.DECIMALTAL) {
            return DataTypes.DECIMALTAL;
        } else if (type1 == DataTypes.HELTAL && type2 == DataTypes.HELTAL) {
            return DataTypes.HELTAL;
        } else if (type1 == DataTypes.TEKST && type2 == DataTypes.TEKST) {
            return DataTypes.TEKST;
        } else if (type1 == DataTypes.BOOLSK && type2 == DataTypes.BOOLSK) {
            return DataTypes.BOOLSK;
        } else {
            error("Ugyldig operation mellem type" + type1.toString() + "og type" + type2.toString());
        }
        return null;
    }

    private AST convert(AST n, DataTypes type) {
        if (n.type == DataTypes.DECIMALTAL && type == DataTypes.HELTAL) {
            error("Ikke muligt at konvertere decimaltal til integer");
        } else if (n.type == DataTypes.HELTAL && type == DataTypes.DECIMALTAL){
            return new ConvertToFloat(n);
        }
        return n;
    }

    private void error(String message) {
        throw new Error(message);
    }
}
