import java.util.HashSet;
import java.util.List;
import java.util.Map;

public abstract class Type {
    private int hashCodeCache;

    enum Case {
        IMPL, VAR
    }

    abstract String toStr();
    abstract int hashCodeImpl();
    abstract Case getCase();
    abstract Type getLeft();
    abstract Type getRight();
    abstract Type replace(String what, Type with);
    abstract Type replace(List<Equation> list);
    abstract boolean isVar();
    abstract boolean contains(String what);

    @Override
    public int hashCode() {
        if(hashCodeCache == 0) {
            hashCodeCache = hashCodeImpl();
        }
        return hashCodeCache;
    }

    @Override
    public String toString() {
        return toStr();
    }
}
