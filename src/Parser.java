import java.util.List;

public class Parser {
    private final List<Token> tokens;
    private int curPos = 0;

    Parser(String input) throws ParseException {
        Lexer lexer = new Lexer(input);
        tokens = lexer.getTokens();
    }

    private Token get() {
        return tokens.get(curPos);
    }

    Expression parse() throws ParseException {
        return expr();
    }

    private boolean skip(TokenType type) {
        if(get().type == type) {
            curPos++;
            return true;
        }
        return false;
    }

    private String skipVar() throws ParseException {
        if(get().type == TokenType.VAR) {
            String text = get().text;
            curPos++;
            return text;
        }
        throw new ParseException("Var expected at token "+curPos);
    }

    private Expression expr() throws ParseException {
        if(skip(TokenType.LAMBDA)) {
            String name = skipVar();
            skip(TokenType.DOT);
            Expression e2 = expr();
            return new Lambda(name, e2);
        } else {
            Expression a = apply();
            if(skip(TokenType.LAMBDA)) {
                String name = skipVar();
                skip(TokenType.DOT);
                Expression e2 = expr();
                return new Apply(a, new Lambda(name, e2));
            }
            return a;
        }
    }

    private Expression apply() throws ParseException {
        Expression a = atom();
        while(get().type == TokenType.LB || get().type == TokenType.VAR) {
            Expression b = atom();
            a = new Apply(a, b);
        }
        return a;
    }

    private Expression atom() throws ParseException {
        if(skip(TokenType.LB)) {
            Expression a = expr();
            skip(TokenType.RB);
            return a;
        }
        return new Var(skipVar());
    }


}
