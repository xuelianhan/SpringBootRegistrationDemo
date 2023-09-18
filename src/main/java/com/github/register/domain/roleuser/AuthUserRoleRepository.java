package com.github.register.domain.roleuser;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author sniper
 * @date 17 Sep 2023
 */
@Repository
public interface AuthUserRoleRepository extends CrudRepository<AuthUserRole, Long> {

}
