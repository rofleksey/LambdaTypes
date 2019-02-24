public class TypeException extends Exception {
    TypeException(String varName, Type a, Type b) {
        super("Variable '"+varName+"' got two different types: "+a+" <> "+b+"!");
    }
}
