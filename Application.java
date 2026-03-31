// Application.java
// Model class representing a single job application

import java.sql.Date;

public class Application {
    private int    id;
    private String companyName;
    private String role;
    private Date   dateApplied;
    private String status;       // Applied / Interview / Rejected / Offered
    private String notes;
    private Date   followUpDate;

    // Constructor for adding new application (no id yet)
    public Application(String companyName, String role, Date dateApplied,
                       String status, String notes, Date followUpDate) {
        this.companyName  = companyName;
        this.role         = role;
        this.dateApplied  = dateApplied;
        this.status       = status;
        this.notes        = notes;
        this.followUpDate = followUpDate;
    }

    // Getters
    public int    getId()           { return id; }
    public String getCompanyName()  { return companyName; }
    public String getRole()         { return role; }
    public Date   getDateApplied()  { return dateApplied; }
    public String getStatus()       { return status; }
    public String getNotes()        { return notes; }
    public Date   getFollowUpDate() { return followUpDate; }

    // Setters
    public void setId(int id)              { this.id = id; }
    public void setCompanyName(String c)   { this.companyName = c; }
    public void setRole(String r)          { this.role = r; }
    public void setDateApplied(Date d)     { this.dateApplied = d; }
    public void setStatus(String s)        { this.status = s; }
    public void setNotes(String n)         { this.notes = n; }
    public void setFollowUpDate(Date f)    { this.followUpDate = f; }

    @Override
    public String toString() {
        return String.format("%-5d %-20s %-20s %-12s %-10s", 
                id, companyName, role, status, dateApplied);
    }
}
