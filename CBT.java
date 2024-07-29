import java.util.Scanner;

public class CBT {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        startCBT(scanner);
    }

    public static void startCBT(Scanner scanner) {
        System.out.println("Welcome to the Computer-Based Test (CBT) Application!");
        System.out.println("1 - Start the CBT");
        System.out.println("2 - Quit the Application");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                createCBTTest(scanner);
                break;
            case 2:
                System.out.println("Exiting the Application. Goodbye!");
                break;
            default:
                System.out.println("Invalid choice. Exiting the Application.");
        }
    }

    public static void createCBTTest(Scanner scanner) {
        String[] questions = {
                "A for ?",
                "B for ?",
                "C for ?"
        };

        String[] answers = {
                "Apple",
                "Ball",
                "Cat"
        };

        System.out.println("Welcome to the Computer-Based Test (CBT)!");
        System.out.print("Enter the number of students: ");
        int numStudents = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        for (int studentIndex = 1; studentIndex <= numStudents; studentIndex++) {
            System.out.println("Enter details for Student " + studentIndex + ":");
            System.out.print("Enter your Username: ");
            String username = scanner.nextLine();
            System.out.print("Enter your Matric Number: ");
            String matricNumber = scanner.nextLine();
            int totalScore = 0;
            for (int questionIndex = 0; questionIndex < questions.length; questionIndex++) {
                System.out.println(
                        "Student " + username + ", Question " + (questionIndex + 1) + ": " + questions[questionIndex]);
                System.out.print("Your answer: ");
                String answer = scanner.nextLine();
                if (answer.equalsIgnoreCase(answers[questionIndex])) {
                    totalScore++;
                }
            }
            System.out.println("Congrats, " + username + " with Matric Number " + matricNumber + " scored: "
                    + totalScore + " out of " + questions.length);
        }
    }
}
