package ASTVisitor.Parser;

import ASTVisitor.ASTnodes.*;
import ASTVisitor.Exceptions.*;

import java.util.ArrayList;
import java.util.List;

import static ASTVisitor.Parser.AST.*;

public class TypeChecker extends Visitor{

    private int i = 1;
    private boolean stringConcatenationAllowed = true;

    @Override
    public void visit(AssignNode n) {
        DataTypes type;
        // Generating id, whether it is a field or a simple id
        if (n.getId().getParentId() != null) {
            type = AST.getSymbolTable().get(n.getId().getParentId() + "." + n.getId().getValue());
        } else
            type = AST.getSymbolTable().get(n.getId().getValue());

        n.getValue().accept(this);

        if (type == null || !type.equals(n.getValue().getType())){
            try {
                if (n.getId().getParentId() == null)
                    throw new IllegalTypeAssignmentException(n.getId().getValue(), type, n.getValue(), n.getLine());
                else
                    throw new IllegalTypeAssignmentException(n.getId() + "." + n.getId().getParentId(), type, n.getValue(), n.getLine());
            } catch (NullPointerException e) {
                throw new MissingTypeException(n.getId(), n.getLine());
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
            n.type = AST.getSymbolTable().get(n.getParentId() + "." + n.getValue());
        else n.type = AST.getSymbolTable().get(n.getValue());
    }

    @Override
    public void visit(IfNode n) {
        n.setNum(i++);
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
    public void visit(DeviceNode n) {
        n.getFields().forEach((field) -> {
            field.accept(this);
        });
        n.type = DataTypes.DEVICE;
    }

    @Override
    public void visit(KlokkenNode n) {
        n.type = DataTypes.TID;
    }

    @Override
    public void visit(TidNode n) {
        n.type = DataTypes.TID;
    }

    @Override
    public void visit(TidDcl n) {
        String id;
        if (n.getId().getParentId() != null)
            id = n.getId().getParentId() + "." + n.getId().getValue();
        else id = n.getId().getValue();
        n.getValue().accept(this);
        if (n.getValue().type != DataTypes.TID)
            throw new IllegalTypeAssignmentException(id, DataTypes.TID, n.getValue(), n.getLine());
    }

    @Override
    public void visit(CommentNode commentNode) {

    }

    /**
     * Compares two DataTypes, and returns the type if they are the same. Otherwise, if one is a floating point
     * number and the other an integer, implicit type conversion is used. If this is not the case, an error is
     * thrown
     * @param type1 first DataType to compare
     * @param type2 second DataType to compare
     * @param operator the operation
     * @return a common datatype or float if int and float is given
     */
    public DataTypes findCommonDataType(DataTypes type1, DataTypes type2, Operators operator) {
        if (type1 == null || type2 == null) {
            throw new NullPointerException();
        }
        ArrayList<DataTypes> types = new ArrayList<>(List.of(type1, type2));
        if (type1 == type2) {
            return type1;
        } else if (types.contains(DataTypes.DECIMALTAL) && types.contains(DataTypes.HELTAL)){
            return DataTypes.DECIMALTAL;
        } else if (types.contains(DataTypes.TEKST) && types.contains(DataTypes.TID)) {
            return DataTypes.TID;
        } else throw new Error("Ugyldig operation: " + operator + " mellem type " + type1 + " og type " + type2);
    }

    public void error(String message) {
        throw new Error(message);
    }
}
