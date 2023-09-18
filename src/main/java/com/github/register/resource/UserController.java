package com.github.register.resource;

import com.github.register.domain.user.AppUser;
import com.github.register.domain.user.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author sniper
 * @date 17 Sep 2023
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    AppUserRepository appUserRepository;

    /**
     * http://127.0.0.1:8080/api/v1/users
     * @return
     */
    @GetMapping
    public List<AppUser> getUserList() {
        return appUserRepository.findAll();
    }

}
