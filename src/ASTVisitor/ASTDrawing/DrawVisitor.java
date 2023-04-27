package ASTVisitor.ASTDrawing;

import ASTVisitor.ASTnodes.*;
import ASTVisitor.Parser.AST;
import ASTVisitor.Parser.Visitor;

import java.awt.*;

public class DrawVisitor extends Visitor {

    private Graphics graphics;
    private int xCoord;
    private int yCoord;
    
    private final int xJump = 100;

    private final int width;
    private final int height;
    public DrawVisitor(Graphics graphics) {
        this.graphics = graphics;
        this.graphics.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        xCoord = graphics.getClipBounds().width / 2 - 25;
        yCoord = 50;
        width = graphics.getClipBounds().width;
        height = graphics.getClipBounds().height;
    }

    private void drawCircle(String title) {
        graphics.drawOval(xCoord, yCoord, 50, 50);
        graphics.setColor(Color.BLUE);
        graphics.drawString(title, xCoord-(title.length()/2 - title.length()%2), yCoord-5);
        graphics.setColor(Color.BLACK);
    }

    private void incrementY() {
        yCoord += 100;
    }

    private void decrementY() {
        yCoord -= 100;
    }

    @Override
    public void visit(AssignNode n) {
        drawCircle("Assign");
        incrementY();
        graphics.drawLine(xCoord+25, yCoord, xCoord+25, yCoord - 50);
        n.getValue().accept(this);
        decrementY();
    }

    @Override
    public void visit(BinaryComputing n) {
        drawCircle("BinCom");
        graphics.drawString(n.getOperation().textual, xCoord+23-(n.getOperation().textual.length() * 2), yCoord+30);
        int temp = xCoord;
        incrementY();
        xCoord -= xJump/2;
        graphics.drawLine(xCoord+25, yCoord, temp+25, yCoord - 50);
        n.getChild1().accept(this);
        xCoord += xJump/2 * 2;
        graphics.drawLine(xCoord+25, yCoord, temp+25, yCoord - 50);
        n.getChild2().accept(this);
        decrementY();
    }

    @Override
    public void visit(BoolskLiteral n) {
        drawCircle("BoolskLit");
        graphics.drawString(n.getValue(), xCoord+25-(n.getValue().length() * 3), yCoord+30);
    }

    @Override
    public void visit(DecimaltalLiteral n) {
        drawCircle("DecLit");
        graphics.drawString(n.getValue(), xCoord+23-(n.getValue().length() * 3), yCoord+30);
    }

    @Override
    public void visit(FuncDclNode n) {
        drawCircle("FuncDclNode");
        incrementY();
        int temp = xCoord;

        if(!n.getBody().isEmpty()) {
            xCoord -= xJump * (n.getBody().size()-1);
        }
        for (AST child : n.getBody()) {
            graphics.drawLine(xCoord+25, yCoord, temp+25, yCoord - 50);
            child.accept(this);
            xCoord += xJump;
        }
        decrementY();
        xCoord = temp;
    }

    @Override
    public void visit(FuncNode n) {
        drawCircle("FuncNode");
    }

    @Override
    public void visit(HeltalLiteral n) {
        drawCircle("HeltalLit");
        graphics.drawString(n.getValue(), xCoord+23-(n.getValue().length() * 3), yCoord+30);
    }

    @Override
    public void visit(IdNode n) {
        drawCircle("IdNode");
        graphics.drawString(n.getName(), xCoord+23-(n.getName().length() * 3), yCoord+30);
    }

