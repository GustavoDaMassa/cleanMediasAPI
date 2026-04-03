package br.com.gustavohenrique.cleanmediasapi.domain.formula;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ShuntingYardTest {

    private final FormulaParser parser = new FormulaParser();
    private final ShuntingYard shuntingYard = new ShuntingYard();

    private List<FormulaToken> rpn(String formula) {
        return shuntingYard.toRpn(parser.tokenize(formula));
    }

    @Test
    void shouldConvertSimpleAddition() {
        // AV1 + AV2 → AV1 AV2 +
        List<FormulaToken> rpn = rpn("AV1+AV2");

        assertThat(rpn).extracting(FormulaToken::value)
                .containsExactly("AV1", "AV2", "+");
    }

    @Test
    void shouldRespectOperatorPrecedence() {
        // AV1 + AV2 * 2 → AV1 AV2 2 * +
        List<FormulaToken> rpn = rpn("AV1+AV2*2");

        assertThat(rpn).extracting(FormulaToken::value)
                .containsExactly("AV1", "AV2", "2", "*", "+");
    }

    @Test
    void shouldHandleParentheses() {
        // (AV1 + AV2) / 2 → AV1 AV2 + 2 /
        List<FormulaToken> rpn = rpn("(AV1+AV2)/2");

        assertThat(rpn).extracting(FormulaToken::value)
                .containsExactly("AV1", "AV2", "+", "2", "/");
    }

    @Test
    void shouldHandleFunctionInRpn() {
        // @M[2](AT1;AT2;AT3) → AT1 AT2 AT3 @M[2]:3
        // ":3" = argCount anotado pelo ShuntingYard para o RpnEvaluator
        List<FormulaToken> rpn = rpn("@M[2](AT1;AT2;AT3)");

        assertThat(rpn).extracting(FormulaToken::value)
                .containsExactly("AT1", "AT2", "AT3", "@M[2]:3");
    }

    @Test
    void shouldHandleComplexFormula() {
        // (0.4 * @M[2](AT1;AT2;AT3)) + (0.6 * (AV1 + AV2) / 2)
        // testa que não lança exceção e produz tokens na ordem correta
        List<FormulaToken> rpn = rpn("(0.4*@M[2](AT1;AT2;AT3))+(0.6*(AV1+AV2)/2)");

        assertThat(rpn).isNotEmpty();
        // o último token deve ser o + principal
        assertThat(rpn.get(rpn.size() - 1).value()).isEqualTo("+");
    }
}
