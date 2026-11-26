import java.util.Scanner;
import java.io.*;

class Student {
    String id;
    String name;
    int marks;

    Student(String id, String name, int marks) {
        this.id = id;
        this.name = name;
        this.marks = marks;
    }

    void display() {
        System.out.println(id + " | " + name + " | " + marks);
    }
}

public class StudentMarksSearchSystem {

    static Student[] students = new Student[1000];
    static int count = 0;
    static int nextId = 101;
    static boolean isSorted = false;

    static Scanner sc = new Scanner(System.in);
    static final String FILE_NAME = "marks.txt";

    // Load students from file
    static void loadData() {
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return;
        }

        try (BufferedReader br = new BufferedReader(
                new FileReader(file))) {

            String line;

            while ((line = br.readLine()) != null
                    && count < students.length) {

                String[] data = line.split("\\|", 3);

                if (data.length == 3) {
                    try {
                        String id = data[0];
                        String name = data[1];
                        int marks = Integer.parseInt(data[2]);

                        if (marks >= 0 && marks <= 100) {
                            students[count++] =
                                new Student(id, name, marks);

                            int number = Integer.parseInt(
                                id.substring(1));

                            if (number >= nextId) {
                                nextId = number + 1;
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("Skipping invalid record.");
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("Error loading file.");
        }
    }

    // Save students to file
    static void saveData() {
        try (PrintWriter pw = new PrintWriter(
                new FileWriter(FILE_NAME))) {

            for (int i = 0; i < count; i++) {
                pw.println(students[i].id + "|"
                        + students[i].name + "|"
                        + students[i].marks);
            }

            System.out.println("Data saved successfully.");

        } catch (IOException e) {
            System.out.println("Error saving file.");
        }
    }

    // Add student
    static void addStudent() {

        if (count >= students.length) {
            System.out.println("Student limit reached.");
            return;
        }

        System.out.print("Enter Name: ");
        String name = sc.nextLine().trim();

        if (name.isEmpty() || name.contains("|")) {
            System.out.println("Invalid student name.");
            return;
        }

        System.out.print("Enter Marks: ");
        int marks;

        try {
            marks = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid marks. Enter a number.");
            return;
        }

        if (marks < 0 || marks > 100) {
            System.out.println(
                "Invalid marks. Marks must be between 0 and 100.");
            return;
        }

        String id = "S" + nextId++;

        students[count++] = new Student(id, name, marks);

        isSorted = false;

        System.out.println("Student added. ID: "
                + id + " | " + name + " | " + marks);
    }

    // Display all students
    static void displayAll() {

        if (count == 0) {
            System.out.println("No student records found.");
            return;
        }

        System.out.println("\nID | Name | Marks");

        for (int i = 0; i < count; i++) {
            System.out.print((i + 1) + ". ");
            students[i].display();
        }
    }

    // Sort students by marks using Bubble Sort
    static void sortByMarks() {

        for (int i = 0; i < count - 1; i++) {

            for (int j = 0; j < count - i - 1; j++) {

                if (students[j].marks > students[j + 1].marks) {

                    Student temp = students[j];
                    students[j] = students[j + 1];
                    students[j + 1] = temp;
                }
            }
        }

        isSorted = true;

        System.out.println("\nStudents sorted by marks:");

        for (int i = 0; i < count; i++) {
            System.out.print((i + 1) + ". ");
            students[i].display();
        }
    }

    // Binary Search by marks
    static void binarySearch(int target) {

        if (!isSorted) {
            System.out.println(
                "Please sort students by marks first (Option 3).");
            return;
        }

        int low = 0;
        int high = count - 1;

        while (low <= high) {

            int mid = low + (high - low) / 2;

            if (students[mid].marks == target) {

                System.out.println("Found at index " + mid
                        + ": " + students[mid].id
                        + " | " + students[mid].name
                        + " | " + students[mid].marks);

                return;
            }

            else if (students[mid].marks < target) {
                low = mid + 1;
            }

            else {
                high = mid - 1;
            }
        }

        System.out.println("Student marks not found.");
    }

    // Main method
    public static void main(String[] args) {

        loadData();

        System.out.println("================================");
        System.out.println("   STUDENT MARKS SEARCH SYSTEM");
        System.out.println("================================");

        while (true) {

            System.out.println("\n1. Add Student");
            System.out.println("2. Display All");
            System.out.println("3. Sort by Marks (Low to High)");
            System.out.println("4. Binary Search by Marks");
            System.out.println("5. Exit");

            System.out.print("Enter your choice: ");

            int choice;

            try {
                choice = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid menu choice.");
                continue;
            }

            switch (choice) {

                case 1:
                    addStudent();
                    break;

                case 2:
                    displayAll();
                    break;

                case 3:
                    sortByMarks();
                    break;

                case 4:
                    System.out.print("Enter marks to search: ");

                    try {
                        int target = Integer.parseInt(
                            sc.nextLine().trim());

                        if (target < 0 || target > 100) {
                            System.out.println("Invalid marks.");
                        } else {
                            binarySearch(target);
                        }

                    } catch (NumberFormatException e) {
                        System.out.println("Enter valid marks.");
                    }
                    break;

                case 5:
                    saveData();
                    System.out.println("Thank you! Exiting...");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}
