import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class TypeImpl extends Type {
    private final Type left, right;

    TypeImpl(Type left, Type right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String toStr() {
        return "("+left+" -> " +right+")";
    }

    @Override
    int hashCodeImpl() {
        return Objects.hash(left, right);
    }

    @Override
    Case getCase() {
        return Case.IMPL;
    }

    @Override
    Type getLeft() {
        return left;
    }

    @Override
    Type getRight() {
        return right;
    }

    @Override
    Type replace(String what, Type with) {
        return new TypeImpl(left.replace(what, with), right.replace(what, with));
    }

    @Override
    Type replace(List<Equation> list) {
        return new TypeImpl(left.replace(list), right.replace(list));
    }

    @Override
    boolean isVar() {
        return false;
    }

    @Override
    boolean contains(String what) {
        return left.contains(what) || right.contains(what);
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        }
        if(o == null || !getClass().equals(o.getClass())) {
            return false;
        }
        if(hashCode() != o.hashCode()) {
            return false;
        }
        TypeImpl other = (TypeImpl) o;
        return left.equals(other.left) && right.equals(other.right);
    }
}
