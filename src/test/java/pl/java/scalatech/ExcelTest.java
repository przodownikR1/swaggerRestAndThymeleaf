package pl.java.scalatech;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import pl.java.scalatech.config.ExcelConfig;
import pl.java.scalatech.excel.SheetService;
import pl.java.scalatech.excel.impl.CellItem;
import pl.java.scalatech.excel.impl.ColumnInfo;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ExcelConfig.class)
public class ExcelTest {
    @Autowired
    private SheetService sheetService;

    @Test
    public void shouldExcelSheetCreate() {
        Map<String, ColumnInfo<?>> columnMap = Maps.newLinkedHashMap();
        columnMap.put("name", new ColumnInfo<>(Optional.empty(), String.class));
        columnMap.put("age", new ColumnInfo<>(Optional.empty(), Integer.class));
        columnMap.put("birthDate", new ColumnInfo<>(Optional.of("yyyy-MM-dd"), LocalDate.class));
        columnMap.put("event", new ColumnInfo<>(Optional.of("yy-MM-dd"), Date.class));
        columnMap.put("salary", new ColumnInfo<>(Optional.of("'$'00.####"), BigDecimal.class));

        sheetService.generateExcelSheet("test.xls", "test", columnMap, populateTable());
        Assertions.assertThat(new File("test.xls").exists()).isTrue();

    }

    private Table<Integer, String, CellItem<?>> populateTable() {
        Table<Integer, String, CellItem<?>> table = HashBasedTable.create();
        table.put(0, "name", new CellItem<>("slawek"));
        table.put(0, "age", new CellItem<>("12"));
        table.put(0, "birthDate", new CellItem<>(LocalDate.of(1989, Month.AUGUST, 5)));
        table.put(0, "event", new CellItem<>(new Date()));
        table.put(0, "salary", new CellItem<>(BigDecimal.valueOf(33.22)));

        table.put(1, "name", new CellItem<>("piotr"));
        table.put(1, "age", new CellItem<>("34"));
        table.put(1, "birthDate", new CellItem<>(LocalDate.of(1979, Month.MAY, 3)));
        table.put(1, "event", new CellItem<>(new Date()));
        table.put(1, "salary", new CellItem<>(BigDecimal.valueOf(53.22)));

        table.put(2, "name", new CellItem<>("bak"));
        table.put(2, "age", new CellItem<>("3"));
        table.put(2, "birthDate", new CellItem<>(LocalDate.now()));
        table.put(2, "event", new CellItem<>(new Date()));
        table.put(2, "salary", new CellItem<>(BigDecimal.valueOf(73.232)));
        return table;
    }
}
