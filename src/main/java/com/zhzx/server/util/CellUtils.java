package com.zhzx.server.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFCell;

import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class CellUtils {

    public static String getCellValue(XSSFCell cell) {
        try {
            if (cell.getCellType() == CellType.NUMERIC) {
                cell.setCellType(CellType.STRING);
                return cell.getStringCellValue() + "";
            }
            if (cell.getCellType() == CellType.STRING) {
                return cell.getStringCellValue() + "";
            }
            return "";
        } catch (Exception e) {
            return "";
        }
    }

    public static String getCellValue(Cell cell, FormulaEvaluator ... evaluator) {
        try {
            if (cell == null) {
                return "";
            }
            if (cell.getCellType() == CellType.NUMERIC) {
                cell.setCellType(CellType.STRING);
                return cell.getStringCellValue() + "";
            }
            if (cell.getCellType() == CellType.STRING) {
                return cell.getStringCellValue() + "";
            }
            if (cell.getCellType() == CellType.FORMULA && evaluator != null && evaluator.length > 0) {
                CellValue cellValue = evaluator[0].evaluate(cell);
                if (cellValue.getCellType() == CellType.NUMERIC) {
                    cell.setCellType(CellType.STRING);
                }
                return cell.getStringCellValue() + "";
            }
            return "";
        } catch (Exception e) {
            return "";
        }
    }

    public static String getCellValue(XSSFCell cell, String formatter) {

        if (cell.getCellType() == CellType.NUMERIC) {
            double value = cell.getNumericCellValue();
            if (StringUtils.isNullOrEmpty(formatter))
                return String.valueOf(value);
            if ("yyyy-MM-dd".equals(formatter) || "yyyy-MM-dd HH:mm:ss".equals(formatter)) {
                Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value);
                SimpleDateFormat sf = new SimpleDateFormat(formatter);
                return sf.format(date);
            }
            return String.format(formatter, value);
        }
        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue();
        }
        return null;
    }
}
