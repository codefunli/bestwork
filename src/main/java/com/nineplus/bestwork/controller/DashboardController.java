package com.nineplus.bestwork.controller;

import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.services.*;
import com.nineplus.bestwork.services.impl.ConstructionServiceImpl;
import com.nineplus.bestwork.utils.CommonConstants;
import com.nineplus.bestwork.utils.UserAuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/dashboard")
public class DashboardController extends BaseController {

    @Autowired
    UserAuthUtils userAuthUtils;

    @Autowired
    CompanyService companyService;

    @Autowired
    IProjectService iProjectService;

    @Autowired
    IConstructionService iConstructionService;

    @Autowired
    IAirWayBillService iAirWayBillService;

    @Autowired
    IProgressService iProgressService;

    @GetMapping("/awb")
    public ResponseEntity<?> countAwbUser() throws BestWorkBussinessException {
        return success(CommonConstants.MessageCode.SCN001, iAirWayBillService.countAwbUser(userAuthUtils.getUserInfoFromReq(false).getUsername()), null);
    }

    @GetMapping("/company")
    public ResponseEntity<?> countCompanyUser() throws BestWorkBussinessException {
        return success(CommonConstants.MessageCode.SCN001, companyService.countCompanyUser(userAuthUtils.getUserInfoFromReq(false).getUsername()), null);
    }

    @GetMapping("/project")
    public ResponseEntity<?> countProjectUser() throws BestWorkBussinessException {
        return success(CommonConstants.MessageCode.SCN001, iProjectService.countProjectUser(userAuthUtils.getUserInfoFromReq(false).getUsername()), null);
    }

    @GetMapping("/construction")
    public ResponseEntity<?> countConstructionUser() throws BestWorkBussinessException {
        return success(CommonConstants.MessageCode.SCN001, iConstructionService.countConstructionUser(userAuthUtils.getUserInfoFromReq(false).getUsername()), null);
    }

    @GetMapping("/search/{year}")
    public ResponseEntity<?> searchPrjConsByMonth(@PathVariable("year") Integer year) throws BestWorkBussinessException {
        List<List<Integer>> listReturn = iProjectService.countPrjConsByMonth(userAuthUtils.getUserInfoFromReq(false).getUsername(), year);
        return success(CommonConstants.MessageCode.SCN001, listReturn, null);
    }

    @GetMapping("/awb/status")
    public ResponseEntity<?> countAwbUserByStt() throws BestWorkBussinessException {
        return success(CommonConstants.MessageCode.SCN001, iAirWayBillService.countAwbByStatus(userAuthUtils.getUserInfoFromReq(false).getUsername()), null);
    }

    @GetMapping("/progress")
    public ResponseEntity<? extends Object> getProgress() throws BestWorkBussinessException {
        return success(CommonConstants.MessageCode.SCN001, iProgressService.getProgressUser(userAuthUtils.getUserInfoFromReq(false).getUsername()), null);
    }

    @GetMapping("/location")
    public ResponseEntity<? extends Object> getLocation() throws BestWorkBussinessException {
        return success(CommonConstants.MessageCode.SCN001, iConstructionService.getLocationsUser(userAuthUtils.getUserInfoFromReq(false).getUsername()), null);
    }

}
