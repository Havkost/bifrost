public class IntDcl extends SymDeclaring {
    IntDcl(String i) { id = i; }

    public void accept(Visitor v) {
        v.visit(this);
    }
}
