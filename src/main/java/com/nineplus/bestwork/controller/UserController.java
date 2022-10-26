package com.nineplus.bestwork.controller;

import com.nineplus.bestwork.dto.*;
import com.nineplus.bestwork.entity.TRole;
import com.nineplus.bestwork.entity.TUser;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.model.UserAuthDetected;
import com.nineplus.bestwork.services.UserService;
import com.nineplus.bestwork.utils.CommonConstants;
import com.nineplus.bestwork.utils.TokenUtils;
import com.nineplus.bestwork.utils.UserAuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@PropertySource("classpath:application.properties")
@RequestMapping(value = "api/v1/users")
@RestController
public class UserController extends BaseController {

    @Autowired
    UserService userService;

    @Autowired
    TokenUtils tokenUtils;

    @Autowired
    UserAuthUtils userAuthUtils;

    @Value("${app.login.jwtPrefix}")
    private String PRE_STRING;

    @GetMapping("/isCheckLogin")
    public ResponseEntity<? extends Object> isCheckLogin(HttpServletRequest request, HttpServletResponse response) {
        return success(CommonConstants.MessageCode.S1X0010, null, null);
    }

    /**
     * Get list company
     *
     * @return list company
     */
    @PostMapping("/list")
    public ResponseEntity<?> getAllUsers(@Valid @RequestBody(required = false) PageSearchUserDto pageCondition, BindingResult bindingResult) {
        if (bindingResult.hasErrors() || null == pageCondition) return failed(CommonConstants.MessageCode.ECU0002, null);
        PageResponseDto<UserResDto> pageUser;
        try {
            pageUser = userService.getAllUsers(pageCondition);
        } catch (BestWorkBussinessException ex) {
            return failed(ex.getMsgCode(), ex.getParam());
        }
        return success(CommonConstants.MessageCode.sU0006, pageUser, null);
    }

    @PostMapping("/create")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserReqDto userReqDto,
                                          BindingResult bindingResult) throws BestWorkBussinessException {

        UserAuthDetected userAuthRoleReq = userAuthUtils.getUserInfoFromReq(false);

        if (userAuthRoleReq.getIsSysAdmin()) {
            return failed(CommonConstants.MessageCode.E1X0014, null);
        }
        if (checkExists(userReqDto, bindingResult, userAuthRoleReq)) {
            return failedWithError(CommonConstants.MessageCode.ECU0001, bindingResult.getFieldErrors().toArray(), null);
        }
        TUser createdUser;
        try {
            createdUser = userService.createUser(userReqDto);
        } catch (BestWorkBussinessException ex) {
            return failed(ex.getMsgCode(), ex.getParam());
        }
        return success(CommonConstants.MessageCode.SCU0001, createdUser, null);
    }

    private boolean checkExists(UserReqDto userReqDto, BindingResult bindingResult, UserAuthDetected userAuthRoleReq) {
        long companyId = this.userService.findCompanyIdByAdminUsername(userAuthRoleReq);
        List<TUser> existsUsers = this.userService.findAllUsersByCompanyId(companyId);
        for (TUser user : existsUsers) {
            if (user.getUserName().equals(userReqDto.getUserName())) {
                bindingResult.rejectValue("userName", "ExistedUsername", "Username already exists in the company.");
            } else if (user.getEmail().equals(userReqDto.getEmail())) {
                bindingResult.rejectValue("email", "ExistedEmail", "Email already exists in the company.");
            }
        }

        return bindingResult.hasErrors();
    }

	@GetMapping("/{userId}")
	public ResponseEntity<?> getUserById(@PathVariable long userId) throws BestWorkBussinessException {
		TUser user = userService.getUserById(userId);
		if (user == null) {
			return failed(CommonConstants.MessageCode.ECU0002, null);
		}
		UserResDto userResDto = new UserResDto();
		userResDto.setId(userId);
		userResDto.setUserName(user.getUserName());
		userResDto.setFirstName(user.getFirstNm());
		userResDto.setLastName(user.getLastNm());
		userResDto.setEmail(user.getEmail());
		userResDto.setTelNo(user.getTelNo());
		userResDto.setEnable(user.getIsEnable());
		userResDto.setRole(user.getRole());
		if (null != user.getUserAvatar()) {
			userResDto.setAvatar(Arrays.toString(user.getUserAvatar()));
		}
		userResDto.setUpdateDate(user.getUpdateDate().toString());

		return success(CommonConstants.MessageCode.SCU0002, userResDto, null);
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<?> updateUser(@PathVariable long id, @Valid @RequestBody UserReqDto userReqDto,
										BindingResult bindingResult) throws BestWorkBussinessException {
		if (bindingResult.hasErrors()) {
			return failedWithError(CommonConstants.MessageCode.ECU0001, bindingResult.getFieldErrors().toArray(), null);
		}
		TUser userEdit;
		try {
			userEdit = userService.editUser(userReqDto, id);
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		return success(CommonConstants.MessageCode.SCU0003, userEdit, null);
	}

    @PostMapping("/delete")
    public ResponseEntity<? extends Object> deleteUser(@RequestBody(required = false) UserListIdDto listId) {
        try {
            userService.deleteUser(listId);
        } catch (NullPointerException ex) {
            return failed(CommonConstants.MessageCode.SU0003, null);
        } catch (BestWorkBussinessException ex) {
            return failed(ex.getMsgCode(), ex.getParam());
        }
        return success(CommonConstants.MessageCode.SCU0004, null, null);
    }

    @GetMapping("/roles")
    public ResponseEntity<?> getRoles() {
        return ResponseEntity.ok(this.userService.getAllRoles());
    }

}
