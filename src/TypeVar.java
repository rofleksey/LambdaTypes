import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class TypeVar extends Type {
    final String name;

    TypeVar(String name) {
        this.name = name;
    }

    public String toStr() {
        return name;
    }

    @Override
    int hashCodeImpl() {
        return Objects.hash(name);
    }

    @Override
    Case getCase() {
        return Case.VAR;
    }

    @Override
    Type getLeft() {
        return null;
    }

    @Override
    Type getRight() {
        return null;
    }

    @Override
    Type replace(String what, Type with) {
        return name.equals(what) ? with : this;
    }

    @Override
    Type replace(List<Equation> list) {
        for(Equation e : list) {
            if(e.left.toString().equals(name)) {
                return e.right;
            }
        }
        return this;
    }

    @Override
    boolean isVar() {
        return true;
    }

    @Override
    boolean contains(String name) {
        return name.equals(this.name);
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
        TypeVar other = (TypeVar) o;
        return name.equals(other.name);
    }
}
