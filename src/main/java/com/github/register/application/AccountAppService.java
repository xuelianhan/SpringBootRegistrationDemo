package com.github.register.application;

import com.github.register.domain.auth.AuthenticAppUser;
import com.github.register.infrastructure.server.JWTAccessTokenService;
import com.github.register.domain.payload.request.LoginRequest;
import com.github.register.domain.payload.request.RegisterRequest;
import com.github.register.domain.payload.response.CodeMessage;
import com.github.register.domain.payload.response.UserInfoResponse;
import com.github.register.domain.role.AuthRole;
import com.github.register.domain.role.AuthRoleRepository;
import com.github.register.domain.role.RoleEnum;
import com.github.register.domain.user.AppUser;
import com.github.register.domain.user.AppUserRepository;
import com.github.register.infrastructure.server.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author sniper
 * @date 18 Sep 2023
 */
@Service
public class AccountAppService {

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

    public ResponseEntity<?> authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        AuthenticAppUser userDetails = (AuthenticAppUser) authentication.getPrincipal();
        ResponseCookie jwtCookie = jwtAccessTokenService.generateJwtCookie(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        UserInfoResponse body = new UserInfoResponse(userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles);

        return CommonResponse.success(HttpHeaders.SET_COOKIE, jwtCookie.toString(), body);
    }


    public ResponseEntity<?> registerUser(RegisterRequest registerRequest) {
        if (appUserRepository.existsByUsername(registerRequest.getUsername())) {
            return CommonResponse.badRequest("Error: Username is already taken!");
        }
        if (appUserRepository.existsByEmail(registerRequest.getEmail())) {
            return CommonResponse.badRequest("Error: Email is already in use!");
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

        CompletableFuture.runAsync(() -> {
            //send mail here
        });

        return CommonResponse.success("User registered successfully!");
    }

    public ResponseEntity<?> logout() {
        ResponseCookie cookie = jwtAccessTokenService.getCleanJwtCookie();
        CodeMessage body = new CodeMessage(0, "You've been logged out!");
        return CommonResponse.success(HttpHeaders.SET_COOKIE, cookie.toString(), body);
    }
}
