package com.nineplus.bestwork.services;

import java.sql.Timestamp;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nineplus.bestwork.dto.PageResponseDto;
import com.nineplus.bestwork.dto.ResPermissionDto;
import com.nineplus.bestwork.dto.SearchDto;
import com.nineplus.bestwork.entity.SysMonitor;
import com.nineplus.bestwork.entity.SysPermission;
import com.nineplus.bestwork.entity.SysRole;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.model.UserAuthDetected;
import com.nineplus.bestwork.repository.PermissionRepository;
import com.nineplus.bestwork.repository.SysMonitorRepository;
import com.nineplus.bestwork.repository.SysRoleRepository;
import com.nineplus.bestwork.utils.CommonConstants;
import com.nineplus.bestwork.utils.MessageUtils;
import com.nineplus.bestwork.utils.PageUtils;
import com.nineplus.bestwork.utils.UserAuthUtils;

@Service
@Transactional
public class PermissionService {

    private final Logger logger = LoggerFactory.getLogger(CompanyService.class);

    @Autowired
    private UserAuthUtils userAuthUtils;

    @Autowired
    private MessageUtils messageUtils;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private SysRoleRepository sysRoleRepository;

    @Autowired
    private SysMonitorRepository monitorRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PageUtils responseUtils;

    @Autowired
    private ObjectMapper mapper;

    public void deletePermission(Long id) throws BestWorkBussinessException {
        try {
            UserAuthDetected userAuthRoleReq = userAuthUtils.getUserInfoFromReq(false);
            // Only system admin can do this
            if (!userAuthRoleReq.getIsSysAdmin()) {
                logger.error(messageUtils.getMessage(CommonConstants.MessageCode.E1X0014, null));
                throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0014, null);
            }
            Optional<SysPermission> sysPermission = permissionRepository.findById(id);
            sysPermission.ifPresent(permission -> permissionRepository.delete(permission));
        } catch (Exception ex) {
            logger.error(messageUtils.getMessage(CommonConstants.MessageCode.RLF0002, null), ex);
            throw new BestWorkBussinessException(CommonConstants.MessageCode.RLF0002, null);
        }
    }

    public ResPermissionDto updatePermission(ResPermissionDto dto) throws BestWorkBussinessException {
        UserAuthDetected userAuthRoleReq = userAuthUtils.getUserInfoFromReq(false);
        // Only system admin can do this
        if (!userAuthRoleReq.getIsSysAdmin()) {
            logger.error(messageUtils.getMessage(CommonConstants.MessageCode.E1X0014, null));
            throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0014, null);
        }
        Optional<SysPermission> checkExist;
        try {
            checkExist = permissionRepository.findById(dto.getId());
            if (checkExist.isEmpty()) {
                logger.error(messageUtils.getMessage(CommonConstants.MessageCode.E1X0014, null));
                throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0014, null);
            }
            SysPermission sysPermission = checkExist.get();
            sysPermission.setCanAdd(Integer.parseInt(dto.getCanAdd()));
            sysPermission.setCanAccess(Integer.parseInt(dto.getCanAccess()));
            sysPermission.setCanDelete(Integer.parseInt(dto.getCanDelete()));
            sysPermission.setCanEdit(Integer.parseInt(dto.getCanEdit()));
            sysPermission.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
