import java.io.*;
import java.util.*;

// ====================================================================
// CUSTOM EXCEPTION
// ====================================================================
class StudentNotFoundException extends Exception {
    public StudentNotFoundException(String msg) {
        super(msg);
    }
}

// ====================================================================
// ABSTRACT CLASS PERSON
// ====================================================================
abstract class Person {
    String name;
    String email;

    abstract void displayInfo();
}

// ====================================================================
// STUDENT CLASS
// ====================================================================
class Student extends Person {

    int rollNo;
    String course;
    double marks;
    char grade;

    public Student(int rollNo, String name, String email, String course, double marks) {
        this.rollNo = rollNo;
        this.name = name;
        this.email = email;
        this.course = course;
        this.marks = marks;
        calculateGrade();
    }

    // Grade Calculator
    public void calculateGrade() {
        if (marks >= 90) grade = 'A';
        else if (marks >= 75) grade = 'B';
        else if (marks >= 60) grade = 'C';
        else grade = 'D';
    }

    @Override
    public void displayInfo() {
        System.out.println("\nStudent Info:");
        System.out.println("Roll No: " + rollNo);
        System.out.println("Name   : " + name);
        System.out.println("Email  : " + email);
        System.out.println("Course : " + course);
        System.out.println("Marks  : " + marks);
        System.out.println("Grade  : " + grade);
    }

    // Convert object to file string
    public String toFileString() {
        return rollNo + "," + name + "," + email + "," + course + "," + marks;
    }
}

// ====================================================================
// LOADER THREAD FOR MULTITHREADING
// ====================================================================
class Loader implements Runnable {

    @Override
    public void run() {
        System.out.print("Loading");
        try {
            for (int i = 0; i < 5; i++) {
                Thread.sleep(300);
                System.out.print(".");
            }
        } catch (Exception e) {}
        System.out.println();
    }
}

// ====================================================================
// INTERFACE
// ====================================================================
interface RecordActions {
    void addStudent();
    void deleteStudent();
    void updateStudent();
    void searchStudent();
    void viewAllStudents();
}

// ====================================================================
// STUDENT MANAGER CLASS
// ====================================================================
class StudentManager implements RecordActions {

    Scanner sc = new Scanner(System.in);
    List<Student> students = new ArrayList<>();
    Map<Integer, Student> studentMap = new HashMap<>();

    public StudentManager() {
        loadFromFile();
    }

