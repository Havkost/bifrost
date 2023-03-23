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

    }

    @Override
    public void visit(LoopNode n) {

    }

    @Override
    public void visit(PrintNode n) {

    }

    @Override
    public void visit(ProgramNode n) {

    }

    @Override
    public void visit(SymDeclaring n) {

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
    public void visit(TypeNode n) {

    }

    @Override
    public void visit(UnaryComputing n) {

    }

    @Override
    public void visit(TekstDcl n) {

    }

    @Override
    public void visit(HeltalDcl n) {

    }

    @Override
    public void visit(DecimaltalDcl n) {

    }

    @Override
    public void visit(BoolskDcl n) {

    }

    @Override
    public void visit(SymReferencing n) {

    }

    private DataTypes generalize (DataTypes type1, DataTypes type2) {
        if (type1 == DataTypes.DECIMALTAL || type2 == DataTypes.DECIMALTAL) {
            return DataTypes.DECIMALTAL;
        } else return DataTypes.HELTAL;
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