//            sysPermission.setCreatedUser();
            if (!StringUtils.isEmpty(dto.getMonitorId()) && sysPermission.getSysMonitor() != null
                    && !Long.valueOf(dto.getMonitorId()).equals(sysPermission.getSysMonitor().getId())) {
                SysMonitor monitor = mapper.readValue(dto.getMonitorId(), new TypeReference<SysMonitor>() {
                });
                monitor = monitorRepository.findById(monitor.getId()).orElse(null);
                sysPermission.setSysMonitor(monitor);
            }

            if (!StringUtils.isEmpty(dto.getRoleId()) && sysPermission.getSysRole() != null
                    && !Long.valueOf(dto.getRoleId()).equals(sysPermission.getSysRole().getId())) {
                SysRole role = mapper.readValue(dto.getRoleId(), new TypeReference<SysRole>() {
                });
                role = sysRoleRepository.findById(role.getId()).orElse(null);
                sysPermission.setSysRole(role);
            }
            permissionRepository.save(sysPermission);
            return modelMapper.map(sysPermission, ResPermissionDto.class);
        } catch (Exception ex) {
            logger.error(messageUtils.getMessage(CommonConstants.MessageCode.E1X0001, null), ex);
            throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0001, null);
        }
    }

    public ResPermissionDto addPermission(ResPermissionDto dto) throws BestWorkBussinessException {
        UserAuthDetected userAuthRoleReq = userAuthUtils.getUserInfoFromReq(false);
        // Only system admin can do this
        if (!userAuthRoleReq.getIsSysAdmin()) {
            logger.error(messageUtils.getMessage(CommonConstants.MessageCode.E1X0014, null));
            throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0014, null);
        }
        SysPermission sysPermission = null;
        try {
            sysPermission = new SysPermission();
            sysPermission.setCanAdd(Integer.parseInt(dto.getCanAdd()));
            sysPermission.setCanEdit(Integer.parseInt(dto.getCanEdit()));
            sysPermission.setCanAccess(Integer.parseInt(dto.getCanAccess()));
            sysPermission.setCanDelete(Integer.parseInt(dto.getCanDelete()));
            sysPermission.setCreatedDate(new Timestamp(System.currentTimeMillis()));
//            sysPermission.setCreatedUser();
            if (!StringUtils.isEmpty(dto.getMonitorId())) {
                SysMonitor monitor = mapper.readValue(dto.getMonitorId(), new TypeReference<SysMonitor>() {
                });
                monitor = monitorRepository.findById(monitor.getId()).orElse(null);
                sysPermission.setSysMonitor(monitor);
            }

            if (!StringUtils.isEmpty(dto.getRoleId())) {
                SysRole role = mapper.readValue(dto.getMonitorId(), new TypeReference<SysRole>() {
                });
                role = sysRoleRepository.findById(role.getId()).orElse(null);
                sysPermission.setSysRole(role);
            }
            permissionRepository.save(sysPermission);
            return modelMapper.map(sysPermission, ResPermissionDto.class);
        } catch (Exception e) {
            logger.error(messageUtils.getMessage(CommonConstants.MessageCode.E1X0001, null), e);
            throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0001, null);
        }
    }

    public ResPermissionDto getPermission(Long id) throws BestWorkBussinessException {
        Optional<SysPermission> sysPermission = permissionRepository.findById(id);
        if (sysPermission.isPresent()) {
            return modelMapper.map(sysPermission.get(), ResPermissionDto.class);
        }
        throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0003, null);

    }

    public PageResponseDto<ResPermissionDto> getPermissions(SearchDto dto) throws BestWorkBussinessException {
        try {
            int pageNumber = NumberUtils.toInt(dto.getPageConditon().getPage());
            if (pageNumber > 0) {
                pageNumber = pageNumber - 1;
            }
            Pageable pageable = PageRequest.of(pageNumber, Integer.parseInt(dto.getPageConditon().getSize()),
                    Sort.by(dto.getPageConditon().getSortDirection(),
                            dto.getPageConditon().getSortBy()));
            Page<SysPermission> pageSysRole =
                    permissionRepository.findBySysMonitor_NameContainingIgnoreCaseAndSysRole_NameContainingIgnoreCase(
                            dto.getConditionSearchDto().getMonitorName(),dto.getConditionSearchDto().getRoleName(), pageable);
            return responseUtils.convertPageEntityToDTO(pageSysRole, ResPermissionDto.class);
        } catch (Exception ex) {
            logger.error(messageUtils.getMessage(CommonConstants.MessageCode.E1X0001, null), ex);
            throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0001, null);
        }
    }
}
