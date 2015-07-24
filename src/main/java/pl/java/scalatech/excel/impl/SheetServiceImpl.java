package pl.java.scalatech.excel.impl;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.assertj.core.util.Lists;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import pl.java.scalatech.excel.SheetService;

import com.google.common.collect.Maps;
import com.google.common.collect.Table;

@Slf4j
@Scope("prototype")
@Component
public class SheetServiceImpl implements SheetService {

    private Map<Object, Object> dateFormatterCache = Maps.newHashMap();
    private static final int HEAD_POSITION = 0;
    private static final int START_POPULATE_FROM_FIRST_ROW = 1;
    private int i;

    @Override
    @SneakyThrows(value = { FileNotFoundException.class, IOException.class })
    public void generateExcelSheet(String fileName, String headName, Map<String, ColumnInfo<?>> columnMap, Table<Integer, String, CellItem<?>> table) {
        // 
        try (FileOutputStream fileOut = new FileOutputStream(fileName)) {

            @SuppressWarnings("resource")
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet(headName);
            createHeader(table.columnKeySet(), sheet);
            table.rowKeySet().stream().forEach(row -> {
                HSSFRow hssRow = sheet.createRow(row + START_POPULATE_FROM_FIRST_ROW);
                int i = 0;
                Map<String, CellItem<?>> rowCellValue = table.row(row);
                for (String column : table.columnKeySet()) {
                    if (columnMap.containsKey(column) && columnMap.get(column).getPattern().isPresent()) {
                        timeDateFormatter(columnMap, hssRow, i, rowCellValue, column);
                        digitFormatter(columnMap, hssRow, i, rowCellValue, column);
                    } else {
                        hssRow.createCell(i).setCellValue(rowCellValue.get(column).getValue().toString());
                    }
                    i++;
                }
            });
            workbook.write(fileOut);
        }
    }

    private void digitFormatter(Map<String, ColumnInfo<?>> columnMap, HSSFRow hssRow, int i, Map<String, CellItem<?>> rowCellValue, String column) {
        String pattern = columnMap.get(column).getPattern().get();
        if (BigDecimal.class.getName().equals(columnMap.get(column).getType().getName())) {
            String result = null;
            BigDecimal ld = (BigDecimal) rowCellValue.get(column).getValue();
            dateFormatterCache.putIfAbsent(columnMap.get(column).getType().getName(), new DecimalFormat(pattern));
            result = ((DecimalFormat) dateFormatterCache.get(columnMap.get(column).getType().getName())).format(ld);
            log.debug("BigDecimal : => result {} , pattern {}", result, pattern);
            hssRow.createCell(i).setCellValue(result);
        }
    }

    private void timeDateFormatter(Map<String, ColumnInfo<?>> columnMap, HSSFRow hssRow, int i, Map<String, CellItem<?>> rowCellValue, String column) {
        String pattern = columnMap.get(column).getPattern().get();
        List<String> newTimeClazz = Lists.newArrayList(LocalDate.class.getName(), LocalDateTime.class.getName(), LocalTime.class.getName(),
                Date.class.getName());
        if (newTimeClazz.contains(columnMap.get(column).getType().getName())) {
            String result = null;
            if (rowCellValue.get(column).getValue() instanceof LocalDate) {
                LocalDate ld = (LocalDate) rowCellValue.get(column).getValue();
                dateFormatterCache.putIfAbsent(columnMap.get(column).getType().getName(), java.time.format.DateTimeFormatter.ofPattern(pattern));
                result = ld.format((DateTimeFormatter) dateFormatterCache.get(columnMap.get(column).getType().getName()));
                log.debug("LocalDate : => result {} , pattern {}", result, pattern);

            }
            if (rowCellValue.get(column).getValue() instanceof LocalDateTime) {
                LocalDateTime ld = (LocalDateTime) rowCellValue.get(column).getValue();
                dateFormatterCache.putIfAbsent(columnMap.get(column).getType().getName(), java.time.format.DateTimeFormatter.ofPattern(pattern));
                result = ld.format((DateTimeFormatter) dateFormatterCache.get(columnMap.get(column).getType().getName()));
                log.debug("LocalDateTime : => result {} , pattern {}", result, pattern);
            }
            if (rowCellValue.get(column).getValue() instanceof LocalTime) {
                LocalTime ld = (LocalTime) rowCellValue.get(column).getValue();
                dateFormatterCache.putIfAbsent(columnMap.get(column).getType().getName(), java.time.format.DateTimeFormatter.ofPattern(pattern));
                result = ld.format((DateTimeFormatter) dateFormatterCache.get(columnMap.get(column).getType().getName()));
                log.debug("LocalTime : => result {} , pattern {}", result, pattern);
            }
            if (rowCellValue.get(column).getValue() instanceof Date) {
                Date ld = (Date) rowCellValue.get(column).getValue();
                dateFormatterCache.putIfAbsent(columnMap.get(column).getType().getName(), new SimpleDateFormat(pattern));
                result = ((SimpleDateFormat) dateFormatterCache.get(columnMap.get(column).getType().getName())).format(ld);
                log.debug("java.util.Date : => result {} , pattern {}", result, pattern);
            }

            hssRow.createCell(i).setCellValue(result);
        }
    }

    private void createHeader(Set<String> columnNames, HSSFSheet sheet) {
        i = 0;
        HSSFRow rowhead = sheet.createRow(HEAD_POSITION);
        columnNames.stream().peek(t -> log.info("{}", t)).forEach(t -> {
            rowhead.createCell(i++).setCellValue(t);
        });
    }

}
