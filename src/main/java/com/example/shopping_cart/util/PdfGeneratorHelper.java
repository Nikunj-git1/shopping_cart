package com.example.shopping_cart.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.util.List;

import static com.example.shopping_cart.util.HexColorHelper.hexToBaseColor;
import static com.itextpdf.text.BaseColor.BLACK;
import static com.itextpdf.text.BaseColor.BLUE;

@Component
public class PdfGeneratorHelper extends PdfPageEventHelper {


    private String backgroundImagePath;
    private Image backgroundImage;
    private float backgroundImageOpacity = 1.0f;
    private BaseColor backgroundColor;
    private String watermarkText;
    private float watermarkOpacity = 0.2f;
    private int watermarkFontSize = 50;
    private int watermarkAngle = 45;
    private BaseColor watermarkTextColor = BaseColor.GRAY;

    // ===== SETTERS FOR CONFIGURATION =====

    public PdfGeneratorHelper setBackgroundImagePath(String imagePath, float opacity) {
        this.backgroundImagePath = imagePath;
        // Ensure opacity stays between 0.0 and 1.0
        this.backgroundImageOpacity = Math.max(0.0f, Math.min(1.0f, opacity));
        return this;
    }

    public PdfGeneratorHelper setBackgroundColor(BaseColor color) {
        this.backgroundColor = color;
        return this;
    }

    public PdfGeneratorHelper setWatermarkText(String text, float opacity, int fontSize, int angle) {
        this.watermarkText = text;
        this.watermarkOpacity = opacity;
        this.watermarkFontSize = fontSize;
        this.watermarkAngle = angle;
        return this;
    }

    public PdfGeneratorHelper setWatermarkTextColor(BaseColor color) {
        this.watermarkTextColor = color;
        return this;
    }

    // ===== MAIN METHOD TO EXPORT TABLE TO PDF =====

    public <T> void exportToPdfWithStyling(List<T> dtoList, String outputPath) throws Exception {
        if (dtoList == null || dtoList.isEmpty()) {
            throw new IllegalArgumentException("DTO list is empty or null.");
        }

        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(outputPath));

        // 🔥 Set background image/watermark to apply on every page
        writer.setPageEvent(this);

        setPdfEncryption(writer);
        document.open();

        addPdfTitle(document, "Category List");

        PdfPTable table = generateDynamicTable(dtoList);
        document.add(table);

        document.close();
    }


    // ===== PRIVATE UTILITY METHODS =====

    private void setPdfEncryption(PdfWriter writer) throws DocumentException {
        String userPassword = "user";
        String ownerPassword = "admin";
        writer.setEncryption(userPassword.getBytes(), ownerPassword.getBytes(),
                PdfWriter.ALLOW_PRINTING, PdfWriter.ENCRYPTION_AES_128);
    }

    private void applyBackgroundOrWatermark(PdfContentByte canvas, Rectangle pageSize) {
        if (!tryApplyBackgroundImage(canvas, pageSize)) {
            if (!tryApplyBackgroundColor(canvas, pageSize)) {
                applyWatermark(canvas, pageSize, watermarkText != null ? watermarkText : "WATERMARK");
            }
        }
    }

    /**
     * Attempts to apply the background image with specified opacity.
     *
     * @return true if background image applied successfully, else false.
     */
    private boolean tryApplyBackgroundImage(PdfContentByte canvas, Rectangle pageSize) {
        if (backgroundImagePath == null) {
            return false;
        }

        try {
            File file = new File(backgroundImagePath);
            if (!file.exists()) {
                return false;
            }

            backgroundImage = Image.getInstance(file.getAbsolutePath());
            backgroundImage.scaleToFit(pageSize.getWidth(), pageSize.getHeight());

            // Calculate center position
            float x = (pageSize.getWidth() - backgroundImage.getScaledWidth()) / 2;
            float y = (pageSize.getHeight() - backgroundImage.getScaledHeight()) / 2;
            backgroundImage.setAbsolutePosition(x, y);

            // Set opacity
            PdfGState gState = new PdfGState();
            gState.setFillOpacity(backgroundImageOpacity);
            gState.setStrokeOpacity(backgroundImageOpacity);

            // Apply image with transparency
            canvas.saveState();
            canvas.setGState(gState);
            canvas.addImage(backgroundImage);
            canvas.restoreState();

            return true;

        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Attempts to fill background with specified color.
     *
     * @return true if color applied successfully, else false.
     */
    private boolean tryApplyBackgroundColor(PdfContentByte canvas, Rectangle pageSize) {
        try {
            canvas.setColorFill(backgroundColor != null ? backgroundColor : hexToBaseColor("#9CC7EC"));
            canvas.rectangle(0, 0, pageSize.getWidth(), pageSize.getHeight());
            canvas.fill();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //      Applies a watermark text on the PDF canvas.
    private void applyWatermark(PdfContentByte canvas, Rectangle pageSize, String text) {
        try {
            BaseFont baseFont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.EMBEDDED);
            canvas.beginText();
            canvas.setFontAndSize(baseFont, watermarkFontSize);
            canvas.setColorFill(watermarkTextColor);
            canvas.showTextAligned(Element.ALIGN_CENTER, text,
                    pageSize.getWidth() / 2, pageSize.getHeight() / 2, watermarkAngle);
            canvas.endText();
        } catch (Exception e) {
            // Ignore fallback error
        }
    }

    //      Adds title to PDF document.
    private void addPdfTitle(Document document, String titleText) throws DocumentException {
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(BLUE);

        Paragraph title = new Paragraph(titleText, font);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
    }

    //      Dynamically generates a PdfPTable from the DTO list by reflecting on its fields.
    private <T> PdfPTable generateDynamicTable(List<T> dtoList) {
        Class<?> dtoClass = dtoList.get(0).getClass();
        Field[] fields = dtoClass.getDeclaredFields();

        PdfPTable table = new PdfPTable(fields.length);
        table.setWidthPercentage(100f);
        table.setSpacingBefore(10);

        writeTableHeader(table, fields);
        writeTableData(table, dtoList, fields);

        return table;
    }

    //      Writes table header cells with field names.
    private void writeTableHeader(PdfPTable table, Field[] fields) {
        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(BLACK);

        for (Field field : fields) {
            field.setAccessible(true);
            PdfPCell cell = new PdfPCell(new Phrase(toTitleCase(field.getName()), font));
            cell.setBackgroundColor(hexToBaseColor("#9CECC1"));
            cell.setPadding(5);
            table.addCell(cell);
        }
    }

    //      Writes the data rows into the table by fetching values via reflection.
    private <T> void writeTableData(PdfPTable table, List<T> dtoList, Field[] fields) {
        for (T item : dtoList) {
            for (Field field : fields) {
                field.setAccessible(true);
                try {
                    Object value = field.get(item);
                    table.addCell(value != null ? value.toString() : "");
                } catch (IllegalAccessException e) {
                    table.addCell("ERROR");
                }
            }
        }
    }

    //      Converts camelCase or snake_case field names to Title Case strings for headers.
    private String toTitleCase(String input) {
        return input.replaceAll("([a-z])([A-Z])", "$1 $2")
                .replace("_", " ")
                .replaceAll("\\s+", " ")
                .trim()
                .replaceFirst(".", input.substring(0, 1).toUpperCase());
    }


    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        PdfContentByte canvas = writer.getDirectContentUnder();
        Rectangle pageSize = document.getPageSize();

        // Always apply background/watermark per page
        applyBackgroundOrWatermark(canvas, pageSize);
    }
}