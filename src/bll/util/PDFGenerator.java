package bll.util;

import be.Client;
import be.Project;
import bll.managers.ProjectManager;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfObject;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.colorspace.PdfColorSpace;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.DashedBorder;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.properties.UnitValue;
import exceptions.BLLException;
import util.SymbolPaths;
import com.itextpdf.io.font.constants.StandardFonts;

import java.io.IOException;
import java.net.URL;


public class PDFGenerator {

    public static void generateProjectPdf(Project project, String destination) throws Exception {
        PdfWriter pdfWriter = null;
        PdfDocument pdfDocument = null;
        Document document = null;
        try {
            final String fileName = project.getName().replaceAll(" ", "_") + "-" + project.getID() + ".pdf";

            pdfWriter = new PdfWriter(destination + "\\" + fileName);
            pdfDocument = new PdfDocument(pdfWriter);

            pdfDocument.addNewPage();

            document = new Document(pdfDocument);

            addLogo(document);
            addClientInfo(document, project.getClient());
        }
        catch (Exception e) {
            BLLException bllException = new BLLException("Failed to generate project PDF", e);
            bllException.printStackTrace();
            throw bllException;
        }
        finally {
            if (document != null) document.close();

            if (pdfDocument != null) pdfWriter.close();

            if (pdfWriter != null) pdfWriter.close();
        }
    }

    private static void addClientInfo(Document document, Client client) {
        Paragraph clientInfo = new Paragraph("Client Info");
        document.add(clientInfo);

        Table clientTable = new Table(UnitValue.createPercentArray(new float[] {5f, 5f})).useAllAvailableWidth();

        clientTable.setBorder(new SolidBorder(new DeviceRgb(0f, 0f, 0f), 1f));

        Paragraph clientName = new Paragraph("Name: " + client.getName());
        Paragraph clientPhone = new Paragraph("Phone: " + client.getPhone());
        Paragraph clientLocation = new Paragraph("Location: " + "\n" + client.getStreet() + "," + "\n" + client.getPostalCode() + " " + client.getCity());
        Paragraph clientEmail = new Paragraph("Email: " + client.getEmail());
        clientTable.addCell(new Cell()
                .add(clientName)
                .setBorder(Border.NO_BORDER));
        clientTable.addCell(new Cell()
                .add(clientPhone)
                .setBorder(Border.NO_BORDER));
        clientTable.addCell(
                new Cell()
                    .add(clientLocation)
                    .setBorder(Border.NO_BORDER));
        clientTable.addCell(new Cell()
                .add(clientEmail)
                .setBorder(Border.NO_BORDER));

        document.add(clientTable);
    }

    private static void addLogo(Document document) throws Exception {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL logoAbsolutePath = classLoader.getResource(SymbolPaths.LOGO_NO_BG).toURI().toURL();
        ImageData logoData = ImageDataFactory.create(logoAbsolutePath);
        Image logoImage = new Image(logoData);
        logoImage.setHeight(100);
        logoImage.setWidth(100);
        document.add(logoImage);
    }

    private static PdfFont getFontBold() throws Exception {
        return PdfFontFactory.createFont(StandardFonts.TIMES_BOLD);
    }

    private static PdfFont getFont() throws Exception {
        return PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN);
    }

    public static void main(String[] args) {
        try {
            ProjectManager projectManager = new ProjectManager();

            generateProjectPdf(projectManager.getAllProjects().get(0), "C:\\Users\\kaosk\\Desktop");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
