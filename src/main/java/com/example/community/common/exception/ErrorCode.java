package com.example.community.common.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    // 공통
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "internal_server_error"),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "invalid_request"),

    // 인증 관련
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "not_login_user"),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "token_expired"),
    TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "invalid_token"),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "invalid_password"),

    //유저
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "not_found_user"),
    NOT_LOGIN_USER(HttpStatus.UNAUTHORIZED, "not_login_user"),
    ALREADY_DELETED_USER(HttpStatus.NOT_FOUND, "already_deleted_user"),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "duplicate_email"),
    ALREADY_REGISTERED_EMAIL(HttpStatus.BAD_REQUEST, "already_exist_email"),
    ALREADY_REGISTERED_NICKNAME(HttpStatus.BAD_REQUEST, "already_exist_nickname"),
    NOT_FOUND_USER_IMAGE(HttpStatus.NOT_FOUND, "not_found_user_image"),

    //게시글
    NOT_FOUND_BOARD(HttpStatus.NOT_FOUND, "not_found_board"),
    ALREAD_DELETED_BOARD(HttpStatus.NOT_FOUND, "already_deleted_board"),
    DUPLICATE_TITLE(HttpStatus.BAD_REQUEST, "duplicate_title"),

    //이미지
    NOT_FOUND_IMAGE(HttpStatus.NOT_FOUND, "not_found_image"),

    //댓글
    NOT_FOUND_COMMENT(HttpStatus.NOT_FOUND, "not_found_comment"),
    ALREAD_DELETE_COMMENT(HttpStatus.NOT_FOUND,"already_deleted_comment"),

    //좋아요
    ALREADY_LIKED_POST(HttpStatus.BAD_REQUEST, "already_liked_post"),
    NOT_FOUND_LIKE(HttpStatus.NOT_FOUND, "not_found_like");


    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
