package com.layby.domain.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    VALIDATION_FAIL("VF", "Validation failed."),

    DUPLICATED_USERNAME("DI", "Duplicate Id."),

    SIGN_IN_FAIL("SF", "Login information mismatch."),

    CERTIFICATION_FAIL("CF", "Certification failed"),

    MAIL_FAIL("MF", "Mail send failed."),

    DATABASE_ERROR("DBE", "Database error."),

    INTERNAL_SERVER_ERROR("ISE", "Server Error aroused. Please contact the server administrator");


    private final String code;

    private final String message;
}
