package com.study.commerce_lab.adapter.in.webapi.exception;

import com.study.commerce_lab.domain.product.exception.StockQuantityExceededException;
import com.study.commerce_lab.domain.product.exception.DuplicateSkuException;
import com.study.commerce_lab.domain.product.exception.ProductNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.List;
import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ProblemDetail handleProductNotFound(
            ProductNotFoundException e,
            HttpServletRequest request
            ) {
        return createProblemDetail(
                HttpStatus.NOT_FOUND,
                "Product Not Found",
                e.getMessage(),
                "product-not-found",
                "PRODUCT_NOT_FOUND",
                request
        );
    }

    @ExceptionHandler(DuplicateSkuException.class)
    public ProblemDetail handleDuplicateSku(
            DuplicateSkuException e,
            HttpServletRequest request
    ){
        return createProblemDetail(
                HttpStatus.CONFLICT,
                "Duplicate SKU",
                e.getMessage(),
                "duplicate-sku",
                "DUPLICATE_SKU",
                request
        );
    }

    @ExceptionHandler(StockQuantityExceededException.class)
    public ProblemDetail handleExeedStockQuantity(
            StockQuantityExceededException e,
            HttpServletRequest request
    ){
        return createProblemDetail(
                HttpStatus.CONFLICT,
                "Exceed Stock Quantity",
                e.getMessage(),
                "exeed-stock-quantity",
                "EXCEED_STOCK_QUANTITY",
                request
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleInvalidRequest(
            MethodArgumentNotValidException e,
            HttpServletRequest request
    ) {
        ProblemDetail problemDetail = createProblemDetail(
                HttpStatus.BAD_REQUEST,
                "Invalid Input",
                "요청 값이 올바르지 않습니다.",
                "invalid-input",
                "INVALID_INPUT",
                request
        );

        List<ValidationError> errors = e.getBindingResult()
                .getFieldErrors().stream()
                .map(error -> new ValidationError(error.getField(),
                        Objects.requireNonNullElse(error.getDefaultMessage(),
                                "잘못된 입력입니다.")))
                .toList();

        problemDetail.setProperty("errors", errors);

        return problemDetail;
    }

    private ProblemDetail createProblemDetail(
            HttpStatus status,
            String title,
            String detail,
            String type,
            String errorCode,
            HttpServletRequest request
    ){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);

        problemDetail.setTitle(title);
        problemDetail.setType(URI.create("https://commerce-lab.dev/errors/" + type));

        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setProperty("errorCode", errorCode);

        return problemDetail;
    }
}
