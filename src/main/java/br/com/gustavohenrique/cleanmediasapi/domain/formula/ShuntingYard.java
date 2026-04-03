package br.com.gustavohenrique.cleanmediasapi.domain.formula;

import java.util.*;

// Algoritmo Shunting Yard de Dijkstra.
// Converte tokens em notação infixa para RPN (Reverse Polish Notation).
//
// Extensão para funções: ao encontrar @M[n](...), conta os SEPs (;)
// para determinar o argCount e anota no token emitido como "@M[n]:argCount".
// Isso permite ao RpnEvaluator saber exatamente quantos valores consumir da pilha.
public class ShuntingYard {

    public List<FormulaToken> toRpn(List<FormulaToken> tokens) {
        List<FormulaToken> output = new ArrayList<>();
        Deque<FormulaToken> opStack = new ArrayDeque<>();

        // Pilha de contadores de args: empilhamos 1 ao entrar em função,
        // incrementamos a cada SEP. Ao emitir a função, desempilhamos.
        Deque<Integer> argCountStack = new ArrayDeque<>();

        for (FormulaToken token : tokens) {

            if (token.isNumber() || token.isIdentifier()) {
                output.add(token);

            } else if (token.isFunction()) {
                opStack.push(token);
                argCountStack.push(1); // começa com 1 arg

            } else if (token.isSep()) {
                // Esvazia operadores até o '(' correspondente à função
                while (!opStack.isEmpty() && !opStack.peek().isLParen()) {
                    output.add(opStack.pop());
                }
                // Incrementa contador de args da função atual
                if (!argCountStack.isEmpty()) {
                    int count = argCountStack.pop();
                    argCountStack.push(count + 1);
                }

            } else if (token.isOperator()) {
                while (!opStack.isEmpty()
                        && opStack.peek().isOperator()
                        && precedence(opStack.peek()) >= precedence(token)) {
                    output.add(opStack.pop());
                }
                opStack.push(token);

            } else if (token.isLParen()) {
                opStack.push(token);

            } else if (token.isRParen()) {
                while (!opStack.isEmpty() && !opStack.peek().isLParen()) {
                    output.add(opStack.pop());
                }
                if (!opStack.isEmpty()) opStack.pop(); // descarta '('

                // Se o topo agora é uma função, emite anotada com argCount
                if (!opStack.isEmpty() && opStack.peek().isFunction()) {
                    FormulaToken fn = opStack.pop();
                    int argCount = argCountStack.isEmpty() ? 1 : argCountStack.pop();
                    // Anota: "@M[2]:3" significa @M[2] com 3 argumentos
                    output.add(new FormulaToken(FormulaToken.Type.FUNCTION,
                            fn.value() + ":" + argCount));
                }
            }
        }

        while (!opStack.isEmpty()) {
            output.add(opStack.pop());
        }

        return output;
    }

    private int precedence(FormulaToken token) {
        return switch (token.value()) {
            case "+", "-" -> 1;
            case "*", "/" -> 2;
            default -> 0;
        };
    }
}
