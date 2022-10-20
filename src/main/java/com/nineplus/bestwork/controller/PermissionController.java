package com.nineplus.bestwork.controller;
import com.nineplus.bestwork.dto.PageResponseDto;
import com.nineplus.bestwork.dto.ResPermissionDto;
import com.nineplus.bestwork.dto.SearchDto;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.services.PermissionService;
import com.nineplus.bestwork.utils.CommonConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@PropertySource("classpath:application.properties")
@RequestMapping(value = "api/v1/permission")
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

    @PostMapping
    public ResponseEntity<? extends Object> addPermission(@RequestBody ResPermissionDto dto) {
        try {
            permissionService.addPermission(dto);
        } catch (BestWorkBussinessException ex) {
            return failed(ex.getMsgCode(), ex.getParam());
        }
        return success(CommonConstants.MessageCode.CPN0001, null, null);
    }

    @PutMapping
    public ResponseEntity<? extends Object> updatePermission(@RequestBody ResPermissionDto dto) {
        ResPermissionDto resPermissionDto;
        try {
            resPermissionDto = permissionService.updatePermission(dto);
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
