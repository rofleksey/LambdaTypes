import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.StringJoiner;

public class Main {

    public static void main(String[] args) throws Exception {
        String input;
        //input = "\\a. a' a z8'";
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))){
            StringBuilder builder = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }
            input = builder.toString();
        }
        Parser parser = new Parser(input);
        Expression expr = parser.parse();
        expr.prepareLinks();
        EquationSystem system = new EquationSystem();
        expr.getEquations(system);
        //System.out.println("Expression type: "+expr.type);
        //System.out.println("System: "+system);
        if(system.solve()) {
            HashMap<String, Type> freeVarsTypes = new HashMap<>();
            HashMap<String, Type> typeMap = new HashMap<>();
            try {
                expr.prepareTypes(system, typeMap, freeVarsTypes);
            } catch (TypeException e) {
                e.printStackTrace();
                System.out.println("Expression has no type");
                return;
            }
            StringJoiner joiner = new StringJoiner(", ");
            freeVarsTypes.forEach((key, value) -> joiner.add(key + " : " + value));
            String context = joiner.toString();
            expr.printResult(system, context, typeMap);
        } else {
            System.out.println("Expression has no type");
        }
        //System.out.println("Solved system: "+system);
    }
}
