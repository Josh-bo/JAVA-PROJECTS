import java.util.Scanner;
import java.util.Random;

public class LoveCalculator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        System.out.print("Enter the first name: ");
        String name1 = scanner.nextLine();

        System.out.print("Enter the second name: ");
        String name2 = scanner.nextLine();

        if (name1.isEmpty() || name2.isEmpty()) {
            System.out.println("Both names are required.");
            return;
        }

        int score = random.nextInt(101);
        String message;

        if (score < 30) {
            message = "Not compatible";
        } else if (score < 70) {
            message = "Still manageable";
        } else {
            message = "Compatible";
        }

        System.out.println("Compatibility score: " + score);
        System.out.println("Message: " + message);
    }
}
