package ASTVisitor.ASTDrawing;

import ASTVisitor.Parser.AST;

import javax.swing.*;
import java.awt.*;

public class TreeDrawing extends JPanel {

    private AST tree;
    public TreeDrawing(AST tree) {
        this.tree = tree;
    }

    @Override
    public void paintComponent(Graphics g) {
        // Draw Tree Here
        tree.accept(new DrawVisitor(g));
    }

    public void draw() {
        JFrame jFrame = new JFrame();
        jFrame.add(new TreeDrawing(tree));
        jFrame.setTitle("EzIOT - AST");
        jFrame.setSize(1920, 1080);
        jFrame.setVisible(true);
    }

}