    // ----------------------------
    // LOAD DATA FROM FILE
    // ----------------------------
    private void loadFromFile() {
        Thread t = new Thread(new Loader());
        t.start();

        try (BufferedReader br = new BufferedReader(new FileReader("students.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] arr = line.split(",");
                if (arr.length == 5) {
                    Student s = new Student(
                            Integer.parseInt(arr[0]),
                            arr[1], arr[2], arr[3],
                            Double.parseDouble(arr[4])
                    );
                    students.add(s);
                    studentMap.put(s.rollNo, s);
                }
            }
        } catch (Exception e) {
            System.out.println("No existing file found. Starting fresh.");
        }
    }

    // ----------------------------
    // SAVE TO FILE
    // ----------------------------
    public void saveToFile() {
        Thread t = new Thread(new Loader());
        t.start();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("students.txt"))) {
            for (Student s : students) {
                bw.write(s.toFileString());
                bw.newLine();
            }
            System.out.println("Saved and exiting.");
        } catch (Exception e) {
            System.out.println("Error saving file.");
        }
    }

    // ----------------------------
    // ADD STUDENT
    // ----------------------------
    @Override
    public void addStudent() {
        try {
            System.out.print("Enter Roll No: ");
            int roll = sc.nextInt();
            sc.nextLine();

            if (studentMap.containsKey(roll)) {
                throw new Exception("Roll number already exists!");
            }

            System.out.print("Enter Name: ");
            String name = sc.nextLine();

            System.out.print("Enter Email: ");
            String email = sc.nextLine();

            System.out.print("Enter Course: ");
            String course = sc.nextLine();

            System.out.print("Enter Marks: ");
            double marks = sc.nextDouble();

            if (marks < 0 || marks > 100) {
                throw new Exception("Invalid marks! Must be between 0 and 100.");
            }

            Student s = new Student(roll, name, email, course, marks);
            students.add(s);
            studentMap.put(roll, s);

            System.out.println("Student added successfully.");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            sc.nextLine();
        }
    }

    // ----------------------------
    // DELETE STUDENT
    // ----------------------------
    @Override
    public void deleteStudent() {
        sc.nextLine();
        System.out.print("Enter name to delete: ");
        String name = sc.nextLine();

        Iterator<Student> it = students.iterator();
        boolean deleted = false;

        while (it.hasNext()) {
            Student s = it.next();
            if (s.name.equalsIgnoreCase(name)) {
                it.remove();
                studentMap.remove(s.rollNo);
                deleted = true;
                System.out.println("Student record deleted.");
            }
        }

        if (!deleted) {
            System.out.println("Student not found.");
        }
    }

    // ----------------------------
    // UPDATE STUDENT
    // ----------------------------
    @Override
    public void updateStudent() {
        System.out.print("Enter Roll No to update: ");
        int roll = sc.nextInt();
        sc.nextLine();

        if (!studentMap.containsKey(roll)) {
            System.out.println("Student not found.");
            return;
        }

        Student s = studentMap.get(roll);

        System.out.print("Enter new course: ");
        s.course = sc.nextLine();

        System.out.print("Enter new marks: ");
        s.marks = sc.nextDouble();
        s.calculateGrade();

        System.out.println("Record updated successfully.");
    }

    // ----------------------------
    // SEARCH STUDENT
    // ----------------------------
    @Override
    public void searchStudent() {
        sc.nextLine();
        System.out.print("Enter name to search: ");
        String name = sc.nextLine();

        boolean found = false;
        for (Student s : students) {
            if (s.name.equalsIgnoreCase(name)) {
                s.displayInfo();
                found = true;
            }
        }

        if (!found) {
            System.out.println("Student not found.");
        }
    }

    // ----------------------------
    // VIEW ALL STUDENTS (Iterator)
    // ----------------------------
    @Override
    public void viewAllStudents() {
        if (students.isEmpty()) {
            System.out.println("No records found.");
            return;
        }

        Iterator<Student> it = students.iterator();
        while (it.hasNext()) {
            it.next().displayInfo();
        }
    }

    // ----------------------------
    // SORT BY MARKS
    // ----------------------------
    public void sortByMarks() {
        students.sort((a, b) -> Double.compare(b.marks, a.marks));

        System.out.println("Sorted Student List by Marks:");
        for (Student s : students) {
            s.displayInfo();
        }
    }
}

// ====================================================================
// MAIN CLASS
// ====================================================================
public class StudentRecordManagementSystem {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        StudentManager m = new StudentManager();

        int choice = 0;

        while (choice != 6) {
            System.out.println("\n===== Capstone Student Menu =====");
            System.out.println("1. Add Student");
            System.out.println("2. View All Students");
            System.out.println("3. Search by Name");
            System.out.println("4. Delete by Name");
            System.out.println("5. Sort by Marks");
            System.out.println("6. Save and Exit");
            System.out.print("Enter choice: ");

            try {
                choice = sc.nextInt();

                switch (choice) {
                    case 1 -> m.addStudent();
                    case 2 -> m.viewAllStudents();
                    case 3 -> m.searchStudent();
                    case 4 -> m.deleteStudent();
                    case 5 -> m.sortByMarks();
                    case 6 -> m.saveToFile();
                    default -> System.out.println("Invalid choice!");
                }

            } catch (Exception e) {
                System.out.println("Invalid input!");
                sc.nextLine();
            }
        }

        sc.close();
    }
}
