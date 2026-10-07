package com.conecel.claro.bnas.m2m.generate_document.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.management.RuntimeErrorException;

import org.apache.logging.log4j.util.Strings;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import cn.afterturn.easypoi.excel.export.styler.ExcelExportStylerDefaultImpl;
import cn.afterturn.easypoi.excel.html.css.CssParseService;
import cn.afterturn.easypoi.excel.html.css.ICssConvertToExcel;
import cn.afterturn.easypoi.excel.html.css.impl.AlignCssConvertImpl;
import cn.afterturn.easypoi.excel.html.css.impl.BackgroundCssConvertImpl;
import cn.afterturn.easypoi.excel.html.css.impl.BorderCssConverImpl;
import cn.afterturn.easypoi.excel.html.css.impl.HeightCssConverImpl;
import cn.afterturn.easypoi.excel.html.css.impl.TextCssConvertImpl;
import cn.afterturn.easypoi.excel.html.css.impl.WidthCssConverImpl;
import cn.afterturn.easypoi.excel.html.entity.ExcelCssConstant;
import cn.afterturn.easypoi.excel.html.entity.HtmlCssConstant;
import cn.afterturn.easypoi.excel.html.entity.style.CellStyleEntity;
import cn.afterturn.easypoi.exception.excel.ExcelExportException;
import cn.afterturn.easypoi.exception.excel.enums.ExcelExportEnum;
import cn.afterturn.easypoi.util.PoiMergeCellUtil;

@Component
public class ExcelUtil {

	private ExcelUtil() {
	}
	
	private static final List<ICssConvertToExcel> STYLE_APPLIERS = new LinkedList<>();

	private static final List<ICssConvertToExcel> SHEET_APPLIERS = new LinkedList<>();

    static {
        STYLE_APPLIERS.add(new AlignCssConvertImpl());
        STYLE_APPLIERS.add(new BackgroundCssConvertImpl());
        STYLE_APPLIERS.add(new BorderCssConverImpl());
        STYLE_APPLIERS.add(new TextCssConvertImpl());
		SHEET_APPLIERS.add(new WidthCssConverImpl());
		SHEET_APPLIERS.add(new HeightCssConverImpl());
	}
	
	Sheet sheet = null;
	Map<String, Object> cellsOccupied = new HashMap<>();
	private final Map<String, CellStyle> cellStyles = new HashMap<>();
	private CellStyle defaultCellStyle;
	int maxRow = 0;
	private final CssParseService cssParse = new CssParseService();

	public Workbook htmlToExcel(String html) {
		Workbook workbook = new XSSFWorkbook();
		return createSheet(html, workbook);
	}

	public Workbook htmlToExcel(InputStream is) {
		try {
			return htmlToExcel(new String(IOUtils.toByteArray(is)));
		} catch (IOException e) {
			throw new ExcelExportException(ExcelExportEnum.HTML_ERROR, e);
		}
	}

	private void processTable(Element table) {
        int rowIndex = 0;
        if (maxRow > 0) {
            // blank row
            maxRow += 2;
            rowIndex = maxRow;
        }
        String freezeCol;
        int freezeColIndex = -1;
        for (Element row : table.select("tr")) {
            String freezeRow = row.attr(ExcelCssConstant.FREEZE_ROW);
            if ("true".equals(freezeRow)) {
                sheet.createFreezePane(0, rowIndex + 1, 0, rowIndex + 1);
            }
            int colIndex = 0;
            for (Element td : row.select("td, th")) {
                freezeCol = td.attr(ExcelCssConstant.FREEZE_COL);
                if ("true".equals(freezeCol)) {
                    if (colIndex > freezeColIndex) {
                        freezeColIndex = colIndex;
                    }
                }
 
                // skip occupied cell
                while (cellsOccupied.get(rowIndex + "_" + colIndex) != null) {
                    ++colIndex;
                }
                int rowSpan = 0;
                String strRowSpan = td.attr("rowspan");
                if (Strings.isNotBlank(strRowSpan) && UsefulFunctions.isNumeric(strRowSpan)) {
                    rowSpan = Integer.parseInt(strRowSpan);
                }
                int colSpan = 0;
                String strColSpan = td.attr("colspan");
                if (Strings.isNotBlank(strColSpan) && UsefulFunctions.isNumeric(strColSpan)) {

                    colSpan = Integer.parseInt(strColSpan);
                }
                // col span & row span
                if (colSpan > 1 && rowSpan > 1) {
                    spanRowAndCol(td, rowIndex, colIndex, rowSpan, colSpan);
                    colIndex += colSpan;
                }
                // col span only
                else if (colSpan > 1) {
                    spanCol(td, rowIndex, colIndex, colSpan);
                    colIndex += colSpan;
                }
                // row span only
                else if (rowSpan > 1) {
                    spanRow(td, rowIndex, colIndex, rowSpan);
                    ++colIndex;
                }
                // no span
                else {
                    createCell(td, getOrCreateRow(rowIndex), colIndex).setCellValue(td.text());
                    ++colIndex;
                }
            }
            ++rowIndex;
            ++maxRow;
        }
        if (freezeColIndex != -1) {
            sheet.createFreezePane(freezeColIndex + 1, 0, freezeColIndex + 1, 0);
        }
    }
 
