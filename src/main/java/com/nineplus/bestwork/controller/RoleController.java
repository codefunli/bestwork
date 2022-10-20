package com.nineplus.bestwork.controller;

import com.nineplus.bestwork.dto.PageResponseDto;
import com.nineplus.bestwork.dto.ResRoleDto;
import com.nineplus.bestwork.dto.SearchDto;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.services.RoleService;
import com.nineplus.bestwork.utils.CommonConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@PropertySource("classpath:application.properties")
@RequestMapping(value = "api/v1/role")
@RestController
public class RoleController extends BaseController {

    @Autowired
    RoleService roleService;

    @GetMapping("/{id}")
    public ResponseEntity<? extends Object> getRole(@PathVariable Long id) {
        ResRoleDto dto = null;
        try {
            dto = roleService.getRole(id);
        } catch (BestWorkBussinessException ex) {
            return failed(ex.getMsgCode(), ex.getParam());
        }
        return success(CommonConstants.MessageCode.RLS0001, dto, null);

    }

    @PostMapping
    public ResponseEntity<? extends Object> addRole(@RequestBody ResRoleDto dto) {
        try {
            roleService.addRole(dto);
        } catch (BestWorkBussinessException ex) {
            return failed(ex.getMsgCode(), ex.getParam());
        }
        return success(CommonConstants.MessageCode.CPN0001, null, null);
    }

    @PutMapping
    public ResponseEntity<? extends Object> updateRole(@RequestBody ResRoleDto dto) {
        ResRoleDto resRoleDto;
        try {
            resRoleDto = roleService.updateRole(dto);
        } catch (BestWorkBussinessException ex) {
            return failed(ex.getMsgCode(), ex.getParam());
        }
        return success(CommonConstants.MessageCode.RLS0003, resRoleDto, null);
    }

    @PostMapping("/search")
    public ResponseEntity<? extends Object> getRoles(@RequestBody SearchDto dto) {
        PageResponseDto<ResRoleDto> pageSearchDto = null;
        try {
            pageSearchDto = roleService.getRoles(dto);
        } catch (BestWorkBussinessException ex) {
            return failed(ex.getMsgCode(), ex.getParam());
        }
        return success(CommonConstants.MessageCode.RLS0001, pageSearchDto, null);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<? extends Object> deleteRole(@PathVariable Long id) {
        try {
            roleService.deleteRole(id);
        } catch (BestWorkBussinessException ex) {
            return failed(ex.getMsgCode(), ex.getParam());
        }
        return success(CommonConstants.MessageCode.RLS0004, id, null);
    }

}
