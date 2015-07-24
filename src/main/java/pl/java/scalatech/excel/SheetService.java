package pl.java.scalatech.excel;

import java.util.Map;

import pl.java.scalatech.excel.impl.CellItem;
import pl.java.scalatech.excel.impl.ColumnInfo;

import com.google.common.collect.Table;

public interface SheetService {
    void generateExcelSheet(String fileName, String headName, Map<String, ColumnInfo<?>> columnMap, Table<Integer, String, CellItem<?>> table);
}
