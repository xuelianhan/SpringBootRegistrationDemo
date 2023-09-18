package com.github.register.domain.role;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author sniper
 * @date 17 Sep 2023
 */
@Repository
public interface AuthRoleRepository extends CrudRepository<AuthRole, Long> {

    Optional<AuthRole> findByRoleName(RoleEnum roleName);
}
