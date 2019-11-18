import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws WrongValueException, WrongExpressionFormatException {
        String expression;

        System.out.print("Введите выражение: ");

        Scanner scanner = new Scanner(System.in);
        expression = scanner.nextLine();

        System.out.println("RESULT: " + Calculator.calculateExpression(expression));
    }
}
