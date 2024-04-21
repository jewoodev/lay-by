package com.layby.domain.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    VALIDATION_FAIL("401", "Validation failed."),

    DUPLICATED_USERNAME("409", "Duplicate Id."),

    SIGN_IN_FAIL("400", "Login information mismatch."),

    CERTIFICATION_FAIL("400", "Certification failed"),

    MAIL_FAIL("400", "Mail send failed."),

    DATABASE_ERROR("500", "Database error."),

    INTERNAL_SERVER_ERROR("500", "Server Error aroused. Please contact the server administrator"),

    NOT_ENOUGH_STOCK("409", "All stock has been sold out."),

    DELIVERY_ALEADY_START("409", "Cancellation is only possible before delivery begins.");


    private final String code;

    private final String message;
}
