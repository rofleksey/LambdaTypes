import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

public class Apply extends Expression {
    final Expression e1, e2;

    Apply(Expression e1, Expression e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    @Override
    String toStringImpl() {
        return "(" + e1 + " " + e2 + ")";
    }

    @Override
    int hashCodeImpl() {
        return Objects.hash(e1, e2);
    }

    @Override
    void prepareLinks(HashMap<String, Lambda> map) {
        e1.prepareLinks(map);
        e2.prepareLinks(map);
    }

    @Override
    void replaceType(Equation eq) {
        e1.replaceType(eq);
        e2.replaceType(eq);
    }

    @Override
    void getEquations(EquationSystem system) {
        e1.getEquations(system);
        e2.getEquations(system);
        type = new TypeVar(system.getNewBoundVarType());
        system.addEquation(new Equation(e1.type, new TypeImpl(e2.type, type)));
        //System.out.println(toString()+" : "+type);
    }

    @Override
    void prepareTypes(EquationSystem system, HashMap<String, Type> typeMap, HashSet<String> boundVars, HashMap<String, Type> freeVarsTypes) throws TypeException {
        type = type.replace(system.getSolution());
        e1.prepareTypes(system, typeMap, boundVars, freeVarsTypes);
        e2.prepareTypes(system, typeMap, boundVars, freeVarsTypes);
    }

    @Override
    void printResult(EquationSystem system, String context, HashMap<String, Type> typeMap, int depth) {
        printHeader(depth);
        if(context.isEmpty()) {
            System.out.println("|- "+this+" : "+type+" [rule #2]");
        } else {
            System.out.println(context+" |- "+this+" : "+type+" [rule #2]");
        }
        e1.printResult(system, context, typeMap,depth+1);
        e2.printResult(system, context, typeMap, depth+1);
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
        Apply other = (Apply) o;
        return e1.equals(other.e1) && e2.equals(other.e2);
    }
}
