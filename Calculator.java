import java.util.Scanner;

public class Calculator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Simple Terminal Calculator");
        System.out.println("Enter first number:");
        double num1 = getValidNumber(scanner);

        System.out.println("Enter an operator (+, -, *, /):");
        char operator = getValidOperator(scanner);

        System.out.println("Enter second number:");
        double num2 = getValidNumber(scanner);

        double result = performCalculation(num1, num2, operator);

        System.out.printf("Result: %.2f%n", result);
    }

    private static double getValidNumber(Scanner scanner) {
        while (!scanner.hasNextDouble()) {
            System.out.println("Invalid input. Please enter a valid number:");
            scanner.next(); // Clear invalid input
        }
        return scanner.nextDouble();
    }

    private static char getValidOperator(Scanner scanner) {
        char operator;
        while (true) {
            String input = scanner.next();
            if (input.length() == 1 && "+-*/".contains(input)) {
                operator = input.charAt(0);
                break;
            }
            System.out.println("Invalid operator. Please enter one of the following operators (+, -, *, /):");
        }
        return operator;
    }

    private static double performCalculation(double num1, double num2, char operator) {
        switch (operator) {
            case '+':
                return num1 + num2;
            case '-':
                return num1 - num2;
            case '*':
                return num1 * num2;
            case '/':
                if (num2 != 0) {
                    return num1 / num2;
                } else {
                    System.out.println("Error: Division by zero is not allowed.");
                    return Double.NaN;
                }
            default:
                System.out.println("Invalid operator.");
                return Double.NaN;
        }
    }
}
