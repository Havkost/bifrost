package ASTVisitor.Parser;

import ASTVisitor.ASTnodes.*;
import ASTVisitor.Exceptions.*;

import java.util.ArrayList;
import java.util.List;

import static ASTVisitor.Parser.AST.*;

public class TypeChecker extends Visitor{

    private boolean stringConcatenationAllowed = true;

    @Override
    public void visit(AssignNode n) {
        n.getValue().accept(this);
        DataTypes type;
        if (n.getId() instanceof FieldNode) {
            type = AST.getSymbolTable().get(((FieldNode) n.getId()).getParentId() + "." + ((FieldNode) n.getId()).getId());
        } else
            type = AST.getSymbolTable().get(((IdNode) n.getId()).getId());

        if (type != n.getValue().getType()){
            try {
                if (n.getId() instanceof IdNode)
                    throw new IllegalTypeAssignmentException(((IdNode) n.getId()).getId(), type, n.getValue(), n.getLine());
                else
                    throw new IllegalTypeAssignmentException(((FieldNode) n.getId()), type, n.getValue(), n.getLine());
            } catch (NullPointerException e) {
                if (n.getValue() instanceof BinaryComputing)
                    throw new MissingTypeException((BinaryComputing) n.getValue(), n.getLine());
                else if (n.getValue() instanceof UnaryComputing)
                    throw new MissingTypeException((UnaryComputing) n.getValue(), n.getLine());
                else throw new MissingTypeException(n.getValue(), n.getLine());
            }

        }
    }

    @Override
    public void visit(BinaryComputing n) {
        n.getChild1().accept(this);
        n.getChild2().accept(this);
        DataTypes type;

        if(n.getChild1().type == DataTypes.RUTINE || n.getChild2().type == DataTypes.RUTINE) {
            throw new IllegalOperationTypeException(n.getOperation(), DataTypes.RUTINE, n.getLine());
        }

        try {
            type = findCommonDataType(n.getChild1().type, n.getChild2().type, n.getOperation());
        } catch (NullPointerException e) {
            throw new MissingTypeException(n, n.getLine());
        }

        DataTypes resultType = getOperationResultType(n.getOperation(), type);
        if(resultType != null && (resultType != DataTypes.TEKST || stringConcatenationAllowed)) {
            n.setType(resultType);
        } else if (resultType == DataTypes.TEKST) {
            throw new IllegalStringConcatenationException(n.getLine());
        } else throw new IllegalOperationTypeException(n.getOperation(), type, n.getLine());
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
        n.setType(DataTypes.RUTINE);
        if(n.getBody() == null) return;
        for(AST ast : n.getBody()) {
            ast.accept(this);
        }
    }

    @Override
    public void visit(FuncNode n) {
        if (AST.getSymbolTable().get(n.getId()) != DataTypes.RUTINE) {
            throw new UnexpectedTypeException(n.getId(), n.getType(), DataTypes.RUTINE, n.getLine());
        }
        n.setType(DataTypes.RUTINE);
    }

    @Override
    public void visit(IdNode n) {
        if (n.getParentId() != null)
            n.type = AST.getSymbolTable().get(n.getParentId() + "." + n.getId());
        else n.type = AST.getSymbolTable().get(n.getId());
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
        } else throw new MissingTypeException(n, n.getLine());
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
    public void visit(FieldNode n) {
        n.type = AST.getSymbolTable().get(n.getParentId() + "." + n.getId());
    }

    @Override
    public void visit(DeviceNode n) {
        n.getFields().forEach((field) -> {
            field.accept(this);
        });
        n.type = DataTypes.DEVICE;
    }

    @Override
    public void visit(FieldDclNode n) {
        if (AST.getSymbolTable().get(n.getId()) != DataTypes.HELTAL || AST.getSymbolTable().get(n.getId()) != DataTypes.DECIMALTAL || AST.getSymbolTable().get(n.getId()) != DataTypes.BOOLSK || AST.getSymbolTable().get(n.getId()) != DataTypes.TEKST){
            // TODO: throw new UnexpectedTypeException(n.getId(),  n.getLine());
        }
        n.getValue().accept(this);
        n.type = n.getValue().type;
    }

    public DataTypes findCommonDataType(DataTypes type1, DataTypes type2, Operators operator) {
        if (type1 == null || type2 == null) {
            throw new NullPointerException();
        }
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
