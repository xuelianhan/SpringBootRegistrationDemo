package com.github.register.resource;

import com.github.register.application.UserAppService;
import com.github.register.domain.payload.request.UserInfoRequest;
import com.github.register.domain.user.AppUser;
import com.github.register.domain.user.validation.AuthenticatedAppUser;
import com.github.register.domain.user.validation.NotConflictAppUser;
import com.github.register.infrastructure.server.CommonResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
     *
     *
     * {
     *     "code": 0,
     *     "message": "ok",
     *     "data": [
     *         {
     *             "id": 3,
     *             "username": "JamesBond",
     *             "email": "JamesBond@sina.cn",
     *             "deleted": 0,
     *             "roles": [
     *                 {
     *                     "id": 1,
     *                     "roleName": "ROLE_USER",
     *                     "createTime": "2023-09-17T12:35:07.000+00:00",
     *                     "description": "user"
     *                 }
     *             ]
     *         }
     *     ]
     * }
     * @return
     */
    @GetMapping("/valid")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity getValidUsers() {
        return CommonResponse.run(
                () -> userAppService.findValidUsers()
        );
    }

    /**
     * e.g. PUT http://127.0.0.1:8080/api/v1/users/3
     *
     * positive case of request:
     * {
     *     "id":3,
     *     "username":"JamesBond",
     *     "email":"JamesBond@sina.cn",
     *     "deleted":0
     * }
     * @AuthenticatedAppUser @NotConflictAppUser
     *
     * @param userInfoRequest
     * @return
     */
    @PutMapping("{id}")
    public ResponseEntity editUser(@PathVariable("id") Integer id, @Validated @RequestBody UserInfoRequest userInfoRequest) {
        return CommonResponse.run(
                () -> userAppService.edit(id, userInfoRequest)
        );
    }

    /**
     * Marking an individual user as deleted
     * e.g. DELETE http://127.0.0.1:8080/api/v1/users/delete/3
     */
    @DeleteMapping("/delete/{id}")
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
