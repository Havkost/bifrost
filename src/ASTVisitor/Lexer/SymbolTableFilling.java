package ASTVisitor.Lexer;

import ASTVisitor.Visitor;

public class SymbolTableFilling extends Visitor {
    @Override
    void visit(Assigning n) {
        n.child1.accept(this);
    }

    @Override
    void visit(FloatDcl n) {
    if(AST.SymbolTable.get(n.id) == null) AST.SymbolTable.put(n.id, TokenType.DECIMALTAL);
    else error("variable " + n.id + " is already declared");
    }

    @Override
    void visit(IntDcl n) {
    if(AST.SymbolTable.get(n.id) == null) AST.SymbolTable.put(n.id,TokenType.HELTAL);
    else error("variable " + n.id + " is already declared");
    }

    @Override
    void visit(SymDeclaring n) {

    }

    @Override
    void visit(Computing n) {
        n.child1.accept(this);
        n.child2.accept(this);
    }

    private void error(String message) {
        throw new Error(message);
    }
}
