package br.com.gustavohenrique.cleanmediasapi.domain.formula;

import java.util.*;
import java.util.function.Function;
import java.util.regex.*;

// Avalia uma expressão em RPN (Reverse Polish Notation).
//
// Pilha: números e identifiers são empilhados, operadores consomem os dois
// topos, funções @M[n]:argCount consomem exatamente argCount valores.
//
// O `resolver` é uma função que recebe o nome limpo do identifier (ex: "AV1")
// e retorna o valor atual da nota — inversão de dependência: o avaliador
// não sabe onde as notas estão armazenadas.
public class RpnEvaluator {

    // Captura n de "@M[n]" e argCount de "@M[n]:argCount"
    private static final Pattern FUNCTION_PATTERN =
            Pattern.compile("@M\\[(\\d+)\\](?::(\\d+))?");

    public double evaluate(List<FormulaToken> rpn, Function<String, Double> resolver) {
        Deque<Double> stack = new ArrayDeque<>();

        for (FormulaToken token : rpn) {

            if (token.isNumber()) {
                stack.push(Double.parseDouble(token.value()));

            } else if (token.isIdentifier()) {
                // Remove [maxValue] do identifier: "AV1[10]" → "AV1"
                String name = token.value().replaceAll("\\[.*?\\]", "");
                stack.push(resolver.apply(name));

            } else if (token.isOperator()) {
                double b = stack.pop();
                double a = stack.pop();
                stack.push(applyOperator(token.value(), a, b));

            } else if (token.isFunction()) {
                Matcher m = FUNCTION_PATTERN.matcher(token.value());
                if (!m.find()) throw new IllegalArgumentException("Invalid function: " + token.value());

                int n = Integer.parseInt(m.group(1));
                int argCount = m.group(2) != null ? Integer.parseInt(m.group(2)) : n;

                // Coleta exatamente argCount valores da pilha
                List<Double> args = new ArrayList<>();
                for (int i = 0; i < argCount; i++) {
                    args.add(stack.pop());
                }
                // Ordena decrescente, soma os n maiores, divide por n
                args.sort(Collections.reverseOrder());
                double sum = args.stream().limit(n).mapToDouble(Double::doubleValue).sum();
                stack.push(sum / n);
            }
        }

        return stack.pop();
    }

    private double applyOperator(String op, double a, double b) {
        return switch (op) {
            case "+" -> a + b;
            case "-" -> a - b;
            case "*" -> a * b;
            case "/" -> a / b;
            default -> throw new IllegalArgumentException("Unknown operator: " + op);
        };
    }
}
