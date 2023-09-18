package com.github.register.resource;

import com.github.register.application.EmailService;
import com.github.register.domain.mail.Email;
import com.github.register.domain.payload.response.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author sniper
 * @date 17 Sep 2023
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/test")
public class TestController {

    @Autowired
    private EmailService emailService;

    /**
     * e.g. GET http://127.0.0.1:8080/api/v1/test/all
     *
     * Test is OK.
     *
     * @return
     */
    @GetMapping("/all")
    public String allAccess() {
        // test content for all.
        return "Public Content.";
    }

    /**
     * Step-1: e.g. GET http://127.0.0.1:8080/api/v1/test/user
     *
     * {
     *     "path": "/api/v1/test/user",
     *     "error": "Unauthorized",
     *     "message": "Full authentication is required to access this resource",
     *     "status": 401
     * }
     *
     * Step-2: e.g. GET http://127.0.0.1:8080/api/v1/auth/login
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
     * The above step has set jwt-cookie at the client, so we can access the following resource
     * Step-3: e.g. GET http://127.0.0.1:8080/api/v1/test/user
     *
     * Test is OK.
     *
     * @return
     */
    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public String userAccess() {
        // test content for user who has logged in.
        return "User Content.";
    }

    @GetMapping("/mod")
    @PreAuthorize("hasRole('MODERATOR')")
    public String moderatorAccess() {
        // test content for moderator user.
        return "Moderator Board.";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        // test content for admin user.
        return "Admin Board.";
    }

    /**
     * e.g. GET http://127.0.0.1:8080/api/v1/test/sendMail
     * @return
     */
    @GetMapping("/sendMail")
    public ResponseEntity<?> sendMail() {
        try {
            String to = "to_a@gmail.com";
            String content = "test test test";
            Email email = new Email();

            emailService.sendSimpleMessage(email);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email sent failed!"));
        }

        return ResponseEntity.ok(new MessageResponse("Email has sent successfully!"));
    }

}
