package com.Soganis.Controller;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.awt.*;
import java.awt.print.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import javax.print.PrintService;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;

public class PDFPrintable implements Printable {

    private FileInputStream fileInputStream;

    public PDFPrintable(FileInputStream fileInputStream) {
        this.fileInputStream = fileInputStream;
    }
    
    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        if (pageIndex > 0) {
            return Printable.NO_SUCH_PAGE;
        }

        try {
            PDDocument document = PDDocument.load(fileInputStream);
            PrinterJob printerJob = PrinterJob.getPrinterJob();
            PageFormat defaultPageFormat = printerJob.defaultPage();
            pageFormat.setOrientation(defaultPageFormat.getOrientation());
            if (printerJob.printDialog()) {
                PrintService printService = printerJob.getPrintService();
                PDFRenderer pdfRenderer = new PDFRenderer(document);
                BufferedImage bufferedImage = pdfRenderer.renderImage(0);
                document.close();
                PrintRequestAttributeSet attributes = new HashPrintRequestAttributeSet();
                attributes.add(new Copies(1)); // Example: Set number of copies
                printerJob.setPrintService(printService);
                printerJob.setPrintable(new ImagePrintable(bufferedImage), pageFormat);
                printerJob.print(attributes);

                return Printable.PAGE_EXISTS;
            } else {
                return Printable.NO_SUCH_PAGE;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return Printable.NO_SUCH_PAGE;
        }
    }

    private static class ImagePrintable implements Printable {

        private final Image image;

        public ImagePrintable(Image image) {
            this.image = image;
        }
        
        

        @Override
        public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
            if (pageIndex > 0) {
                return Printable.NO_SUCH_PAGE;
            }

            Graphics2D graphics2D = (Graphics2D) graphics;
            graphics2D.drawImage(image, 0, 0, null);
            return Printable.PAGE_EXISTS;
        }
    }
}
