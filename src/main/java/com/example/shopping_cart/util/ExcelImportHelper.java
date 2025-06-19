package com.example.shopping_cart.util;

import com.example.shopping_cart.request_dto.ExcelErrorDTOResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ExcelImportHelper {

    private static final ThreadLocal<List<ExcelErrorDTOResponse>> threadLocalErrors = ThreadLocal.withInitial(ArrayList::new);

    // ✅ Entry Point: Step 12 - Master method to call in service
    public static  <T> List<T> processExcelFile(MultipartFile file, Class<T> dtoClass) {
        clearValidationErrors();
        validateFileExtension(file);               // Step 1
        validateFileNotEmpty(file);                // Step 2
        return parseAndValidateExcelRows(file, dtoClass); // Steps 3-11
    }

    // ✅ Step 1: Validate Excel File Extension
    private static void validateFileExtension(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (fileName == null || (!fileName.toLowerCase().endsWith(".xls") && !fileName.toLowerCase().endsWith(".xlsx"))) {
            throw new IllegalArgumentException("Invalid file format. Only .xls or .xlsx are supported.");
        }
    }

    // ✅ Step 2: Check if File is Empty
    private static void validateFileNotEmpty(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Uploaded file is empty.");
        }
    }

    // ✅ Step 3–11: Parse Excel, Map to DTOs, Validate Fields
    private static <T> List<T> parseAndValidateExcelRows(MultipartFile file, Class<T> dtoClass) {
        List<T> validDTOs = new ArrayList<>();

        try (InputStream is = file.getInputStream(); Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            Row headerRow = sheet.getRow(0);

            if (headerRow == null) throw new IllegalArgumentException("Excel file does not contain a header row.");

            List<String> headers = extractHeaders(headerRow);
            validateHeaderCount(headers, dtoClass);       // Step 3
            validateHeaderNames(headers, dtoClass);       // Step 4

            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            Validator validator = factory.getValidator();

            Set<String> seenFieldKeys = new HashSet<>();

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                T dtoInstance = dtoClass.getDeclaredConstructor().newInstance();
                boolean isRowValid = true;
                seenFieldKeys.clear(); // Clear for each row

                for (int j = 0; j < headers.size(); j++) {
                    String fieldName = headers.get(j);
                    Field field;
                    try {
                        field = dtoClass.getDeclaredField(fieldName);
                        field.setAccessible(true);
                        Cell cell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                        Object cellValue = extractAndConvertCellValue(cell, field.getType()); // Step 6, 7, 8
                        field.set(dtoInstance, cellValue);
                    } catch (Exception e) {
                        isRowValid = false;
                        threadLocalErrors.get().add(
                                new ExcelErrorDTOResponse(
                                        i, fieldName, "Invalid field or mapping", ""));
                    }
                }

                Set<ConstraintViolation<T>> violations = validator.validate(dtoInstance);
                if (!violations.isEmpty()) {
                    isRowValid = false;
                    for (ConstraintViolation<T> violation : violations) {
                        String field = violation.getPropertyPath().toString();
                        String key = (i + 1) + "-" + field; // Unique key per row+field
                        if (seenFieldKeys.contains(key)) {
                            continue;
                        }
                        seenFieldKeys.add(key);

                        Object value = null;
                        try {
                            Field f = dtoClass.getDeclaredField(field);
                            f.setAccessible(true);
                            value = f.get(dtoInstance);
                        } catch (Exception ignored) {
                        }
                        threadLocalErrors.get().add(
                                new ExcelErrorDTOResponse(
                                        i, field, violation.getMessage(), String.valueOf(value)));
                    }
                }

                if (isRowValid) {
                    validDTOs.add(dtoInstance); // Step 6: Only fully valid row is saved
                }
            }


        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Excel file: " + e.getMessage(), e);
        }

        return validDTOs;
    }

    // ✅ Step 3: Check Header Count with DTO
    private static <T> void validateHeaderCount(List<String> headers, Class<T> dtoClass) {
        if (headers.size() != dtoClass.getDeclaredFields().length) {
            throw new IllegalArgumentException("Header count does not match DTO field count.");
        }
    }

    // ✅ Step 4: Match Header Names with DTO Fields
    private static <T> void validateHeaderNames(List<String> headers, Class<T> dtoClass) {
        Set<String> dtoFields = Arrays.stream(dtoClass.getDeclaredFields())
                .map(Field::getName)
                .collect(Collectors.toSet());

        if (!new HashSet<>(headers).equals(dtoFields)) {
            throw new IllegalArgumentException("Excel headers do not match DTO fields.");
        }
    }

    // ✅ Step 6-8: Extract, Clean, and Convert Cell Value
    private static Object extractAndConvertCellValue(Cell cell, Class<?> targetType) {
        if (cell == null) return null;

        try {
            switch (cell.getCellType()) {
                case STRING -> {
                    String str = cell.getStringCellValue().replaceAll("\\s+", " ").trim();
                    return targetType == String.class ? toTitleCase(str) : str;
                }
                case NUMERIC -> {
                    if (DateUtil.isCellDateFormatted(cell) && targetType == LocalDate.class) {
                        return cell.getLocalDateTimeCellValue().toLocalDate();
                    }
                    return targetType == Double.class || targetType == double.class
                            ? cell.getNumericCellValue()
                            : (int) cell.getNumericCellValue();
                }
                case BOOLEAN -> {
                    if (targetType == Boolean.class || targetType == boolean.class) {
                        return cell.getBooleanCellValue();
                    }
                }
                case BLANK -> {
                    return targetType == String.class ? "" : null;
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    // ✅ Helper: Convert to Title Case
    private static String toTitleCase(String input) {
        return Arrays.stream(input.trim().split(" "))
                .map(word -> word.isEmpty()
                        ? "" : Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
    }

    // ✅ Helper: Extract headers
    private static List<String> extractHeaders(Row row) {
        List<String> headers = new ArrayList<>();
        for (Cell cell : row) {
            headers.add(cell.getStringCellValue().trim());
        }
        return headers;
    }

    // ✅ Step 9 & 14: Error Handling Methods
    public static List<ExcelErrorDTOResponse> getValidationErrors() {
        List<ExcelErrorDTOResponse> temp = threadLocalErrors.get();

        return temp;
    }

    public static void clearValidationErrors() {

        threadLocalErrors.remove();
    }

    // ✅ नया मेथड: फाइनल JSON रिस्पॉन्स तैयार करता है
    public static Map<String, Object> buildImportResponseDataOnly(
            List<?> validDTOs,
            List<ExcelErrorDTOResponse> validationErrors,
            int inserted,
            int updated
    ) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("errors", validationErrors);

        // ✅ Unique error rowNumbers only (Set will auto-remove duplicates)
        Set<Integer> uniqueErrorRows = validationErrors.stream()
                .map(ExcelErrorDTOResponse::getRowNumber)
                .collect(Collectors.toSet());

        Map<String, Object> summary = new LinkedHashMap<>();
        summary.put("total_row", validDTOs.size() + uniqueErrorRows.size());
        summary.put("total_insert_row", inserted);
        summary.put("total_update_row", updated);
        summary.put("total_error_row", uniqueErrorRows.size());

        data.put("summary", summary);

        return data;
    }
}