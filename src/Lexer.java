import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
    private String input;
    private Token EOF_TOKEN;
    private int pos = -1;
    private boolean eof;
    private ArrayList<TokenMatcher> matchers;

    Lexer(String input) {
        EOF_TOKEN = new Token(TokenType.EOF, "");
        this.input = input;
        matchers = new ArrayList<>(10);
        matchers.add(new TokenMatcher(TokenType.SKIP, "\\s+", true));
        matchers.add(new TokenMatcher(TokenType.VAR, "[a-zA-Z]([a-zA-Z0-9'])*", true));
        matchers.add(new TokenMatcher(TokenType.LB, "(", false));
        matchers.add(new TokenMatcher(TokenType.RB, ")", false));
        matchers.add(new TokenMatcher(TokenType.LAMBDA, "\\", false));
        matchers.add(new TokenMatcher(TokenType.DOT, ".", false));
        matchers.forEach(TokenMatcher::prepare);
        nextChar();
    }

    void nextChar() {
        if(eof) {
            return;
        }
        if(pos + 1 < input.length()) {
            pos++;
        } else {
            eof = true;
        }
    }

    Token nextToken() throws ParseException {
        if (!eof) {
            matchers.forEach(m -> m.from(pos));
            for (TokenMatcher m : matchers) {
                if (m.match()) {
                    pos += m.getLen() - 1;
                    nextChar();
                    return new Token(m.type, m.getMatch());
                }
            }
            throw new ParseException("Invalid input at "+pos);
        }
        return EOF_TOKEN;
    }

    List<Token> getTokens() throws ParseException {
        Token token;
        ArrayList<Token> tokens = new ArrayList<>(100);
        do {
            token = nextToken();
            if(token.type != TokenType.SKIP) {
                tokens.add(token);
            }
        } while (token.type != TokenType.EOF);
        return tokens;
    }

    private class TokenMatcher {
        final TokenType type;
        private final Pattern pattern;
        private Matcher matcher;
        String last;

        TokenMatcher(TokenType type, String pattern, boolean special) {
            this.type = type;
            this.pattern = Pattern.compile(special ? pattern : Pattern.quote(pattern));
        }

        void prepare() {
            matcher = pattern.matcher(input);
        }

        void from(int ind) {
            matcher.region(ind, input.length());
        }

        boolean match() {
            if (matcher.lookingAt()) {
                last = matcher.group();
                return true;
            }
            return false;
        }

        int getLen() {
            return last.length();
        }

        String getMatch() {
            return last;
        }
    }
}
