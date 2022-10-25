package com.nineplus.bestwork.controller;
import com.nineplus.bestwork.dto.PageResponseDto;
import com.nineplus.bestwork.dto.ResMonitorDto;
import com.nineplus.bestwork.dto.SearchDto;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.services.MonitorService;
import com.nineplus.bestwork.utils.CommonConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@PropertySource("classpath:application.properties")
@RequestMapping(value = "api/v1/monitor")
@RestController
public class MonitorController extends BaseController {
    
    @Autowired
    MonitorService monitorService;

    @GetMapping("/{id}")
    public ResponseEntity<? extends Object> getMonitor(@PathVariable Long id) {
        ResMonitorDto dto = null;
        try {
            dto = monitorService.getMonitor(id);
        } catch (BestWorkBussinessException ex) {
            return failed(ex.getMsgCode(), ex.getParam());
        }
        return success(CommonConstants.MessageCode.RLS0001, dto, null);

    }

    @PostMapping
    public ResponseEntity<? extends Object> addMonitor(@RequestBody ResMonitorDto dto) {
        try {
            monitorService.addMonitor(dto);
        } catch (BestWorkBussinessException ex) {
            return failed(ex.getMsgCode(), ex.getParam());
        }
        return success(CommonConstants.MessageCode.CPN0001, null, null);
    }

    @PutMapping
    public ResponseEntity<? extends Object> updateMonitor(@RequestBody ResMonitorDto dto) {
        ResMonitorDto resMonitorDto;
        try {
            resMonitorDto = monitorService.updateMonitor(dto);
        } catch (BestWorkBussinessException ex) {
            return failed(ex.getMsgCode(), ex.getParam());
        }
        return success(CommonConstants.MessageCode.RLS0003, resMonitorDto, null);
    }

    @PostMapping("/search")
    public ResponseEntity<? extends Object> getMonitors(@RequestBody SearchDto dto) {
        PageResponseDto<ResMonitorDto> pageSearchDto = null;
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
