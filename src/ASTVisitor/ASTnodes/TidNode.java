package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.AST;
import ASTVisitor.Parser.Visitor;

public class TidNode extends AST {
    private int hour;
    private int minute;

    public TidNode(int hour, int minute, int line) {
        super(line);
        this.hour = hour;
        this.minute = minute;
        this.type = DataTypes.TID;
    }

    public TidNode(int hour, int minute) {
        super(0);
        this.hour = hour;
        this.minute = minute;
        this.type = DataTypes.TID;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