    public Workbook createSheet(String html, Workbook workbook) {
        Elements els = Jsoup.parseBodyFragment(html).select("table");
        Map<String, Sheet> sheets = new HashMap<>();
        Map<String, Integer> maxrowMap = new HashMap<>();
        for (Element table : els) {
           // String sheetName = table.attr(ExcelCssConstant.SHEET_NAME);
            String sheetName="excel";
            if (Strings.isBlank(sheetName)) {
                throw new RuntimeErrorException(null, "table");
            }
 
            if (sheets.containsKey(sheetName)) {
                maxRow = maxrowMap.get(sheetName);
                sheet = sheets.get(sheetName);
            } else {
                maxRow = 0;
                cellStyles.clear();
                cellsOccupied.clear();
                sheet = workbook.createSheet(sheetName);
            }
            defaultCellStyle = new ExcelExportStylerDefaultImpl(workbook).stringNoneStyle(workbook,
                false);
            processTable(table);
            maxrowMap.put(sheetName, maxRow);
            sheets.put(sheetName, sheet);
        }
        return workbook;
    }
 
    private void spanRow(Element td, int rowIndex, int colIndex, int rowSpan) {
        mergeRegion(rowIndex, rowIndex + rowSpan - 1, colIndex, colIndex);
        for (int i = 0; i < rowSpan; ++i) {
            Row row = getOrCreateRow(rowIndex + i);
            createCell(td, row, colIndex);
            cellsOccupied.put((rowIndex + i) + "_" + colIndex, true);
        }
        getOrCreateRow(rowIndex).getCell(colIndex).setCellValue(td.text());
    }
 
    private void spanCol(Element td, int rowIndex, int colIndex, int colSpan) {
        mergeRegion(rowIndex, rowIndex, colIndex, colIndex + colSpan - 1);
        Row row = getOrCreateRow(rowIndex);
        for (int i = 0; i < colSpan; ++i) {
            createCell(td, row, colIndex + i);
        }
        row.getCell(colIndex).setCellValue(td.text());
    }
 
    private void spanRowAndCol(Element td, int rowIndex, int colIndex, int rowSpan, int colSpan) {
        mergeRegion(rowIndex, rowIndex + rowSpan - 1, colIndex, colIndex + colSpan - 1);
        for (int i = 0; i < rowSpan; ++i) {
            Row row = getOrCreateRow(rowIndex + i);
            for (int j = 0; j < colSpan; ++j) {
                createCell(td, row, colIndex + j);
                cellsOccupied.put((rowIndex + i) + "_" + (colIndex + j), true);
            }
        }
        getOrCreateRow(rowIndex).getCell(colIndex).setCellValue(td.text());
    }
 
    private Cell createCell(Element td, Row row, int colIndex) {
        Cell cell = row.getCell(colIndex);
        if (cell == null) {
            cell = row.createCell(colIndex);
        }
        return applyStyle(td, cell);
    }
 
    private Cell applyStyle(Element td, Cell cell) {
        String style = td.attr(HtmlCssConstant.STYLE);
        CellStyle cellStyle;
        if (Strings.isNotBlank(style)) {
            CellStyleEntity styleEntity = cssParse.parseStyle(style.trim());
            cellStyle = cellStyles.get(styleEntity.toString());
            if (cellStyle == null) {
                cellStyle = cell.getRow().getSheet().getWorkbook().createCellStyle();
                cellStyle.cloneStyleFrom(defaultCellStyle);
                for (ICssConvertToExcel cssConvert : STYLE_APPLIERS) {
                    cssConvert.convertToExcel(cell, cellStyle, styleEntity);
                }
                cellStyles.put(styleEntity.toString(), cellStyle);
            }
            for (ICssConvertToExcel cssConvert : SHEET_APPLIERS) {
                cssConvert.convertToExcel(cell, cellStyle, styleEntity);
            }
            if (cellStyles.size() >= 4000) {
               cellStyle = defaultCellStyle;
            }
        } else {
            cellStyle = defaultCellStyle;
        }
 
        cell.setCellStyle(cellStyle);
        return cell;
    }
 
    private Row getOrCreateRow(int rowIndex) {
        Row row = sheet.getRow(rowIndex);
        if (row == null) {
            row = sheet.createRow(rowIndex);
            if (rowIndex > maxRow) {
                maxRow = rowIndex;
            }
        }
        return row;
    }
 
    private void mergeRegion(int firstRow, int lastRow, int firstCol, int lastCol) {
        PoiMergeCellUtil.addMergedRegion(sheet,firstRow, lastRow, firstCol, lastCol);
    }

}
