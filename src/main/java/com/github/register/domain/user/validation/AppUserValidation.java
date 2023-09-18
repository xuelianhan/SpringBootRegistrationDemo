package com.github.register.domain.user.validation;

import com.github.register.domain.auth.AuthenticAppUser;
import com.github.register.domain.user.AppUser;
import com.github.register.domain.user.AppUserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.function.Predicate;

/**
 * @author sniper
 * @date 18 Sep 2023
 */
public class AppUserValidation <T extends Annotation> implements ConstraintValidator<T, AppUser> {

    protected Predicate<AppUser> predicate = c -> true;

    @Autowired
    protected AppUserRepository appUserRepository;

    @Override
    public boolean isValid(AppUser value, ConstraintValidatorContext context) {
        return appUserRepository == null || predicate.test(value);
    }

    @Override
    public void initialize(T constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    public static class ExistsAppUserValidator extends AppUserValidation<ExistsAppUser> {
        public void initialize(ExistsAppUser constraintAnnotation) {
            predicate = c -> appUserRepository.existsById(c.getId());
        }
    }

    public static class AuthenticatedAppUserValidator extends AppUserValidation<AuthenticatedAppUser> {
        public void initialize(AuthenticatedAppUser constraintAnnotation) {
            predicate = c -> {
                Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                if ("anonymousUser".equals(principal)) {
                    return false;
                } else {
                    AuthenticAppUser loginUser = (AuthenticAppUser) principal;
                    return c.getId().equals(loginUser.getId());
                }
            };
        }
    }

    public static class UniqueAppUserValidator extends AppUserValidation<UniqueAppUser> {
        public void initialize(UniqueAppUser constraintAnnotation) {
            predicate = c -> !appUserRepository.existsByUsernameOrEmail(c.getUsername(), c.getEmail());
        }
    }

    public static class NotConflictAppUserValidator extends AppUserValidation<NotConflictAppUser> {
        public void initialize(NotConflictAppUser constraintAnnotation) {
            predicate = c -> {
                Collection<AppUser> collection = appUserRepository.findByUsernameOrEmail(c.getUsername(), c.getEmail());
                // Changing a username, email to something that doesn't duplicate the existing one at all,
                // or only duplicates itself,
                // is not a conflict
                return collection.isEmpty() || (collection.size() == 1 && collection.iterator().next().getId().equals(c.getId()));
            };
        }
    }


}
