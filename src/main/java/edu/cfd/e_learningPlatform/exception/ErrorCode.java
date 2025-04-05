package edu.cfd.e_learningPlatform.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.Getter;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Uncategorized error", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1002, "User existed", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003, "Username must be at least 3 characters", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1004, "Password must be at least 3 characters", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "User not existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "You do not have permission", HttpStatus.FORBIDDEN),
    INVALID_DOB(1008, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),
    ROLE_NOT_FOUND(1009, "Role not found", HttpStatus.NOT_FOUND),
    INVALID_REQUEST(1010, "Request is not null", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(1011, "User not found", HttpStatus.NOT_FOUND),
    COURSE_NOT_FOUND(1012, "Course not found", HttpStatus.NOT_FOUND),
    WISHLIST_NOT_FOUND(1013, "Wishlist not found", HttpStatus.NOT_FOUND),
    CART_NOT_FOUND(1015,"Cart not found", HttpStatus.NOT_FOUND),
    USER_ROLE_NOT_FOUND(1016,"User role not found", HttpStatus.NOT_FOUND),
    INSTRUCTOR_NOT_FOUND(1017,"Instructor not found" ,HttpStatus.NOT_FOUND),
    WALLET_NOT_FOUND(1018, "Admin wallet not found",HttpStatus.NOT_FOUND ),
    WALLET_BALANCE_NOT_ENOUGH(1019,"Insufficient balance in admin wallet" ,HttpStatus.NOT_FOUND ),
    INVALID_WITHDRAW_AMOUNT(1020,"Withdraw amount must be greater than 0" ,HttpStatus.BAD_REQUEST ),
    EMAIL_SENDING_FAILED(1021,"Email sending failed" ,HttpStatus.INTERNAL_SERVER_ERROR ),
    WITHDRAW_REQUEST_NOT_FOUND(1022,"Withdraw request not found" ,HttpStatus.NOT_FOUND ),
    OTP_VERIFICATION_FAILED(1023,"OTP verification failed" ,HttpStatus.BAD_REQUEST ),
    WITHDRAW_NOT_FOUND(1024,"withdraw does not exist or timed out" ,HttpStatus.NOT_FOUND ),
    WITHDRAW_ALREADY_PROCESSED(1025, "Withdraw already processed", HttpStatus.BAD_REQUEST),
    TRANSACTION_NOT_FOUND(1026,"Transaction not found", HttpStatus.NOT_FOUND ),
    OTP_EXPIRED(1027,"OTP expired", HttpStatus.BAD_REQUEST ),
    CATEGORY_NOT_FOUND(1030,"Category not found" ,HttpStatus.NOT_FOUND ),
    ENROLLMENT_FALSE(1031,"Enrollment false" ,HttpStatus.NOT_FOUND ),
    RATING_USER_NOT_FOUND(1031,"Rating and user not found" ,HttpStatus.NOT_FOUND ),
    RATING_NOT_FOUND(1032,"Rating not found", HttpStatus.NOT_FOUND);

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
