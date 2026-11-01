package com.study.commerce_lab.adapter.in.webapi.exception;

public record ValidationError(
    String field,
    String message
) {
}
