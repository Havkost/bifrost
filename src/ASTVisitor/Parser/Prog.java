package ASTVisitor.Parser;

import java.util.ArrayList;

public class Prog extends Node {
    ArrayList<Node> child;

    Prog(ArrayList<Node> child) {this.child = child;}

    public void accept(Visitor v) {v.visit(this);}
}
