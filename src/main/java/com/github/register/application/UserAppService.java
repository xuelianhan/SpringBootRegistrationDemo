package com.github.register.application;

import com.github.register.domain.payload.request.UserInfoRequest;
import com.github.register.domain.user.AppUser;
import com.github.register.domain.user.AppUserRepository;
import com.github.register.domain.user.DeletedStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author sniper
 * @date 18 Sep 2023
 */
@Service
public class UserAppService {

    @Autowired
    AppUserRepository appUserRepository;

    public List<AppUser> findValidUsers() {
        return appUserRepository.findByDeletedEquals(0);
    }

    public List<AppUser> findAllUsers() {
        return appUserRepository.findAll();
    }

    public AppUser edit(Integer id, UserInfoRequest userInfoRequest) {
        Optional<AppUser> op = appUserRepository.findById(id);
        AppUser cur = null;
        if (op.isPresent()) {
            cur = op.get();
            updateUserFromRequest(cur, userInfoRequest);
            appUserRepository.save(cur);
        }
        return cur;
    }

    private void updateUserFromRequest(AppUser user, UserInfoRequest userInfoRequest) {
        user.setDeleted(userInfoRequest.getDeleted());
        user.setUsername(userInfoRequest.getUsername());
        user.setEmail(userInfoRequest.getEmail());
    }

    public void markAccountDeletedById(Integer id) {
        Optional<AppUser> op = appUserRepository.findById(id);
        if (op.isPresent()) {
            AppUser appUser = op.get();
            appUser.setDeleted(DeletedStatusEnum.DELETED.getCode());
            appUserRepository.save(appUser);
        }
    }

    public void markAccountDeletedByIds(String ids) {
        List<Integer> idList = Arrays.stream(ids.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        if (null == idList || idList.isEmpty()) {
            return;
        }
        List<AppUser> list = appUserRepository.findByIdIn(new HashSet<>(idList));
        if (null == list || list.isEmpty()) {
            return;
        }
        list.forEach(a -> {
            a.setDeleted(DeletedStatusEnum.DELETED.getCode());
        });
        appUserRepository.saveAll(list);
    }

}
