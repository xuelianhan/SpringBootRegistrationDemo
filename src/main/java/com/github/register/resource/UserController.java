package com.github.register.resource;

import com.github.register.application.UserAppService;
import com.github.register.domain.user.AppUser;
import com.github.register.infrastructure.server.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    UserAppService userAppService;

    /**
     * e.g. GET http://127.0.0.1:8080/api/v1/users/valid
     * @return
     */
    @GetMapping("valid")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity getValidUsers() {
        return CommonResponse.run(
                () -> userAppService.findValidUsers()
        );
    }

    /**
     * Marking an individual user as deleted
     * e.g. DELETE http://127.0.0.1:8080/api/v1/users/delete/3
     */

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity markUserDeleted(@PathVariable("id")Integer deletedUserId) {
        return CommonResponse.op(() -> userAppService.markAccountDeletedById(deletedUserId));
    }

    /**
     * Mark multiple users as delete
     * e.g. DELETE http://127.0.0.1:8080/api/v1/users/deleteMore?ids=2,3
     */
    @DeleteMapping("/deleteMore")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity markUsersDeleted(@RequestParam("ids") String ids) {
        return CommonResponse.op(() -> userAppService.markAccountDeletedByIds(ids));
    }

}
