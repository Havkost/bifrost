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
        this.revalidate();
    }

    public void draw() {
        JFrame jFrame = new JFrame();
        jFrame.setTitle("EzIOT - AST");

        jFrame.add(new TreeDrawing(tree));

        jFrame.setSize(new Dimension(1920, 1080));
        //jFrame.pack();
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);
    }

}
