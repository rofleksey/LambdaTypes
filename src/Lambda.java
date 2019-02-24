import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

public class Lambda extends Expression {
    final String var;
    final Expression body;
    Type varType;

    Lambda(String var, Expression body) {
        this.var = var;
        this.body = body;
    }

    @Override
    String toStringImpl() {
        return "(\\" + var + ". " + body + ")";
    }

    @Override
    int hashCodeImpl() {
        return Objects.hash(var, body);
    }

    @Override
    void prepareLinks(HashMap<String, Lambda> map) {
        map.put(var, this);
        body.prepareLinks(map);
        map.remove(var);
    }

    @Override
    void replaceType(Equation eq) {
        body.replaceType(eq);
    }

    @Override
    void getEquations(EquationSystem system) {
        body.getEquations(system);
        type = new TypeImpl(new TypeVar(varType == null ? system.getFreeVarType(var) : varType.toString()), body.type);
        //System.out.println(toString()+" : "+type);
    }

    @Override
    void prepareTypes(EquationSystem system, HashMap<String, Type> typeMap, HashSet<String> boundVars, HashMap<String, Type> freeVarsTypes) throws TypeException {
        type = type.replace(system.getSolution());
        if(varType != null) {
            varType = varType.replace(system.getSolution());
        }
        boundVars.add(var);
        body.prepareTypes(system, typeMap, boundVars, freeVarsTypes);
        boundVars.remove(var);
    }

    @Override
    void printResult(EquationSystem system, String context, HashMap<String, Type> typeMap, int depth){
        printHeader(depth);
        if(context.isEmpty()) {
            System.out.println("|- "+ (this + " : " + type) +" [rule #3]");
        } else {
            System.out.println(context+" |- "+ (this + " : " + type) +" [rule #3]");
        }
        String tempType = varType != null ? (var+" : "+varType) : (var + " : " + system.getFreeVarType(var));
        if(context.isEmpty()) {
            body.printResult(system, tempType, typeMap, depth+1);
        } else {
            body.printResult(system, context+", "+ tempType, typeMap, depth+1);
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
        Lambda other = (Lambda) o;
        return var.equals(other.var) && body.equals(other.body);
    }
}
