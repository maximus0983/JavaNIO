package com.epam.task2;

import java.util.List;

@FunctionalInterface
public interface FileCalculator<T> {
    List<T> get(T t, List<T> list);
}
