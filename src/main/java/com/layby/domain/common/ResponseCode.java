package com.layby.domain.common;

public interface ResponseCode {
    String SUCCESS = "SU";

    String VALIDATION_FAIL = "VF";
    String DUPLICATED_USERNAME = "DI";

    String SIGN_IN_FAIL = "SF";
    String CERTIFICATION_FAIL = "CF";

    String MAIL_FAIL = "MF";
    String DATABASE_ERROR = "DBE";
    String INTERNAL_SERVER_ERROR = "ISE";
}
