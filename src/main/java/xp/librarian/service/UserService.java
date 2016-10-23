package xp.librarian.service;

import java.time.*;
import java.util.*;

import javax.validation.Valid;

import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.NonNull;
import xp.librarian.model.context.AccountContext;
import xp.librarian.model.context.ErrorCode;
import xp.librarian.model.dto.User;
import xp.librarian.model.form.UserLoginForm;
import xp.librarian.model.form.UserRegisterForm;
import xp.librarian.model.form.UserUpdateForm;
import xp.librarian.model.param.LoginParam;
import xp.librarian.model.result.UserProfileVM;
import xp.librarian.repository.UserDao;
import xp.librarian.model.context.BusinessException;
import xp.librarian.utils.UploadUtils;

/**
 * @author xp
 */
@Service
@Transactional
public class UserService {

    @Autowired
    private UserDao userDao;

    private UserProfileVM buildUserProfileVM(@NonNull User user) {
        return new UserProfileVM().withUser(user);
    }

    public UserProfileVM register(@Valid UserRegisterForm form) {
        User user = form.toDTO();
        user.setAvatarUrl(UploadUtils.upload(form.getAvatar()));
        user.setStatus(User.Status.NORMAL);
        user.setCreateTime(Date.from(Instant.now()));
        if (0 == userDao.add(user)) {
            throw new PersistenceException("user insert failed.");
        }
        return buildUserProfileVM(user);
    }

    public UserProfileVM login(@Valid UserLoginForm form) {
        User where = form.toDTO();
        User user = userDao.get(where);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_LOGIN_FAIL);
        }
        return buildUserProfileVM(user);
    }

    public void logout(@NonNull AccountContext account) {
        // need to do nothing now.
    }

    public UserProfileVM getProfile(@NonNull AccountContext account) {
        User user = userDao.get(account.getId());
        if (user == null) {
            throw new ResourceNotFoundException("user not found.");
        }
        return buildUserProfileVM(user);
    }

    public UserProfileVM setProfile(@NonNull AccountContext account, @Valid UserUpdateForm form) {
        User where = account.toDTO();
        User set = form.toDTO();
        set.setAvatarUrl(UploadUtils.upload(form.getAvatar()));
        if (0 == userDao.update(where, set)) {
            throw new PersistenceException("user update failed.");
        }
        return buildUserProfileVM(userDao.get(account.getId()));
    }

}
