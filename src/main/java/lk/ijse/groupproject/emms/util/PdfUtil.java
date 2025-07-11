package lk.ijse.groupproject.emms.util;

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;

public class PdfUtil {

    public static void saveEmailAsPDF(String title, String body, String filePath) throws Exception {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();

        Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
        Font bodyFont = new Font(Font.FontFamily.HELVETICA, 12);

        document.add(new Paragraph("Email Title", titleFont));
        document.add(new Paragraph(title + "\n\n", bodyFont));
        document.add(new Paragraph("Email Body", titleFont));
        document.add(new Paragraph(body, bodyFont));

        document.close();
    }
}
