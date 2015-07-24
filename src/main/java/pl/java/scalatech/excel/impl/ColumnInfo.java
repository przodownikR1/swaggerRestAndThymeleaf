package pl.java.scalatech.excel.impl;

import java.util.Optional;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ColumnInfo<T> {
    private final @NonNull Optional<String> pattern;
    private final @NonNull Class<T> type;
}