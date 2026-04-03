package br.com.gustavohenrique.cleanmediasapi.domain.formula;

// Representa um token da fórmula após tokenização.
// Sealed para garantir que apenas os tipos conhecidos existam.
public record FormulaToken(Type type, String value) {

    public enum Type {
        NUMBER,      // ex: 0.4, 2, 6
        IDENTIFIER,  // ex: AV1, AT1, AV1[10]
        OPERATOR,    // + - * /
        FUNCTION,    // @M[n]
        LPAREN,      // (
        RPAREN,      // )
        SEP          // ; (separador de argumentos de função)
    }

    public boolean isNumber() { return type == Type.NUMBER; }
    public boolean isIdentifier() { return type == Type.IDENTIFIER; }
    public boolean isOperator() { return type == Type.OPERATOR; }
    public boolean isFunction() { return type == Type.FUNCTION; }
    public boolean isLParen() { return type == Type.LPAREN; }
    public boolean isRParen() { return type == Type.RPAREN; }
    public boolean isSep() { return type == Type.SEP; }
}
