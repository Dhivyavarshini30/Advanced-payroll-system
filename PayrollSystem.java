import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class PayrollSystem {

    private static final String URL = "jdbc:mysql://localhost:3306/data";
    private static final String USER = "root";
    private static final String PASSWORD = "12345";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("Select an option:");
            System.out.println("1. Add New Employee");
            System.out.println("2. View All Payroll Data");
            System.out.println("3. Exit");

            int choice = getIntInput(scanner);

            switch (choice) {
                case 1:
                    addNewEmployee(scanner);
                    break;
                case 2:
                    retrievePayrollData();
                    break;
                case 3:
                    running = false;
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }

        scanner.close();
    }

    private static void addNewEmployee(Scanner scanner) {
        System.out.println("Enter Employee ID:");
        int employeeId = getIntInput(scanner);
        scanner.nextLine();

        System.out.println("Enter Employee Name:");
        String employeeName = scanner.nextLine();

        System.out.println("Enter Salary:");
        double salary = getDoubleInput(scanner);

        System.out.println("Enter Deductions:");
        double deductions = getDoubleInput(scanner);

        System.out.println("Enter Benefits:");
        double benefits = getDoubleInput(scanner);

        System.out.println("Enter Tax:");
        double tax = getDoubleInput(scanner);

        System.out.println("Enter Payment Date (YYYY-MM-DD):");
        String paymentDate = scanner.next();

        System.out.println("Is Direct Deposit? (true/false):");
        boolean directDeposit = scanner.nextBoolean();
        insertPayrollData(employeeId, employeeName, salary, deductions, benefits, tax, paymentDate, directDeposit);
    }

    private static int getIntInput(Scanner scanner) {
        while (true) {
            try {
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
                scanner.next(); 
            }
        }
    }

    private static double getDoubleInput(Scanner scanner) {
        while (true) {
            try {
                return scanner.nextDouble();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid decimal number.");
                scanner.next(); 
            }
        }
    }

    private static void insertPayrollData(int employeeId, String employeeName, double salary,
                                           double deductions, double benefits, double tax,
                                           String paymentDate, boolean directDeposit) {
        String sql = "INSERT INTO payroll_data (employee_id, employee_name, salary, deductions, benefits, tax, payment_date, direct_deposit) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, employeeId);
            pstmt.setString(2, employeeName);
            pstmt.setDouble(3, salary);
            pstmt.setDouble(4, deductions);
            pstmt.setDouble(5, benefits);
            pstmt.setDouble(6, tax);
            pstmt.setString(7, paymentDate);
            pstmt.setBoolean(8, directDeposit);
            pstmt.executeUpdate();
            System.out.println("New employee added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void retrievePayrollData() {
        String sql = "SELECT * FROM payroll_data";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                System.out.println("Employee ID: " + rs.getInt("employee_id"));
                System.out.println("Employee Name: " + rs.getString("employee_name"));
                System.out.println("Salary: " + rs.getDouble("salary"));
                System.out.println("Deductions: " + rs.getDouble("deductions"));
                System.out.println("Benefits: " + rs.getDouble("benefits"));
                System.out.println("Tax: " + rs.getDouble("tax"));
                System.out.println("Payment Date: " + rs.getDate("payment_date"));
                System.out.println("Direct Deposit: " + rs.getBoolean("direct_deposit"));
                System.out.println("-------------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
