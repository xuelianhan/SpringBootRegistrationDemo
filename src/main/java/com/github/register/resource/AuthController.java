package com.github.register.resource;

import com.github.register.application.AccountAppService;
import com.github.register.domain.payload.request.LoginRequest;
import com.github.register.domain.payload.request.RegisterRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author sniper
 * @date 17 Sep 2023
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    AccountAppService accountAppService;


    /**
     * e.g. POST http://127.0.0.1:8080/api/v1/auth/login
     *
     * negative case of request:
     * {
     *     "username":"evilsniper",
     *     "password":"123457"
     * }
     * ----------------------------------------------------
     * positive case of request:
     * {
     *     "username":"evilsniper",
     *     "password":"123456"
     * }
     *
     * positive case of response:
     * {
     *     "code": 0,
     *     "message": "ok",
     *     "data": {
     *         "id": 4,
     *         "username": "elonmusk",
     *         "email": "elonmusk@tesla.com",
     *         "roles": [
     *             "ROLE_ADMIN"
     *         ]
     *     }
     * }
     *
     * Test is OK.
     *
     * @param loginRequest
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return accountAppService.authenticateUser(loginRequest);
    }

    /**
     * e.g. POST http://127.0.0.1:8080/api/v1/auth/register
     * {
     *     "username":"evilsniper",
     *     "email":"evilsniper@sina.cn",
     *     "password":"123456",
     *     "role":["admin"]
     * }
     *
     * Test is OK.
     *
     * @param registerRequest
     * @return
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        return accountAppService.registerUser(registerRequest);
    }

    /**
     * The cookie is cleared while logout happens.
     * e.g. POST http://127.0.0.1:8080/api/v1/auth/logout
     * Test is OK.
     *
     * positive case of response:
     * {
     *     "code": 0,
     *     "message": "ok",
     *     "data": {
     *         "code": 0,
     *         "message": "You've been logged out!",
     *         "data": null
     *     }
     * }
     *
     * @return
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        return accountAppService.logout();
    }
}
