// ExcelExporter.java
// Exports all job applications to an Excel file using Apache POI

// Required library: apache-poi
// Download: https://poi.apache.org/download.html
// Add poi-5.x.jar and poi-ooxml-5.x.jar to your classpath

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelExporter {

    public static void exportToExcel(List<Application> apps, String filePath) {
        try (Workbook workbook = new XSSFWorkbook()) {

            Sheet sheet = workbook.createSheet("Job Applications");

            // ── Header row style ─────────────────────────────────────
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);

            // ── Create header row ────────────────────────────────────
            Row header = sheet.createRow(0);
            String[] columns = {"ID", "Company", "Role", "Date Applied", "Status", "Notes", "Follow-Up Date"};
            for (int i = 0; i < columns.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }

            // ── Fill data rows ───────────────────────────────────────
            CellStyle redStyle   = colorStyle(workbook, IndexedColors.ROSE);
            CellStyle greenStyle = colorStyle(workbook, IndexedColors.LIGHT_GREEN);
            CellStyle blueStyle  = colorStyle(workbook, IndexedColors.LIGHT_CORNFLOWER_BLUE);
            CellStyle yellowStyle= colorStyle(workbook, IndexedColors.LIGHT_YELLOW);

            int rowNum = 1;
            for (Application app : apps) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(app.getId());
                row.createCell(1).setCellValue(app.getCompanyName());
                row.createCell(2).setCellValue(app.getRole());
                row.createCell(3).setCellValue(app.getDateApplied() != null ? app.getDateApplied().toString() : "");
                row.createCell(4).setCellValue(app.getStatus());
                row.createCell(5).setCellValue(app.getNotes() != null ? app.getNotes() : "");
                row.createCell(6).setCellValue(app.getFollowUpDate() != null ? app.getFollowUpDate().toString() : "");

                // Color-code status column
                CellStyle style = switch (app.getStatus()) {
                    case "Rejected"  -> redStyle;
                    case "Offered"   -> greenStyle;
                    case "Interview" -> blueStyle;
                    default          -> yellowStyle;
                };
                row.getCell(4).setCellStyle(style);
            }

            // ── Auto-size columns ────────────────────────────────────
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // ── Save file ────────────────────────────────────────────
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
            }
            System.out.println("✅ Excel exported successfully to: " + filePath);

        } catch (IOException e) {
            System.out.println("Error exporting to Excel: " + e.getMessage());
        }
    }

    private static CellStyle colorStyle(Workbook wb, IndexedColors color) {
        CellStyle style = wb.createCellStyle();
        style.setFillForegroundColor(color.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }
}
