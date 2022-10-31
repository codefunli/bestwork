package com.nineplus.bestwork.controller;

import com.nineplus.bestwork.dto.PageResponseDto;
import com.nineplus.bestwork.dto.ResActionDto;
import com.nineplus.bestwork.dto.SearchDto;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.services.ActionService;
import com.nineplus.bestwork.utils.CommonConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@PropertySource("classpath:application.properties")
@RequestMapping(value = CommonConstants.ApiPath.BASE_PATH + "/action")
@RestController
public class ActionController extends BaseController {
    @Autowired
    ActionService actionService;

    @GetMapping("/{id}")
    public ResponseEntity<? extends Object> getMonitor(@PathVariable Long id) {
        ResActionDto dto = null;
        try {
            dto = actionService.getAction(id);
        } catch (BestWorkBussinessException ex) {
            return failed(ex.getMsgCode(), ex.getParam());
        }
        return success(CommonConstants.MessageCode.RLS0001, dto, null);
    }

    @PostMapping
    public ResponseEntity<? extends Object> addAction(@RequestBody ResActionDto dto) {
        try {
            actionService.addAction(dto);
        } catch (BestWorkBussinessException ex) {
            return failed(ex.getMsgCode(), ex.getParam());
        }
        return success(CommonConstants.MessageCode.CPN0001, null, null);
    }

    @PutMapping
    public ResponseEntity<? extends Object> updateAction(@RequestBody ResActionDto dto) {
        ResActionDto resActionDto;
        try {
            resActionDto = actionService.updateAction(dto);
        } catch (BestWorkBussinessException ex) {
            return failed(ex.getMsgCode(), ex.getParam());
        }
        return success(CommonConstants.MessageCode.RLS0003, resActionDto, null);
    }

    @PostMapping("/search")
    public ResponseEntity<? extends Object> getActions(@RequestBody SearchDto dto) {
        PageResponseDto<ResActionDto> pageSearchDto = null;
        try {
            pageSearchDto = actionService.getActions(dto);
        } catch (BestWorkBussinessException ex) {
            return failed(ex.getMsgCode(), ex.getParam());
        }
        return success(CommonConstants.MessageCode.RLS0001, pageSearchDto, null);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<? extends Object> deleteAction(@PathVariable Long id) {
        try {
            actionService.deleteAction(id);
        } catch (BestWorkBussinessException ex) {
            return failed(ex.getMsgCode(), ex.getParam());
        }
        return success(CommonConstants.MessageCode.RLS0004, id, null);
    }


}
