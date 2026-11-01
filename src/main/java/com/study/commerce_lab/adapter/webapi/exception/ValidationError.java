package com.study.commerce_lab.adapter.webapi.exception;

public record ValidationError(
    String feild,
    String message
) {
}
