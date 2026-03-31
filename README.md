# Job Application Tracker
A Java + MySQL console application to track job applications.

## Features
- Add, view, update, delete job applications
- Filter applications by status (Applied / Interview / Rejected / Offered)
- Follow-up reminders shown on startup
- View statistics (count per status)
- Export all data to color-coded Excel file

## Technologies Used
- Java (JDK 11+)
- MySQL (Database)
- Apache POI (Excel export)
- JDBC (Java-MySQL connection)

## How to Set Up

### Step 1 – Install Requirements
- Install [Java JDK 11+](https://www.oracle.com/java/technologies/downloads/)
- Install [MySQL](https://dev.mysql.com/downloads/)
- Download [mysql-connector-j.jar](https://dev.mysql.com/downloads/connector/j/)
- Download [Apache POI jars](https://poi.apache.org/download.html) (poi-5.x.jar + poi-ooxml-5.x.jar)

### Step 2 – Set Up Database
1. Open MySQL Workbench or terminal
2. Run the file: `database.sql`
   ```
   mysql -u root -p < database.sql
   ```

### Step 3 – Configure DB Password
- Open `src/DBConnection.java`
- Change `PASSWORD = ""` to your MySQL password

### Step 4 – Compile & Run
```bash
# Compile (add your jar paths)
javac -cp ".;mysql-connector-j.jar;poi-5.x.jar;poi-ooxml-5.x.jar" src/*.java

# Run
java -cp ".;mysql-connector-j.jar;poi-5.x.jar;poi-ooxml-5.x.jar" Main
```

## Project Structure
```
job_tracker/
├── database.sql          ← Run this in MySQL first
├── src/
│   ├── DBConnection.java ← MySQL connection setup
│   ├── Application.java  ← Model class
│   ├── ApplicationDAO.java ← All DB operations
│   ├── ExcelExporter.java  ← Export to Excel
│   └── Main.java         ← Menu & main program
└── README.md
```

## How It Works
1. On startup → shows pending follow-up reminders
2. Choose from menu options (1–8)
3. Add applications with company, role, date, status, notes
4. Update status as you progress through hiring rounds
5. Export to Excel anytime for offline review
