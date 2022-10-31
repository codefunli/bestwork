package com.nineplus.bestwork.controller;
import java.util.List;

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

import com.nineplus.bestwork.dto.PageResponseDto;
import com.nineplus.bestwork.dto.RegPermissionDto;
import com.nineplus.bestwork.dto.ResPermissionDto;
import com.nineplus.bestwork.dto.SearchDto;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.services.PermissionService;
import com.nineplus.bestwork.utils.CommonConstants;

@PropertySource("classpath:application.properties")
@RequestMapping(value = CommonConstants.ApiPath.BASE_PATH+"/permission")
@RestController
public class PermissionController extends BaseController {

    @Autowired
    PermissionService permissionService;

    @GetMapping("/{id}")
    public ResponseEntity<? extends Object> getPermission(@PathVariable Long id) {
        ResPermissionDto dto = null;
        try {
            dto = permissionService.getPermission(id);
        } catch (BestWorkBussinessException ex) {
            return failed(ex.getMsgCode(), ex.getParam());
        }
        return success(CommonConstants.MessageCode.RLS0001, dto, null);

    }

//    @PostMapping
//    public ResponseEntity<? extends Object> addPermission(@RequestBody ResPermissionDto dto) {
//        try {
//            permissionService.addPermission(dto);
//        } catch (BestWorkBussinessException ex) {
//            return failed(ex.getMsgCode(), ex.getParam());
//        }
//        return success(CommonConstants.MessageCode.CPN0001, null, null);
//    }

    @PostMapping
    public ResponseEntity<? extends Object> updatePermission(@RequestBody RegPermissionDto dto) {
        List<ResPermissionDto> resPermissionDto;
        try {
            resPermissionDto = permissionService.updatePermissions(dto);
        } catch (BestWorkBussinessException ex) {
            return failed(ex.getMsgCode(), ex.getParam());
        }
        return success(CommonConstants.MessageCode.RLS0003, resPermissionDto, null);
    }

    @PostMapping("/search")
    public ResponseEntity<? extends Object> getPermissions(@RequestBody SearchDto dto) {
        PageResponseDto<ResPermissionDto> pageSearchDto = null;
        try {
            pageSearchDto = permissionService.getPermissions(dto);
        } catch (BestWorkBussinessException ex) {
            return failed(ex.getMsgCode(), ex.getParam());
        }
        return success(CommonConstants.MessageCode.RLS0001, pageSearchDto, null);
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
