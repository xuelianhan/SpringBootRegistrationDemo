package com.github.register.resource;

import com.github.register.domain.auth.AuthenticAppUser;
import com.github.register.domain.auth.service.JWTAccessTokenService;
import com.github.register.domain.payload.request.LoginRequest;
import com.github.register.domain.payload.request.RegisterRequest;
import com.github.register.domain.payload.response.MessageResponse;
import com.github.register.domain.payload.response.UserInfoResponse;
import com.github.register.domain.role.AuthRole;
import com.github.register.domain.role.AuthRoleRepository;
import com.github.register.domain.role.RoleEnum;
import com.github.register.domain.user.AppUser;
import com.github.register.domain.user.AppUserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author sniper
 * @date 17 Sep 2023
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    AuthRoleRepository authRoleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JWTAccessTokenService jwtAccessTokenService;

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
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        AuthenticAppUser userDetails = (AuthenticAppUser) authentication.getPrincipal();
        ResponseCookie jwtCookie = jwtAccessTokenService.generateJwtCookie(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(new UserInfoResponse(userDetails.getId(),
                        userDetails.getUsername(),
                        userDetails.getEmail(),
                        roles));
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
        if (appUserRepository.existsByUsername(registerRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }
        if (appUserRepository.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        AppUser user = new AppUser(registerRequest.getUsername(),
                registerRequest.getEmail(),
                encoder.encode(registerRequest.getPassword()));

        Set<String> strRoles = registerRequest.getRole();
        Set<AuthRole> roles = new HashSet<>();

        AuthRole authRole = null;
        if (strRoles == null) {
            authRole = authRoleRepository.findByRoleName(RoleEnum.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(authRole);
        } else {
            for (String role : strRoles) {
                switch (role) {
                    case "ROLE_ADMIN":
                        authRole = authRoleRepository.findByRoleName(RoleEnum.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(authRole);
                        break;
                    case "ROLE_MODERATOR":
                        authRole = authRoleRepository.findByRoleName(RoleEnum.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(authRole);
                        break;
                    default:
                        authRole = authRoleRepository.findByRoleName(RoleEnum.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(authRole);
                }
            }
        }

        user.setRoles(roles);
        appUserRepository.save(user);

        // todo
        // send email here
        CompletableFuture.runAsync(() -> {

        });

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
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
        ResponseCookie cookie = jwtAccessTokenService.getCleanJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new MessageResponse("You've been signed out!"));
    }
}
