// ApplicationDAO.java
// Data Access Object - handles all database operations (Add, View, Update, Delete)

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ApplicationDAO {

    // ── ADD new application ──────────────────────────────────────────
    public boolean addApplication(Application app) {
        String sql = "INSERT INTO applications (company_name, role, date_applied, status, notes, follow_up_date) VALUES (?,?,?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, app.getCompanyName());
            ps.setString(2, app.getRole());
            ps.setDate  (3, app.getDateApplied());
            ps.setString(4, app.getStatus());
            ps.setString(5, app.getNotes());
            ps.setDate  (6, app.getFollowUpDate());
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error adding application: " + e.getMessage());
            return false;
        }
    }

    // ── VIEW all applications ────────────────────────────────────────
    public List<Application> getAllApplications() {
        List<Application> list = new ArrayList<>();
        String sql = "SELECT * FROM applications ORDER BY date_applied DESC";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Application app = new Application(
                    rs.getString("company_name"),
                    rs.getString("role"),
                    rs.getDate("date_applied"),
                    rs.getString("status"),
                    rs.getString("notes"),
                    rs.getDate("follow_up_date")
                );
                app.setId(rs.getInt("id"));
                list.add(app);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching applications: " + e.getMessage());
        }
        return list;
    }

    // ── UPDATE status ────────────────────────────────────────────────
    public boolean updateStatus(int id, String newStatus) {
        String sql = "UPDATE applications SET status=? WHERE id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, newStatus);
            ps.setInt   (2, id);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error updating status: " + e.getMessage());
            return false;
        }
    }

    // ── DELETE application ───────────────────────────────────────────
    public boolean deleteApplication(int id) {
        String sql = "DELETE FROM applications WHERE id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error deleting application: " + e.getMessage());
            return false;
        }
    }

    // ── GET applications by status ───────────────────────────────────
    public List<Application> getByStatus(String status) {
        List<Application> list = new ArrayList<>();
        String sql = "SELECT * FROM applications WHERE status=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Application app = new Application(
                    rs.getString("company_name"),
                    rs.getString("role"),
                    rs.getDate("date_applied"),
                    rs.getString("status"),
                    rs.getString("notes"),
                    rs.getDate("follow_up_date")
                );
                app.setId(rs.getInt("id"));
                list.add(app);
            }
        } catch (SQLException e) {
            System.out.println("Error filtering applications: " + e.getMessage());
        }
        return list;
    }

    // ── GET follow-up reminders (due today or overdue) ───────────────
    public List<Application> getFollowUpReminders() {
        List<Application> list = new ArrayList<>();
        String sql = "SELECT * FROM applications WHERE follow_up_date <= CURDATE() AND status NOT IN ('Rejected','Offered')";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Application app = new Application(
                    rs.getString("company_name"),
                    rs.getString("role"),
                    rs.getDate("date_applied"),
                    rs.getString("status"),
                    rs.getString("notes"),
                    rs.getDate("follow_up_date")
                );
                app.setId(rs.getInt("id"));
                list.add(app);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching reminders: " + e.getMessage());
        }
        return list;
    }

    // ── GET stats summary ────────────────────────────────────────────
    public void printStats() {
        String sql = "SELECT status, COUNT(*) as count FROM applications GROUP BY status";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            System.out.println("\n===== APPLICATION STATISTICS =====");
            int total = 0;
            while (rs.next()) {
                System.out.printf("%-12s : %d%n", rs.getString("status"), rs.getInt("count"));
                total += rs.getInt("count");
            }
            System.out.println("----------------------------------");
            System.out.println("Total Applied : " + total);
            System.out.println("==================================\n");

        } catch (SQLException e) {
            System.out.println("Error fetching stats: " + e.getMessage());
        }
    }
}
