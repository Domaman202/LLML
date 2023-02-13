package ru.DmN.llml.utils;

public interface IASTPrintable {
    default StringBuilder toString(int offset) {
        return new StringBuilder("|").append("\t".repeat(offset));
    }
}
