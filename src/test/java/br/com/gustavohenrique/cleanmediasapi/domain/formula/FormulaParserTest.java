package br.com.gustavohenrique.cleanmediasapi.domain.formula;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class FormulaParserTest {

    private final FormulaParser parser = new FormulaParser();

    // --- extração de identifiers ---

    @Test
    void shouldExtractIdentifiersFromSimpleFormula() {
        // (AV1 + AV2) / 2
        Map<String, Double> identifiers = parser.extractIdentifiers("(AV1+AV2)/2");

        assertThat(identifiers).containsKeys("AV1", "AV2");
        assertThat(identifiers.get("AV1")).isEqualTo(10.0); // maxValue default
        assertThat(identifiers.get("AV2")).isEqualTo(10.0);
    }

    @Test
    void shouldExtractMaxValueFromBracketAnnotation() {
        // AV1[8] → maxValue = 8.0
        Map<String, Double> identifiers = parser.extractIdentifiers("(AV1[8]+AV2[6])/2");

        assertThat(identifiers.get("AV1")).isEqualTo(8.0);
        assertThat(identifiers.get("AV2")).isEqualTo(6.0);
    }

    @Test
    void shouldExtractIdentifiersFromFunctionFormula() {
        // @M[2](AT1;AT2;AT3) → 3 identifiers com maxValue default
        Map<String, Double> identifiers = parser.extractIdentifiers("@M[2](AT1;AT2;AT3)");

        assertThat(identifiers).containsKeys("AT1", "AT2", "AT3");
    }

    @Test
    void shouldExtractIdentifiersFromComplexFormula() {
        String formula = "(0.4*@M[2](AT1;AT2;AT3))+(0.6*(AV1[10]+AV2[10])/2)";
        Map<String, Double> identifiers = parser.extractIdentifiers(formula);

        assertThat(identifiers).containsKeys("AT1", "AT2", "AT3", "AV1", "AV2");
    }

    // --- tokenização ---

    @Test
    void shouldTokenizeSimpleFormula() {
        List<FormulaToken> tokens = parser.tokenize("(AV1+AV2)/2");

        assertThat(tokens).extracting(FormulaToken::type).containsExactly(
                FormulaToken.Type.LPAREN,
                FormulaToken.Type.IDENTIFIER,
                FormulaToken.Type.OPERATOR,
                FormulaToken.Type.IDENTIFIER,
                FormulaToken.Type.RPAREN,
                FormulaToken.Type.OPERATOR,
                FormulaToken.Type.NUMBER
        );
        assertThat(tokens.get(1).value()).isEqualTo("AV1");
        assertThat(tokens.get(6).value()).isEqualTo("2");
    }

    @Test
    void shouldTokenizeFormulaWithFunction() {
        List<FormulaToken> tokens = parser.tokenize("@M[2](AT1;AT2)");

        // deve ter: FUNCTION(@M[2]) LPAREN IDENTIFIER(AT1) SEP IDENTIFIER(AT2) RPAREN
        assertThat(tokens).extracting(FormulaToken::type).containsExactly(
                FormulaToken.Type.FUNCTION,
                FormulaToken.Type.LPAREN,
                FormulaToken.Type.IDENTIFIER,
                FormulaToken.Type.SEP,
                FormulaToken.Type.IDENTIFIER,
                FormulaToken.Type.RPAREN
        );
        assertThat(tokens.get(0).value()).isEqualTo("@M[2]");
    }
}
