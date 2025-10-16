package com.example.community.common.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    // 공통
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "internal_server_error"),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "invalid_request"),

    //유저
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "not_found_user"),
    NOT_LOGIN_USER(HttpStatus.UNAUTHORIZED, "not_login_user"),
    ALREADY_DELETED_USER(HttpStatus.NOT_FOUND, "already_deleted_user"),

    //게시글
    NOT_FOUND_BOARD(HttpStatus.NOT_FOUND, "not_found_board"),
    ALREAD_DELETED_BOARD(HttpStatus.NOT_FOUND, "already_deleted_board"),

    //이미지
    NOT_FOUND_IMAGE(HttpStatus.NOT_FOUND, "not_found_image"),

    //댓글
    NOT_FOUND_COMMENT(HttpStatus.NOT_FOUND, "not_found_comment"),
    ALREAD_DELETE_COMMENT(HttpStatus.NOT_FOUND,"already_deleted_comment");


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
