package com.nineplus.bestwork.services;

import java.awt.Color;
import java.io.IOException;
import java.util.List;
 
import javax.servlet.http.HttpServletResponse;
 
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.nineplus.bestwork.entity.MedicineEntity;

public class MedicinePDFExporterService {

	private List<MedicineEntity> listMedicine;
    
    public MedicinePDFExporterService(List<MedicineEntity> listMedicine) {
        this.listMedicine = listMedicine;
    }
 
    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.BLUE);
        cell.setPadding(5);
         
        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);
         
        cell.setPhrase(new Phrase("User ID", font));
         
        table.addCell(cell);
         
        cell.setPhrase(new Phrase("Medicine Name", font));
        table.addCell(cell);
         
        cell.setPhrase(new Phrase("Medicine Company", font));
        table.addCell(cell);
         
//        cell.setPhrase(new Phrase("Roles", font));
//        table.addCell(cell);
//         
//        cell.setPhrase(new Phrase("Enabled", font));
//        table.addCell(cell);       
    }
     
    private void writeTableData(PdfPTable table) {
        for (MedicineEntity medicine : listMedicine) {
            table.addCell(String.valueOf(medicine.getId()));
            table.addCell(medicine.getMedicineName());
            table.addCell(medicine.getMedicineCompany());
            //table.addCell(medicine.getRoles().toString());
            //table.addCell(String.valueOf(medicine.isEnabled()));
        }
    }
     
    public void export(HttpServletResponse response) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
         
        document.open();
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(Color.BLUE);
         
        Paragraph p = new Paragraph("List of Users", font);
        p.setAlignment(Paragraph.ALIGN_CENTER);
         
        document.add(p);
         
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100f);
        table.setWidths(new float[] {1.5f, 3.5f, 3.0f, 3.0f, 1.5f});
        table.setSpacingBefore(10);
         
        writeTableHeader(table);
        writeTableData(table);
         
        document.add(table);
         
        document.close();
         
    }
}
