package br.com.gustavohenrique.cleanmediasapi.domain.formula;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

class RpnEvaluatorTest {

    private final FormulaParser parser = new FormulaParser();
    private final ShuntingYard shuntingYard = new ShuntingYard();
    private final RpnEvaluator evaluator = new RpnEvaluator();

    private double evaluate(String formula, Map<String, Double> grades) {
        var tokens = parser.tokenize(formula);
        var rpn = shuntingYard.toRpn(tokens);
        return evaluator.evaluate(rpn, grades::get);
    }

    @Test
    void shouldEvaluateSimpleAverage() {
        // (AV1 + AV2) / 2 com AV1=8, AV2=6 → 7.0
        double result = evaluate("(AV1+AV2)/2", Map.of("AV1", 8.0, "AV2", 6.0));

        assertThat(result).isCloseTo(7.0, within(0.001));
    }

    @Test
    void shouldEvaluateWeightedAverage() {
        // 0.4*AV1 + 0.6*AV2 com AV1=10, AV2=5 → 7.0
        double result = evaluate("0.4*AV1+0.6*AV2", Map.of("AV1", 10.0, "AV2", 5.0));

        assertThat(result).isCloseTo(7.0, within(0.001));
    }

    @Test
    void shouldEvaluateFunctionTopN() {
        // @M[2](AT1;AT2;AT3) com notas 6, 8, 4 → (8+6)/2 = 7.0
        double result = evaluate("@M[2](AT1;AT2;AT3)",
                Map.of("AT1", 6.0, "AT2", 8.0, "AT3", 4.0));

        assertThat(result).isCloseTo(7.0, within(0.001));
    }

    @Test
    void shouldEvaluateComplexFormula() {
        // (0.4*@M[2](AT1;AT2;AT3)) + (0.6*(AV1+AV2)/2)
        // ATs: 6,8,4 → top2 = (8+6)/2 = 7; AVs: 9,7 → (9+7)/2 = 8
        // resultado: 0.4*7 + 0.6*8 = 2.8 + 4.8 = 7.6
        double result = evaluate(
                "(0.4*@M[2](AT1;AT2;AT3))+(0.6*(AV1+AV2)/2)",
                Map.of("AT1", 6.0, "AT2", 8.0, "AT3", 4.0, "AV1", 9.0, "AV2", 7.0)
        );

        assertThat(result).isCloseTo(7.6, within(0.001));
    }

    @Test
    void shouldEvaluateFormulaWithAllZeros() {
        double result = evaluate("(AV1+AV2)/2", Map.of("AV1", 0.0, "AV2", 0.0));

        assertThat(result).isCloseTo(0.0, within(0.001));
    }
}
