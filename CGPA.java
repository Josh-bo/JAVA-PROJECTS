import java.util.Scanner;

public class CGPA {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        firstStart(scanner);
    }

    public static void firstStart(Scanner scanner) {
        System.out.println("Welcome to the CGPA Calculator Application!");
        System.out.println("1 - Start the CGPA Calculator");
        System.out.println("2 - Quit the Application");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                calculateCGPA(scanner);
                break;
            case 2:
                System.out.println("Exiting the Application. Goodbye!");
                break;
            default:
                System.out.println("Invalid choice. Exiting the Application.");
        }
    }

    // This one is the Method to calculate CGPA
    public static void calculateCGPA(Scanner scanner) {
        System.out.println("WELCOME TO MY CGPA CALCULATOR APPLICATION USING JAVA");
        scanner.nextLine();// This one is to Consume the newline character left by nextInt()

        System.out.println("KINDLY ENTER YOUR NAME :=>");
        String name = scanner.nextLine();
        System.out.println("KINDLY ENTER YOUR DEPARTMENT :=>");
        String className = scanner.nextLine();

        // This one is to Define the number of courses
        System.out.println("Enter the number of courses:");
        int numCourses = scanner.nextInt();
        double[] courseScores = new double[numCourses];
        for (int i = 0; i < numCourses; i++) {
            System.out.println("Enter score for Course " + (i + 1) + ":");
            courseScores[i] = scanner.nextDouble();
        }

        // This one is to Calculate CGPA
        double cgpa = calculateCGPA(courseScores);

        // This one is to Display CGPA
        System.out.println("Hello " + name + "!");
        System.out.println("Class: " + className);
        System.out.println("Your CGPA is: " + cgpa);
        firstStart(scanner);
    }

    // This one is the Helper methods for CGPA calculation
    public static double calculateCGPA(double[] scores) {
        double totalGradePoints = 0;
        for (double score : scores) {
            totalGradePoints += convertToGradePoint(score);
        }
        return totalGradePoints / scores.length;
    }

    public static double convertToGradePoint(double score) {
        if (score >= 70) {
            return 5.0;
        } else if (score >= 60) {
            return 4.0;
        } else if (score >= 50) {
            return 3.0;
        } else if (score >= 45) {
            return 2.0;
        } else if (score >= 40) {
            return 1.0;
        } else {
            return 0.0;
        }
    }
}
