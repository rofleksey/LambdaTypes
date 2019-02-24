import java.util.HashMap;
import java.util.HashSet;

public class Var extends Expression {
    final String name;
    Lambda link;

    Var(String name) {
        this.name = name;
    }

    @Override
    String toStringImpl() {
        return name;
    }

    @Override
    int hashCodeImpl() {
        return name.hashCode();
    }

    @Override
    void prepareLinks(HashMap<String, Lambda> map) {
        if(map.containsKey(name)) {
            link = map.get(name);
        }
    }

    @Override
    void replaceType(Equation eq) {
        if(name.equals(eq.left.toString())) {
            type = eq.right;
        }
    }

    @Override
    void getEquations(EquationSystem system) {
        if(link == null) {
            type = new TypeVar(system.getFreeVarType(name));
        } else {
            if(link.varType == null) {
                link.varType = new TypeVar(system.getNewBoundVarType());
            }
            type = link.varType;
        }
        //System.out.println(toString()+" : "+type);
    }

    @Override
    void prepareTypes(EquationSystem system, HashMap<String, Type> typeMap, HashSet<String> boundVars, HashMap<String, Type> freeVarsTypes) throws TypeException {
        type = type.replace(system.getSolution());
        typeMap.put(name, type);
        if(!boundVars.contains(name)) {
            freeVarsTypes.put(name, type);
        }
    }

    @Override
    void printResult(EquationSystem system, String context, HashMap<String, Type> typeMap, int depth){
        printHeader(depth);
        String temp = this+" : "+type;
        if(context.isEmpty()) {
            System.out.println(temp+" |- "+temp+" [rule #1]");
        } else {
            System.out.println(context + " |- " + temp + " [rule #1]");
        }
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
        Var other = (Var) o;
        return name.equals(other.name);
    }
}
