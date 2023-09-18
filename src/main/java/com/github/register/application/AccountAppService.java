package com.github.register.application;

import com.github.register.domain.auth.AuthenticAppUser;
import com.github.register.domain.event.UserRegistrationEvent;
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
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
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


    @Autowired
    ApplicationEventPublisher eventPublisher;

    public ResponseEntity<?> authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        AuthenticAppUser userDetails = (AuthenticAppUser) authentication.getPrincipal();
        ResponseCookie jwtCookie = jwtAccessTokenService.generateJwtCookie(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        UserInfoResponse userInfo = new UserInfoResponse.Builder()
                .id(userDetails.getId())
                .username(userDetails.getUsername())
                .email(userDetails.getEmail())
                .roles(roles)
                .build();

        return CommonResponse.success(HttpHeaders.SET_COOKIE, jwtCookie.toString(), userInfo);
    }



    @Transactional
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

        if (strRoles == null || strRoles.isEmpty()) {
            roles.add(findRole(RoleEnum.ROLE_USER));
        } else {
            for (String role : strRoles) {
                roles.add(findRole(RoleEnum.getRoleEnum(role)));
            }
        }

        user.setRoles(roles);
        appUserRepository.save(user);

        // sending event of welcome-email after the successful completion of registration transaction.
        eventPublisher.publishEvent(new UserRegistrationEvent(this, user.getUsername(), user.getEmail()));

        return CommonResponse.success("User registered successfully!");
    }

    private AuthRole findRole(RoleEnum roleEnum) {
        return authRoleRepository.findByRoleName(roleEnum)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
    }

    public ResponseEntity<?> logout() {
        ResponseCookie cookie = jwtAccessTokenService.getCleanJwtCookie();
        CodeMessage body = new CodeMessage(0, "You've been logged out!");
        return CommonResponse.success(HttpHeaders.SET_COOKIE, cookie.toString(), body);
    }
}
