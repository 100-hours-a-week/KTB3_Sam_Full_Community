package com.example.community.common;

import org.springframework.http.HttpStatus;

public enum SuccessCode {
    //유저
    USER_REGISTERED(HttpStatus.CREATED, "register_sucess"),
    USER_FOUND(HttpStatus.OK, "user_find_sucess"),
    USER_INFO_UPDATED(HttpStatus.NO_CONTENT, "user_info_update_sucess"),
    PASSWORD_UPDATED(HttpStatus.NO_CONTENT, "password_update_sucess"),
    USER_DELETED(HttpStatus.NO_CONTENT, "user_delete_success"),
    USER_LOGIN(HttpStatus.OK, "login_success"),
    USER_LOGOUT(HttpStatus.NO_CONTENT, "user_logout_success"),

    //게시글
    BOARD_CREATED(HttpStatus.CREATED, "board_create_success"),
    ALL_BOARDS_FOUND(HttpStatus.OK, "board_find_all_sucess"),
    BOARD_DETAIL_FOUND(HttpStatus.OK, "boards_detail_find_success"),
    BOARD_UPDATED(HttpStatus.NO_CONTENT, "board_update_success"),
    BOARD_DELETED(HttpStatus.NO_CONTENT, "board_delete_sucess"),

    //이미지
    IMAGE_UPLOADED(HttpStatus.CREATED, "image_upload_success"),
    IMAGE_FOUND(HttpStatus.OK, "image_find_success"),

    //댓글
    COMMENT_CREATED(HttpStatus.CREATED, "comment_upload_success"),
    ALL_COMMENTS_ON_BOARD_FOUND(HttpStatus.OK, "find_all_comments_on_board_success"),
    COMMENT_UPDATED(HttpStatus.NO_CONTENT, "comment_update_success"),
    COMMENT_DELETED(HttpStatus.NO_CONTENT, "comment_delete_sucess");

    private final HttpStatus status;
    private final String message;

    SuccessCode(HttpStatus status, String message) {
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
