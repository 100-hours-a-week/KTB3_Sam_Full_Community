package com.example.community.controller;

import com.example.community.auth.JwtUtil;
import com.example.community.common.SuccessCode;
import com.example.community.dto.request.PasswordModifyRequest;
import com.example.community.dto.request.UserModifyRequest;
import com.example.community.dto.request.UserRegisterRequest;
import com.example.community.dto.response.APIResponse;
import com.example.community.dto.response.DuplicationCheckResponse;
import com.example.community.dto.response.UserInfoResponse;
import com.example.community.dto.response.UserRegisterResponse;
import com.example.community.entity.User;
import com.example.community.facade.UserImageQueryFacade;
import com.example.community.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "유저 API", description = "유저 정보 관련 API")
@RestController
public class UserController {
    private final UserService userService;
    private final UserImageQueryFacade userImageQueryFacade;
    private final JwtUtil jwtUtil;

    UserController(UserService userService, UserImageQueryFacade userImageQueryFacade, JwtUtil jwtUtil) {
        this.userService = userService;
        this.userImageQueryFacade = userImageQueryFacade;
        this.jwtUtil = jwtUtil;
    }

    @Operation(summary = "회원가입", description = "사용자의 정보를 받아 회원가입을 진행합니다.")
    @PostMapping("/users")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "register_success"),
            @ApiResponse(responseCode = "400", description = "invalid_request"),
            @ApiResponse(responseCode = "500", description = "internal_server_error")
    })
    public ResponseEntity<APIResponse<UserRegisterResponse>> registerUser(@Valid @RequestBody UserRegisterRequest request) {
        User user = userService.registerUser(request.email(), request.password(), request.nickname(), request.profileImageId());
        return ResponseEntity.ok(APIResponse.success(SuccessCode.USER_REGISTERED, UserRegisterResponse.from(user)));
    }

    @Operation(summary = "회원 상세 정보 조회", description = "로그인된 회원의 상세 정보를 조회합니다.")
    @GetMapping("/users")
    @SecurityRequirement(name = "JWT")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "user_find_success"),
            @ApiResponse(responseCode = "400", description = "invalid_request"),
            @ApiResponse(responseCode = "500", description = "internal_server_error")
    })
    public ResponseEntity<APIResponse<UserInfoResponse>> getUser(HttpServletRequest servletRequest) {
        Long userId = jwtUtil.extractUserId((String) servletRequest.getAttribute("accessToken"));
        
        UserInfoResponse userInfoResponse = userImageQueryFacade.getUser(userId);

        return ResponseEntity.ok(APIResponse.success(SuccessCode.USER_FOUND,userInfoResponse));
    }

    @Operation(summary = "회원 정보 수정", description = "로그인된 회원의 정보를 수정합니다.")
    @PatchMapping("/users")
    @SecurityRequirement(name = "JWT")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "no_content"),
            @ApiResponse(responseCode = "400", description = "invalid_request"),
            @ApiResponse(responseCode = "401", description = "not_login_user"),
            @ApiResponse(responseCode = "500", description = "internal_server_error")
    })
    public ResponseEntity<APIResponse<Void>> modifyUser(HttpServletRequest servletRequest, @Valid @RequestBody UserModifyRequest request) {
        Long userId = jwtUtil.extractUserId((String) servletRequest.getAttribute("accessToken"));
        userService.modifyUser(userId, request.nickname(), request.profileImageId());
        return ResponseEntity.ok(APIResponse.success(SuccessCode.USER_INFO_UPDATED,null ));
    }

    @Operation(summary = "비밀번호 변경", description = "사용자의 비밀번호를 변경합니다.")
    @PatchMapping("/users/password")
    @SecurityRequirement(name = "JWT")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "no_content"),
            @ApiResponse(responseCode = "400", description = "invalid_request"),
            @ApiResponse(responseCode = "401", description = "not_login_user"),
            @ApiResponse(responseCode = "500", description = "internal_server_error")
    })
    public ResponseEntity<APIResponse<Void>> updatePassword(HttpServletRequest servletRequest, @Valid @RequestBody PasswordModifyRequest request) {
        Long userId = jwtUtil.extractUserId((String) servletRequest.getAttribute("accessToken"));
        userService.changePassword(userId, request.password(), request.checkPassword());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "회원 탈퇴", description = "로그인된 유저의 회원 탈퇴를 진행합니다.")
    @DeleteMapping("/users")
    @SecurityRequirement(name = "JWT")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "no_content"),
            @ApiResponse(responseCode = "404", description = "already_deleted_user"),
            @ApiResponse(responseCode = "500", description = "internal_server_error")
    })
    public ResponseEntity<APIResponse<Void>> deleteUser(HttpServletRequest servletRequest) {
        Long userId = jwtUtil.extractUserId((String) servletRequest.getAttribute("accessToken"));
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "이메일 중복 확인", description = "이메일의 중복 여부를 확인합니다.")
    @GetMapping("/users/email")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "email_duplication_checked"),
            @ApiResponse(responseCode = "500", description = "internal_server_error")
    })
    public ResponseEntity<APIResponse<DuplicationCheckResponse>> checkEmailDuplicated (@RequestParam("email") String email) {
        Boolean isEmailExists = userService.checkEmailDuplicated(email);
        return ResponseEntity.ok(APIResponse.success(SuccessCode.EMAIL_DUPLICATION_CHECKED, DuplicationCheckResponse.from(isEmailExists)));
    }

    @Operation(summary = "닉네임 중복 확인", description = "닉네임의 중복 여부를 확인합니다.")
    @GetMapping("/users/nickname")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "nickname_duplication_checked"),
            @ApiResponse(responseCode = "500", description = "internal_server_error")
    })
    public ResponseEntity<APIResponse<DuplicationCheckResponse>> checkNicknameDuplicated (@RequestParam("nickname") String nickname) {
        Boolean isNicknameExists = userService.checkNicknameDuplicated(nickname);
        return ResponseEntity.ok(APIResponse.success(SuccessCode.NICKNAME_DUPLICATION_CHECKED, DuplicationCheckResponse.from(isNicknameExists)));
    }
}
