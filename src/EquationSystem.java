import java.util.*;

public class EquationSystem {
    private LinkedList<Equation> list = new LinkedList<>();
    private LinkedList<Equation> tempList = new LinkedList<>();
    private int varNameCounter = 0;
    private HashSet<String> temp = new HashSet<>();
    private HashMap<String, String> freeVarMap = new HashMap<>();

    void addEquation(Equation eq) {
        list.add(eq);
    }

    String getNewBoundVarType() {
        return "t" + varNameCounter++;
    }

    String getFreeVarType(String var) {
        if(freeVarMap.containsKey(var)) {
            return freeVarMap.get(var);
        }
        String name = "f" + varNameCounter++;
        freeVarMap.put(var, name);
        return name;
    }

    List<Equation> getSolution() {
        return list;
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(", ");
        list.forEach(it -> joiner.add(it.toString()));
        return joiner.toString();
    }

    private boolean isSolved() {
        temp.clear();
        for (Equation e : list) {
            if (!e.left.isVar()) {
                return false;
            } else {
                String var = e.left.toString();
                if(!temp.add(var)) {
                    return false;
                }
            }
        }
        for (Equation e : list) {
            for (String s : temp) {
                if (e.right.contains(s)) {
                    return false;
                }
            }
        }
        return true;
    }

    boolean solve() {
        while (!isSolved()) {
            //System.out.println(">>");
            //System.out.println(this);
            //System.out.println(">>");
            Equation eq = list.removeFirst();
            if (!eq.left.isVar() && eq.right.isVar()) {
                //System.out.println("RULE2");
                addEquation(new Equation(eq.right, eq.left));
                continue;
            } else if (eq.left.equals(eq.right)) {
                //System.out.println("RULE3");
                continue;
            } else if (eq.left.getCase() == Type.Case.IMPL && eq.right.getCase() == Type.Case.IMPL) {
                //System.out.println("RULE1");
                addEquation(new Equation(eq.left.getLeft(), eq.right.getLeft()));
                addEquation(new Equation(eq.left.getRight(), eq.right.getRight()));
                continue;
            } else if (eq.left.isVar()) {
                //System.out.println("RULE4");
                String name = eq.left.toString();
                //System.out.println("?" + eq);
                if (eq.right.contains(name)) {
                    return false;
                }
                tempList.clear();
                Iterator<Equation> iter = list.iterator();
                while (iter.hasNext()) {
                    Equation e = iter.next();
                    if (e.left.contains(name) || e.right.contains(name)) {
                        tempList.add(e.replace(name, eq.right));
                        iter.remove();
                    }
                }
                addEquation(eq);
                list.addAll(tempList);
                continue;
            }
            //System.out.println("NOTHING");
            addEquation(eq);
        }
        //System.out.println("DONE");
        return true;
    }
}
