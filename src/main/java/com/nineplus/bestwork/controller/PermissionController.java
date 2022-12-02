package com.nineplus.bestwork.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nineplus.bestwork.dto.PermissionResDto;
import com.nineplus.bestwork.dto.RegPermissionDto;
import com.nineplus.bestwork.dto.ResRoleDto;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.model.enumtype.Status;
import com.nineplus.bestwork.services.PermissionService;
import com.nineplus.bestwork.services.RoleService;
import com.nineplus.bestwork.utils.CommonConstants;

@PropertySource("classpath:application.properties")
@RequestMapping(value = CommonConstants.ApiPath.BASE_PATH + "/permission")
@RestController
public class PermissionController extends BaseController {

	@Autowired
	PermissionService permissionService;

	@Autowired
	RoleService roleService;

	@PostMapping
	public ResponseEntity<? extends Object> updatePermission(@RequestBody List<RegPermissionDto> dto) {
		List<PermissionResDto> resPermissionDto;
		try {
			resPermissionDto = permissionService.updatePermissions(dto);
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		return success(CommonConstants.MessageCode.RLS0003, resPermissionDto, null);
	}

	@GetMapping("/{id}")
	public ResponseEntity<? extends Object> getPermissions(@PathVariable Long id) throws BestWorkBussinessException {
		ResRoleDto role = roleService.getRole(id);
		List<PermissionResDto> mapResponse = new ArrayList<>();
		List<String> roleList = new ArrayList<>();
		roleList.add(role.getName());
		List<Integer> lstStt = new ArrayList<>();
		lstStt.add(Status.ACTIVE.getValue());
		try {
			mapResponse = permissionService.getMapPermissions(roleList, lstStt).values().stream().flatMap(List::stream)
					.collect(Collectors.toList());
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		return success(CommonConstants.MessageCode.PMS0001, mapResponse, null);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<? extends Object> deletePermission(@PathVariable Long id) {
		try {
			permissionService.deletePermission(id);
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		return success(CommonConstants.MessageCode.RLS0004, id, null);
	}

}
