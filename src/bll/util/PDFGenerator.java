package bll.util;

import be.Client;
import be.Installation;
import be.Project;
import bll.managers.ProjectManager;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.splitting.BreakAllSplitCharacters;
import exceptions.BLLException;
import util.SymbolPaths;

import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.List;


public class PDFGenerator {

    public static void generateProjectPdf(Project project, List<Installation> installations, String destination) throws Exception {
        PdfWriter pdfWriter = null;
        PdfDocument pdfDocument = null;
        Document document = null;
        try {
            final String fileName = project.getName().replaceAll(" ", "_") + "-" + project.getID() + ".pdf";

            pdfWriter = new PdfWriter(destination + "\\" + fileName);
            pdfDocument = new PdfDocument(pdfWriter);

            pdfDocument.addNewPage();

            document = new Document(pdfDocument);

            document.setFont(getFont());

            addLogo(document);
            addClientInfo(document, project.getClient());
            addProjectInfo(document, project);
            addInstallationInfo(document, installations);

            Desktop.getDesktop().open(new File(destination + "\\" + fileName));
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

    private static void addInstallationInfo(Document document, List<Installation> installations) throws Exception {
        Paragraph installationInfo = new Paragraph("Installation Info").setFont(getFontBold());
        installationInfo.setMarginTop(getMargin());
        document.add(installationInfo);

        Table installationTable = new Table(UnitValue.createPercentArray(new float[] {5f, 5f})).useAllAvailableWidth();
        installationTable.setBorder(new SolidBorder(new DeviceRgb(0f, 0f, 0f), 1f));

        for(Installation installation : installations) {
            Paragraph installationName = getParagraph("Name: ", getFontBold());
            installationName.add(getParagraph(installation.getName(), getFont()));

            Paragraph projectDescription = getParagraph("Description: ", getFontBold());
            projectDescription.add(getParagraph(installation.getDescription(), getFont()));

            installationTable.addCell(getNoBorderedTableCell(installationName));
            installationTable.addCell(getNoBorderedTableCell(projectDescription));
        }
        document.add(installationTable);
    }

    private static void addProjectInfo(Document document, Project project) throws Exception {
        Paragraph projectInfo = new Paragraph("Project Info").setFont(getFontBold());
        projectInfo.setMarginTop(getMargin());
        document.add(projectInfo);

        Table projectTable = new Table(UnitValue.createPercentArray(new float[] {5f, 5f})).useAllAvailableWidth();
        projectTable.setBorder(new SolidBorder(new DeviceRgb(0f, 0f, 0f), 1f));

        Paragraph projectName = getParagraph("Name: ", getFontBold());
        projectName.add(getParagraph(project.getName(), getFont()));

        Paragraph projectAddress = getParagraph("Address: ", getFontBold());
        projectAddress.add(getParagraph(project.getAddress().getStreet() + ", " + project.getAddress().getPostalCode() + project.getAddress().getCity(), getFont()));

        Paragraph projectDescription = getParagraph("Description: ", getFontBold());
        projectDescription.add(getParagraph(project.getDescription(), getFont()));

        Paragraph projectCreated = getParagraph("Created: ", getFontBold());
        projectCreated.add(getParagraph(project.getCreated().toString(), getFont()));

        projectTable.addCell(getNoBorderedTableCell(projectName));
        projectTable.addCell(getNoBorderedTableCell(projectAddress));
        projectTable.addCell(getNoBorderedTableCell(projectDescription));
        projectTable.addCell(getNoBorderedTableCell(projectCreated));

        document.add(projectTable);
    }

    private static void addClientInfo(Document document, Client client) throws Exception {
        Paragraph clientInfo = new Paragraph("Client Info").setFont(getFontBold());
        clientInfo.setMarginTop(getMargin());
        document.add(clientInfo);

        Table clientTable = new Table(UnitValue.createPercentArray(new float[] {5f, 5f})).useAllAvailableWidth();
        clientTable.setBorder(new SolidBorder(new DeviceRgb(0f, 0f, 0f), 1f));

        Paragraph clientName = getParagraph("Name: ", getFontBold());
        clientName.add(getParagraph(client.getName(), getFont()));

        Paragraph clientPhone = getParagraph("Phone: ", getFontBold());
        clientPhone.add(getParagraph(client.getPhone(), getFont()));

        Paragraph clientLocation = getParagraph("Location: ", getFontBold());
        clientLocation.add(getParagraph(client.getStreet() + ", " + client.getPostalCode() + " " + client.getCity(), getFont()));

        Paragraph clientEmail = getParagraph("Email: ", getFontBold());
        clientEmail.add(getParagraph(client.getEmail(), getFont()));

        clientTable.addCell(getNoBorderedTableCell(clientName));
        clientTable.addCell(getNoBorderedTableCell(clientPhone));
        clientTable.addCell(getNoBorderedTableCell(clientLocation));
        clientTable.addCell(getNoBorderedTableCell(clientEmail));

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

    private static Cell getNoBorderedTableCell(Paragraph paragraph) {
        return new Cell().add(paragraph).setBorder(Border.NO_BORDER);
    }

    private static Paragraph getParagraph(String text, PdfFont font) {
        return new Paragraph(text).setFont(font).setSplitCharacters(new BreakAllSplitCharacters());
    }

    private static PdfFont getFontBold() throws Exception {
        return PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
    }

    private static PdfFont getFont() throws Exception {
        return PdfFontFactory.createFont(StandardFonts.HELVETICA);
    }

    private static float getMargin()  {
        return 20f;
    }

    public static void main(String[] args) {
        try {
            ProjectManager projectManager = new ProjectManager();

            generateProjectPdf(projectManager.getAllProjects().get(0), null, "C:\\Users\\kaosk\\Desktop");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
