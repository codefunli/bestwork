package com.nineplus.bestwork.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nineplus.bestwork.dto.PageResDto;
import com.nineplus.bestwork.dto.MonitorResDto;
import com.nineplus.bestwork.dto.SearchDto;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.services.MonitorService;
import com.nineplus.bestwork.utils.CommonConstants;

@PropertySource("classpath:application.properties")
@RequestMapping(value = CommonConstants.ApiPath.BASE_PATH+"/monitor")
@RestController
public class MonitorController extends BaseController {
    
    @Autowired
    MonitorService monitorService;

    @GetMapping("/{id}")
    public ResponseEntity<? extends Object> getMonitor(@PathVariable Long id) {
        MonitorResDto dto = null;
        try {
            dto = monitorService.getMonitor(id);
        } catch (BestWorkBussinessException ex) {
            return failed(ex.getMsgCode(), ex.getParam());
        }
        return success(CommonConstants.MessageCode.RLS0001, dto, null);

    }

    @PostMapping
    public ResponseEntity<? extends Object> addMonitor(@RequestBody MonitorResDto dto) {
        try {
            monitorService.addMonitor(dto);
        } catch (BestWorkBussinessException ex) {
            return failed(ex.getMsgCode(), ex.getParam());
        }
        return success(CommonConstants.MessageCode.SCM001, null, null);
    }

    @PutMapping
    public ResponseEntity<? extends Object> updateMonitor(@RequestBody MonitorResDto dto) {
        MonitorResDto resMonitorDto;
        try {
            resMonitorDto = monitorService.updateMonitor(dto);
        } catch (BestWorkBussinessException ex) {
            return failed(ex.getMsgCode(), ex.getParam());
        }
        return success(CommonConstants.MessageCode.RLS0003, resMonitorDto, null);
    }

    @PostMapping("/search")
    public ResponseEntity<? extends Object> getMonitors(@RequestBody SearchDto dto) {
        PageResDto<MonitorResDto> pageSearchDto = null;
        try {
            pageSearchDto = monitorService.getMonitors(dto);
        } catch (BestWorkBussinessException ex) {
            return failed(ex.getMsgCode(), ex.getParam());
        }
        return success(CommonConstants.MessageCode.RLS0001, pageSearchDto, null);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<? extends Object> deleteMonitor(@PathVariable Long id) {
        try {
            monitorService.deleteMonitor(id);
        } catch (BestWorkBussinessException ex) {
            return failed(ex.getMsgCode(), ex.getParam());
        }
        return success(CommonConstants.MessageCode.RLS0004, id, null);
    }

}
