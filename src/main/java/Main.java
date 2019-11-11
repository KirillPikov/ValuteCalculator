public class Main {

    public static void main(String[] args) throws WrongValueException {
        String test = "toDollars(toDollars(59p + toDollars(120p - $2) + 61p - toRubles($1)) + toRubles($3))";
        System.out.println(calculate(test));
    }

    public static String calculate(String expression) throws WrongValueException {
        String result = expression;
        if(expression.startsWith("toDollars(")) {
            int indexEndInnerExp = getIndexEndInnerExp(expression);
            result = Converter.toDollars(
                    calculate(expression.substring(10, indexEndInnerExp))
            );
            if(indexEndInnerExp < expression.length() - 1) {
                if (expression.charAt(indexEndInnerExp + 2) == '+') {
                    result = Calculator.sum(result, calculate(expression.substring(indexEndInnerExp + 4)));
                }
                if (expression.charAt(indexEndInnerExp + 2) == '-') {
                    result = Calculator.sub(result, calculate(expression.substring(indexEndInnerExp + 4)));
                }
            }
        } else
        if(expression.startsWith("toRubles(")) {
            int indexEndInnerExp = getIndexEndInnerExp(expression);
            result = Converter.toRubles(
                    calculate(expression.substring(9, indexEndInnerExp))
            );
            if(indexEndInnerExp < expression.length() - 1) {
                if (expression.charAt(indexEndInnerExp + 2) == '+') {
                    result = Calculator.sum(result, calculate(expression.substring(indexEndInnerExp + 4)));
                }
                if (expression.charAt(indexEndInnerExp + 2) == '-') {
                    result = Calculator.sub(result, calculate(expression.substring(indexEndInnerExp + 4)));
                }
            }
        } else {
            int indexFirstSpace = expression.indexOf(' ');
            if(expression.charAt(indexFirstSpace + 1) == '+') {
                result = Calculator.sum(expression.substring(0, indexFirstSpace), calculate(expression.substring(indexFirstSpace + 3)));
            }
            if(expression.charAt(indexFirstSpace + 1) == '-') {
                result = Calculator.sub(expression.substring(0, indexFirstSpace), calculate(expression.substring(indexFirstSpace + 3)));
            }
        }
        return result;
    }

    public static int getIndexEndInnerExp(String expression) {
        char[] charArray = expression.substring(10).toCharArray();
        int brakesCount = 1;
        int indexEndInnerExp = 10;
        for (char ch: charArray) {
            if(ch == '(') brakesCount++;
            if(ch == ')') brakesCount--;
            if(brakesCount == 0) break;
            indexEndInnerExp++;
        }
        return indexEndInnerExp;
    }
}
