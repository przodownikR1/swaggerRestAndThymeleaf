package pl.java.scalatech.excel.impl;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CellItem<T> {
    private T value;
}
