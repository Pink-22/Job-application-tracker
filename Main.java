// Main.java
// Entry point - Console menu for Job Application Tracker

import java.sql.Date;
import java.util.List;
import java.util.Scanner;

public class Main {

    static ApplicationDAO dao     = new ApplicationDAO();
    static Scanner        scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║      JOB APPLICATION TRACKER         ║");
        System.out.println("╚══════════════════════════════════════╝");

        // Show reminders on startup
        showReminders();

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Enter choice: ");

            switch (choice) {
                case 1 -> addApplication();
                case 2 -> viewAllApplications();
                case 3 -> updateStatus();
                case 4 -> deleteApplication();
                case 5 -> filterByStatus();
                case 6 -> dao.printStats();
                case 7 -> exportToExcel();
                case 8 -> showReminders();
                case 0 -> { System.out.println("Goodbye!"); running = false; }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
        scanner.close();
    }

    // ── MENU ─────────────────────────────────────────────────────────
    static void printMenu() {
        System.out.println("\n========== MENU ==========");
        System.out.println("1. Add New Application");
        System.out.println("2. View All Applications");
        System.out.println("3. Update Application Status");
        System.out.println("4. Delete Application");
        System.out.println("5. Filter by Status");
        System.out.println("6. View Statistics");
        System.out.println("7. Export to Excel");
        System.out.println("8. Show Follow-Up Reminders");
        System.out.println("0. Exit");
        System.out.println("==========================");
    }

    // ── ADD APPLICATION ───────────────────────────────────────────────
    static void addApplication() {
        System.out.println("\n--- Add New Job Application ---");
        System.out.print("Company Name  : "); String company    = scanner.nextLine().trim();
        System.out.print("Role/Position : "); String role       = scanner.nextLine().trim();
        System.out.print("Date Applied (YYYY-MM-DD): "); String dateStr = scanner.nextLine().trim();
        System.out.print("Status (Applied/Interview/Rejected/Offered): "); String status = scanner.nextLine().trim();
        System.out.print("Notes         : "); String notes      = scanner.nextLine().trim();
        System.out.print("Follow-Up Date (YYYY-MM-DD or leave blank): "); String followUp = scanner.nextLine().trim();

        Date dateApplied  = Date.valueOf(dateStr);
        Date followUpDate = followUp.isEmpty() ? null : Date.valueOf(followUp);

        Application app = new Application(company, role, dateApplied, status, notes, followUpDate);
        if (dao.addApplication(app)) {
            System.out.println("✅ Application added successfully!");
        }
    }

    // ── VIEW ALL ──────────────────────────────────────────────────────
    static void viewAllApplications() {
        List<Application> list = dao.getAllApplications();
        if (list.isEmpty()) {
            System.out.println("No applications found.");
            return;
        }
        System.out.println("\n--- All Applications ---");
        System.out.printf("%-5s %-20s %-20s %-12s %-10s%n", "ID", "Company", "Role", "Status", "Date");
        System.out.println("-".repeat(70));
        for (Application app : list) {
            System.out.println(app);
        }
    }

    // ── UPDATE STATUS ─────────────────────────────────────────────────
    static void updateStatus() {
        viewAllApplications();
        int id = readInt("\nEnter Application ID to update: ");
        System.out.print("New Status (Applied/Interview/Rejected/Offered): ");
        String newStatus = scanner.nextLine().trim();
        if (dao.updateStatus(id, newStatus)) {
            System.out.println("✅ Status updated successfully!");
        }
    }

    // ── DELETE ────────────────────────────────────────────────────────
    static void deleteApplication() {
        viewAllApplications();
        int id = readInt("\nEnter Application ID to delete: ");
        System.out.print("Are you sure? (yes/no): ");
        String confirm = scanner.nextLine().trim();
        if (confirm.equalsIgnoreCase("yes")) {
            if (dao.deleteApplication(id)) {
                System.out.println("✅ Application deleted.");
            }
        } else {
            System.out.println("Cancelled.");
        }
    }

    // ── FILTER BY STATUS ──────────────────────────────────────────────
    static void filterByStatus() {
        System.out.print("Filter by status (Applied/Interview/Rejected/Offered): ");
        String status = scanner.nextLine().trim();
        List<Application> list = dao.getByStatus(status);
        if (list.isEmpty()) {
            System.out.println("No applications with status: " + status);
            return;
        }
        System.out.println("\n--- Applications with status: " + status + " ---");
        System.out.printf("%-5s %-20s %-20s %-12s %-10s%n", "ID", "Company", "Role", "Status", "Date");
        System.out.println("-".repeat(70));
        for (Application app : list) System.out.println(app);
    }

    // ── EXPORT TO EXCEL ───────────────────────────────────────────────
    static void exportToExcel() {
        List<Application> list = dao.getAllApplications();
        if (list.isEmpty()) {
            System.out.println("No data to export.");
            return;
        }
        String filePath = "JobApplications_Export.xlsx";
        ExcelExporter.exportToExcel(list, filePath);
    }

    // ── REMINDERS ─────────────────────────────────────────────────────
    static void showReminders() {
        List<Application> reminders = dao.getFollowUpReminders();
        if (!reminders.isEmpty()) {
            System.out.println("\n⚠️  FOLLOW-UP REMINDERS (" + reminders.size() + " pending):");
            System.out.println("-".repeat(60));
            for (Application app : reminders) {
                System.out.println("🔔 " + app.getCompanyName() + " | " + app.getRole() +
                        " | Follow-up: " + app.getFollowUpDate());
            }
            System.out.println("-".repeat(60));
        }
    }

    // ── HELPER ───────────────────────────────────────────────────────
    static int readInt(String prompt) {
        System.out.print(prompt);
        try {
            int val = Integer.parseInt(scanner.nextLine().trim());
            return val;
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
