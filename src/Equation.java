import java.util.Map;

public class Equation {
    final Type left, right;

    Equation(Type left, Type right) {
        this.left = left;
        this.right = right;
    }

    Equation replace(String what, Type with) {
        return new Equation(left.replace(what, with), right.replace(what, with));
    }

    @Override
    public String toString() {
        return left.toString() + " = " + right.toString();
    }
}
