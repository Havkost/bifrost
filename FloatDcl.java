public class FloatDcl extends SymDeclaring {
    FloatDcl(String i) { id = i; }

    public void accept(Visitor v) {
        v.visit(this);
    }
}
