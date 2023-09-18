package com.github.register.domain.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author sniper
 * @date 17 Sep 2023
 */
@Repository
public interface AppUserRepository extends CrudRepository<AppUser, Integer> {

    @Override
    List<AppUser> findAll();

    List<AppUser> findByIdIn(Collection<Integer> ids);

    List<AppUser> findByDeletedEquals(Integer deleted);

    List<AppUser> findByDeletedIs(Integer deleted);

    Optional<AppUser> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);


}
