package br.com.gustavohenrique.cleanmediasapi.domain.formula;

import java.util.*;
import java.util.regex.*;

// Responsabilidades:
// 1. extractIdentifiers() — extrai nome + maxValue de cada assessment na fórmula
// 2. tokenize()          — transforma a string em lista de FormulaToken
//
// Sintaxe suportada:
// - Identificador simples:    AV1        (maxValue default 10.0)
// - Identificador com limite: AV1[8]     (maxValue = 8.0)
// - Função top-n:             @M[2](AT1;AT2;AT3)
// - Operadores:               + - * /
// - Parênteses:               ( )
public class FormulaParser {

    // Captura: identificadores como AV1, AT1, AV1[8] — mas NÃO @M[n]
    // Usa lookbehind para excluir o @ que precede @M
    private static final Pattern IDENTIFIER_PATTERN =
            Pattern.compile("(?<!@)\\b([A-Za-z][A-Za-z0-9]*)(?:\\[(\\d+(?:[.,]\\d+)?)\\])?");

    // Tokenizer: captura cada elemento da fórmula na ordem em que aparece
    private static final Pattern TOKEN_PATTERN = Pattern.compile(
            "(@M\\[\\d+\\])"            // FUNCTION: @M[n]
            + "|(\\d+(?:[.,]\\d+)?)"    // NUMBER: 0.4, 2, 6
            + "|([A-Za-z][A-Za-z0-9]*(?:\\[\\d+(?:[.,]\\d+)?\\])?)" // IDENTIFIER: AV1, AV1[8]
            + "|([+\\-*/])"             // OPERATOR
            + "|(\\()"                  // LPAREN
            + "|(\\))"                  // RPAREN
            + "|(;)"                    // SEP
    );

    // Retorna mapa de identifier → maxValue
    public Map<String, Double> extractIdentifiers(String formula) {
        Map<String, Double> result = new LinkedHashMap<>();
        Matcher m = IDENTIFIER_PATTERN.matcher(formula);
        while (m.find()) {
            String name = m.group(1);
            String maxStr = m.group(2);
            double maxValue = maxStr != null
                    ? Double.parseDouble(maxStr.replace(',', '.'))
                    : 10.0;
            result.put(name, maxValue);
        }
        return result;
    }

    // Converte a string da fórmula em lista de tokens
    public List<FormulaToken> tokenize(String formula) {
        List<FormulaToken> tokens = new ArrayList<>();
        Matcher m = TOKEN_PATTERN.matcher(formula.replaceAll("\\s+", ""));
        while (m.find()) {
            if (m.group(1) != null) {
                tokens.add(new FormulaToken(FormulaToken.Type.FUNCTION, m.group(1)));
            } else if (m.group(2) != null) {
                tokens.add(new FormulaToken(FormulaToken.Type.NUMBER, m.group(2).replace(',', '.')));
            } else if (m.group(3) != null) {
                tokens.add(new FormulaToken(FormulaToken.Type.IDENTIFIER, m.group(3)));
            } else if (m.group(4) != null) {
                tokens.add(new FormulaToken(FormulaToken.Type.OPERATOR, m.group(4)));
            } else if (m.group(5) != null) {
                tokens.add(new FormulaToken(FormulaToken.Type.LPAREN, "("));
            } else if (m.group(6) != null) {
                tokens.add(new FormulaToken(FormulaToken.Type.RPAREN, ")"));
            } else if (m.group(7) != null) {
                tokens.add(new FormulaToken(FormulaToken.Type.SEP, ";"));
            }
        }
        return tokens;
    }
}
