package com.nineplus.bestwork.services;

import java.sql.Timestamp;
import java.util.Optional;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nineplus.bestwork.dto.PageResponseDto;
import com.nineplus.bestwork.dto.ResMonitorDto;
import com.nineplus.bestwork.dto.SearchDto;
import com.nineplus.bestwork.entity.SysMonitor;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.model.UserAuthDetected;
import com.nineplus.bestwork.repository.SysMonitorRepository;
import com.nineplus.bestwork.utils.CommonConstants;
import com.nineplus.bestwork.utils.MessageUtils;
import com.nineplus.bestwork.utils.PageUtils;
import com.nineplus.bestwork.utils.UserAuthUtils;

@Service
@Transactional
public class MonitorService {


    private final Logger logger = LoggerFactory.getLogger(CompanyService.class);

    @Autowired
    UserAuthUtils userAuthUtils;

    @Autowired
    MessageUtils messageUtils;

    @Autowired
    SysMonitorRepository monitorRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    private PageUtils responseUtils;

    public ResMonitorDto getMonitor(Long id) throws BestWorkBussinessException {
        Optional<SysMonitor> monitor = monitorRepository.findById(id);
        if (monitor.isPresent()) {
            return modelMapper.map(monitor.get(), ResMonitorDto.class);
        }
        throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0003, null);

    }

    @Transactional(rollbackFor = {Exception.class})
    public ResMonitorDto addMonitor(ResMonitorDto dto) throws BestWorkBussinessException {
        UserAuthDetected userAuthRoleReq = userAuthUtils.getUserInfoFromReq(false);
        // Only system admin can do this
        if (!userAuthRoleReq.getIsSysAdmin()) {
            logger.error(messageUtils.getMessage(CommonConstants.MessageCode.E1X0014, null));
            throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0014, null);
        }
        SysMonitor monitor = null;
        try {
            monitor = monitorRepository.findSysMonitorByName(dto.getName());
            if (!ObjectUtils.isEmpty(monitor)) {
                logger.error(messageUtils.getMessage(CommonConstants.MessageCode.E1X0014, null));
                throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0014, null);
            }
            monitor = new SysMonitor();
            monitor.setName(dto.getName());
            monitor.setIcon(dto.getIcon());
            monitor.setParentId(Long.valueOf(dto.getParentId()));
            monitor.setDisplayOrder(Integer.parseInt(dto.getDisplayOrder()));
            monitor.setUrl(dto.getUrl());
            monitor.setShowAdd(Integer.parseInt(dto.getShowAdd()));
            monitor.setShowAccess(Integer.parseInt(dto.getShowAccess()));
            monitor.setShowDelete(Integer.parseInt(dto.getShowDelete()));
            monitor.setShowEdit(Integer.parseInt(dto.getShowEdit()));
            monitor.setCreatedDate(new Timestamp(System.currentTimeMillis()));
//            monitor.setCreatedUser();
            monitorRepository.save(monitor);
            return modelMapper.map(monitor, ResMonitorDto.class);
        } catch (Exception e) {
            logger.error(messageUtils.getMessage(CommonConstants.MessageCode.E1X0001, null), e);
            throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0001, null);
        }
    }

    @Transactional(rollbackFor = {Exception.class})
    public ResMonitorDto updateMonitor(ResMonitorDto dto) throws BestWorkBussinessException {
        UserAuthDetected userAuthRoleReq = userAuthUtils.getUserInfoFromReq(false);
        // Only system admin can do this
        if (!userAuthRoleReq.getIsSysAdmin()) {
            logger.error(messageUtils.getMessage(CommonConstants.MessageCode.E1X0014, null));
            throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0014, null);
        }
        Optional<SysMonitor> checkExist;
        try {
            checkExist = monitorRepository.findById(dto.getId());
            if (checkExist.isEmpty()) {
                logger.error(messageUtils.getMessage(CommonConstants.MessageCode.E1X0014, null));
                throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0014, null);
            }
            SysMonitor monitor = checkExist.get();
            monitor.setName(dto.getName());
            monitor.setIcon(dto.getIcon());
            monitor.setParentId(Long.valueOf(dto.getParentId()));
            monitor.setDisplayOrder(Integer.parseInt(dto.getDisplayOrder()));
            monitor.setUrl(dto.getUrl());
            monitor.setShowAdd(Integer.parseInt(dto.getShowAdd()));
            monitor.setShowAccess(Integer.parseInt(dto.getShowAccess()));
            monitor.setShowDelete(Integer.parseInt(dto.getShowDelete()));
            monitor.setShowEdit(Integer.parseInt(dto.getShowEdit()));
            monitor.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
//            monitor.setCreatedUser();
            monitorRepository.save(monitor);
            return modelMapper.map(monitor, ResMonitorDto.class);
        }catch (Exception ex) {
            logger.error(messageUtils.getMessage(CommonConstants.MessageCode.E1X0001, null), ex);
            throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0001, null);
        }
    }

    public PageResponseDto<ResMonitorDto> getMonitors(SearchDto dto) throws BestWorkBussinessException {
        try {
            int pageNumber = NumberUtils.toInt(dto.getPageConditon().getPage());
            if (pageNumber > 0) {
                pageNumber = pageNumber - 1;
            }
            Pageable pageable = PageRequest.of(pageNumber, Integer.parseInt(dto.getPageConditon().getSize()),
                    Sort.by(dto.getPageConditon().getSortDirection(),
                            dto.getPageConditon().getSortBy()));
            Page<SysMonitor> pageSysRole = monitorRepository.findAllByNameContains(dto.getConditionSearchDto().getName(), pageable);
            return responseUtils.convertPageEntityToDTO(pageSysRole, ResMonitorDto.class);
        } catch (Exception ex) {
            logger.error(messageUtils.getMessage(CommonConstants.MessageCode.E1X0001, null), ex);
            throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0001, null);
        }
    }

    public void deleteMonitor(Long id) throws BestWorkBussinessException {
        try {
            UserAuthDetected userAuthRoleReq = userAuthUtils.getUserInfoFromReq(false);
            // Only system admin can do this
            if (!userAuthRoleReq.getIsSysAdmin()) {
                logger.error(messageUtils.getMessage(CommonConstants.MessageCode.E1X0014, null));
                throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0014, null);
            }
            Optional<SysMonitor> monitor = monitorRepository.findById(id);
            monitor.ifPresent(sysMonitor -> monitorRepository.delete(sysMonitor));
        } catch (Exception ex) {
            logger.error(messageUtils.getMessage(CommonConstants.MessageCode.RLF0002, null), ex);
            throw new BestWorkBussinessException(CommonConstants.MessageCode.RLF0002, null);
        }
    }
}
