package com.github.register.resource;

import com.github.register.application.UserApplicationService;
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
    UserApplicationService userApplicationService;


    /**
     * e.g. POST http://127.0.0.1:8080/api/v1/auth/login
     * negative case:
     * {
     *     "username":"evilsniper",
     *     "password":"123457"
     * }
     * ----------------------------------------------------
     * positive case:
     * {
     *     "username":"evilsniper",
     *     "password":"123456"
     * }
     *
     * {
     *     "id": 2,
     *     "username": "evilsniper",
     *     "email": "evilsniper@sina.cn",
     *     "roles": [
     *         "ROLE_USER"
     *     ]
     * }
     *
     * Test is OK.
     *
     * @param loginRequest
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return userApplicationService.authenticateUser(loginRequest);
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
        return userApplicationService.registerUser(registerRequest);
    }

    /**
     * The cookie is cleared while logout happens.
     * e.g. POST http://127.0.0.1:8080/api/v1/auth/logout
     * Test is OK.
     *
     * @return
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        return userApplicationService.logout();
    }
}