    @Override
    public void visit(IfNode n) {
        int temp = xCoord;
        drawCircle("IfNode");
        incrementY();
        if(!n.getBody().isEmpty()) {
            xCoord -= xJump * (n.getBody().size() / 2 + n.getBody().size() % 2);
            if (n.getExpr() instanceof UnaryComputing) xCoord += xJump / 2;
            graphics.drawLine(xCoord+25, yCoord, temp+25, yCoord - 50);
            n.getExpr().accept(this);
            xCoord += xJump;
        } else {
            graphics.drawLine(xCoord+25, yCoord, temp+25, yCoord - 50);
            n.getExpr().accept(this);
        }

        for (AST child : n.getBody()) {
            graphics.drawLine(xCoord+25, yCoord, temp+25, yCoord - 50);
            child.accept(this);
            xCoord += xJump;
        }
        decrementY();
        xCoord = temp;
    }

    @Override
    public void visit(LoopNode n) {
        drawCircle("LoopNode");
        graphics.drawString(n.getId(), xCoord+23-(n.getId().length() * 3), yCoord+20);
        //graphics.drawString(n.getRepeats().getValue(), xCoord+23-(n.getRepeats().getValue().length() * 2), yCoord+40);
        incrementY();
        graphics.drawLine(xCoord+25, yCoord, xCoord+25, yCoord - 50);
        n.getRepeats().accept(this);
        decrementY();
    }

    @Override
    public void visit(PrintNode n) {
        drawCircle("PrintNode");
        incrementY();
        graphics.drawLine(xCoord+25, yCoord, xCoord+25, yCoord - 50);
        n.getValue().accept(this);
        decrementY();
    }

    @Override
    public void visit(ProgramNode n) {
        drawCircle("ProgramNode");
        incrementY();
        if(n.getChild().size() != 0) {
            xCoord = width / (n.getChild().size()) - 25;
        }
        if (n.getChild().size() % 2 == 1) {
            xCoord -= width / n.getChild().size() / 2;
        }
        for (AST child : n.getChild()) {
            graphics.drawLine(xCoord+25, yCoord, width/2, yCoord - 50);
            child.accept(this);
            xCoord += width / n.getChild().size();
        }
        decrementY();
        xCoord = width / 2;
    }

    @Override
    public void visit(TekstLiteral n) {
        drawCircle("TekstLiteral");
        graphics.drawString(n.getValue(), xCoord+25, yCoord+30);
    }


    @Override
    public void visit(UnaryComputing n) {
        drawCircle("UnaryComputing");
        if (n.getOperation().equals(AST.Operators.PAREN)) {
            graphics.drawString("()", xCoord+23-(n.getOperation().textual.length() * 3), yCoord+30);
        } else {
            graphics.drawString(n.getOperation().textual, xCoord+23-(n.getOperation().textual.length() * 3), yCoord+30);
        }
        incrementY();
        graphics.drawLine(xCoord+25, yCoord, xCoord+25, yCoord - 50);
        n.getChild().accept(this);
        decrementY();
    }

    @Override
    public void visit(TekstDcl n) {
        drawCircle("TekstDcl");
        incrementY();
        graphics.drawLine(xCoord+25, yCoord, xCoord+25, yCoord - 50);
        n.getValue().accept(this);
        decrementY();
    }

    @Override
    public void visit(HeltalDcl n) {
        drawCircle("HeltalDcl");
        incrementY();
        graphics.drawLine(xCoord+25, yCoord, xCoord+25, yCoord - 50);
        n.getValue().accept(this);
        decrementY();
    }

    @Override
    public void visit(DecimaltalDcl n) {
        drawCircle("DecDcl");
        incrementY();
        graphics.drawLine(xCoord+25, yCoord, xCoord+25, yCoord - 50);
        n.getValue().accept(this);
        decrementY();
    }

    @Override
    public void visit(BoolskDcl n) {
        drawCircle("BoolskDcl");
        incrementY();
        graphics.drawLine(xCoord+25, yCoord, xCoord+25, yCoord - 50);
        n.getValue().accept(this);
        decrementY();
    }

    @Override
    public void visit(FieldDclNode n) {

    }

    @Override
    public void visit(FieldNode n) {

    }

    @Override
    public void visit(DeviceNode n) {

    }
}
