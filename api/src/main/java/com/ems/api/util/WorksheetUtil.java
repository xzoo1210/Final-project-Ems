package com.ems.api.util;

import org.apache.poi.ss.usermodel.*;

public class WorksheetUtil {
    public static void createSheet(Workbook w,String sheetName, String[][] data){
        Sheet sheet = w.createSheet(sheetName);
        int countRow = 0;
        Row row;
        //header
        row = sheet.createRow(countRow++);
        CellStyle headerStyle = w.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setBorderBottom(BorderStyle.MEDIUM);
        headerStyle.setBorderTop(BorderStyle.MEDIUM);
        headerStyle.setBorderRight(BorderStyle.MEDIUM);
        headerStyle.setBorderLeft(BorderStyle.MEDIUM);
        headerStyle.setWrapText(true);

        Font font =  w.createFont();
        font.setFontName("Times New Roman");
        font.setFontHeightInPoints((short) 16);
        font.setBold(true);
        headerStyle.setFont(font);

        for (int i = 0; i < data[0].length; i++) {
            {
                Cell headerCell = row.createCell(i);
                headerCell.setCellValue(data[0][i]);
                headerCell.setCellStyle(headerStyle);
            }
        }


    }
}
