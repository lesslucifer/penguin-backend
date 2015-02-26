/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.expr;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Salm
 */
public class ExprExpr implements Expression {

    public static final String P_ID = "$expr";
    private final Map<String, Expression> vars;
    private final String expr;

    public ExprExpr(Map<String, Expression> vars, String expr) {
        this.vars = vars;
        this.expr = expr;
    }

    @Override
    public Object eval(ExprContext ctx) {
        Map<String, Object> variables = new HashMap(vars.size());
        vars.forEach((k, v) -> variables.put(k,  v.eval(ctx)));

        String cExpr = expr;
        for (Map.Entry<String, Object> varEntry : variables.entrySet()) {
            String varName = varEntry.getKey();
            Object varVal = varEntry.getValue();
            
            cExpr = cExpr.replaceAll(varName, varVal.toString());
        }
        
        return evalExpr(cExpr);
    }

    public static double evalExpr(final String str) {
        class Parser {

            int pos = -1, c;

            void eatChar() {
                c = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            void eatSpace() {
                while (Character.isWhitespace(c)) {
                    eatChar();
                }
            }

            double parse() {
                eatChar();
                double v = parseExpression();
                if (c != -1) {
                    throw new RuntimeException("Unexpected: " + (char) c);
                }
                return v;
            }

        // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor | term brackets
            // factor = brackets | number | factor `^` factor
            // brackets = `(` expression `)`
            double parseExpression() {
                double v = parseTerm();
                for (;;) {
                    eatSpace();
                    if (c == '+') { // addition
                        eatChar();
                        v += parseTerm();
                    } else if (c == '-') { // subtraction
                        eatChar();
                        v -= parseTerm();
                    } else {
                        return v;
                    }
                }
            }

            double parseTerm() {
                double v = parseFactor();
                for (;;) {
                    eatSpace();
                    if (c == '/') { // division
                        eatChar();
                        v /= parseFactor();
                    } else if (c == '*' || c == '(') { // multiplication
                        if (c == '*') {
                            eatChar();
                        }
                        v *= parseFactor();
                    } else {
                        return v;
                    }
                }
            }

            double parseFactor() {
                double v;
                boolean negate = false;
                eatSpace();
                if (c == '(') { // brackets
                    eatChar();
                    v = parseExpression();
                    if (c == ')') {
                        eatChar();
                    }
                } else { // numbers
                    if (c == '+' || c == '-') { // unary plus & minus
                        negate = c == '-';
                        eatChar();
                        eatSpace();
                    }
                    StringBuilder sb = new StringBuilder();
                    while ((c >= '0' && c <= '9') || c == '.') {
                        sb.append((char) c);
                        eatChar();
                    }
                    if (sb.length() == 0) {
                        throw new RuntimeException("Unexpected: " + (char) c);
                    }
                    v = Double.parseDouble(sb.toString());
                }
                eatSpace();
                if (c == '^') { // exponentiation
                    eatChar();
                    v = Math.pow(v, parseFactor());
                }
                if (c == '%') { // modulo
                    eatChar();
                    v = v % parseFactor();
                }
                if (negate) {
                    v = -v; // exponentiation has higher priority than unary minus: -3^2=-9
                }
                return v;
            }
        }
        return new Parser().parse();
    }

    static class Parser implements ExprParser {

        private Parser() {
        }
        public static final Parser PARSER = new Parser();

        @Override
        public Expression parse(Object js) {
            Map<String, Object> varsData = Collections.EMPTY_MAP;
            String exprData = null;

            if (js instanceof Map) {
                Map<?, ?> m = (Map) js;
                varsData = new HashMap(m);
                varsData.remove("$e");

                exprData = (String) m.get("$e");
            } else if (js instanceof Object) {
                exprData = (String) js;
            }

            Map<String, Expression> vars = new HashMap(varsData.size());
            varsData.forEach((k, v) -> vars.put(k, ExprParser.parseExprF(null, v)));

            return new ExprExpr(vars, exprData);
        }
    }
}
