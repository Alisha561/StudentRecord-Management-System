Student Record Management System (SRMS)
This is a Java multithreaded application designed to simulate a Student Record Management System. It allows for the management of student records (add, update, delete, search, and view) with persistent storage and leverages core Java concepts including Object-Oriented Programming (OOP), the Collections Framework, Exception Handling, File I/O, and Multithreading.


...Features

Core CRUD Operations: Add, update, delete, search, and view all student records.


Persistent Storage: Student data is loaded from and saved to a file named students.txt using BufferedReader and BufferedWriter.




Data Management: Utilizes the Java Collections Framework (List<Student> and Map<Integer, Student>) for efficient record handling.




Sorting: Allows sorting of students by marks in descending order using a Comparator, and display using an Iterator.



Multithreading: Simulates a loading process or delay using a separate Thread for a more responsive user experience.




Robustness: Implements exception handling for invalid inputs (e.g., marks out of range, empty fields, invalid roll number) and includes a custom exception, StudentNotFoundException.



OOP Design: Built with a clear class hierarchy using inheritance (Person abstract class and Student class) and an interface (RecordActions) implemented by StudentManager.





....Technology Stack
Language: Java


File I/O: BufferedReader, BufferedWriter (for students.txt) 


Collections: List, Map 


Concurrency: Thread (for loading simulation)
