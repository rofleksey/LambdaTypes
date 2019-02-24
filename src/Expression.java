import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public abstract class Expression {
    private int hashCodeCache;
    private String cachedString;
    Type type;

    abstract String toStringImpl();
    abstract int hashCodeImpl();
    abstract void prepareLinks(HashMap<String, Lambda> map);
    abstract void replaceType(Equation eq);
    abstract void getEquations(EquationSystem system);
    abstract void prepareTypes(EquationSystem system, HashMap<String, Type> typeMap, HashSet<String> boundVars, HashMap<String, Type> freeVarsTypes) throws TypeException;
    abstract void printResult(EquationSystem system, String context, HashMap<String, Type> typeMap, int depth);

    void prepareLinks() {
        prepareLinks(new HashMap<>());
    }

    void printHeader(int depth) {
        //type = system.apply(type);
        for(int i = 0; i < depth; i++) {
            System.out.print("*   ");
        }
    }

    void prepareTypes(EquationSystem system, HashMap<String, Type> typeMap, HashMap<String, Type> freeVarsTypes) throws TypeException {
        prepareTypes(system, typeMap, new HashSet<>(), freeVarsTypes);
    }

    void printResult(EquationSystem system, String context, HashMap<String, Type> typeMap) {
        printResult(system, context, typeMap, 0);
    }

    @Override
    public String toString() {
        if(cachedString == null) {
            cachedString = toStringImpl();
        }
        return cachedString;
    }

    @Override
    public int hashCode() {
        if(hashCodeCache == 0) {
            hashCodeCache = hashCodeImpl();
        }
        return hashCodeCache;
    }
}
