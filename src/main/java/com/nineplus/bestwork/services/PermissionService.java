package com.nineplus.bestwork.services;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.math.NumberUtils;
import org.modelmapper.Converter;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nineplus.bestwork.dto.PageResponseDto;
import com.nineplus.bestwork.dto.RegPermissionDto;
import com.nineplus.bestwork.dto.ResPermissionDto;
import com.nineplus.bestwork.dto.SearchDto;
import com.nineplus.bestwork.entity.SysMonitor;
import com.nineplus.bestwork.entity.SysPermission;
import com.nineplus.bestwork.entity.TRole;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.model.UserAuthDetected;
import com.nineplus.bestwork.model.enumtype.Status;
import com.nineplus.bestwork.repository.PermissionRepository;
import com.nineplus.bestwork.repository.SysMonitorRepository;
import com.nineplus.bestwork.repository.TRoleRepository;
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
    private TRoleRepository sysRoleRepository;

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

    public List<ResPermissionDto> updatePermissions(RegPermissionDto dto) throws BestWorkBussinessException {
        Optional<SysPermission> checkExist;
        try {
            Optional<TRole> role = sysRoleRepository.findById(dto.getRoleId());
            if (role.isEmpty()) {
                logger.error(messageUtils.getMessage(CommonConstants.MessageCode.E1X0014, null));
                throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0014, null);
            }
            Converter<String, Integer> classificationConverter =
                    ctx -> ctx.getSource() == null ? null : Status.getStatusEnum(ctx.getSource());
            modelMapper.typeMap(SysPermission.class,ResPermissionDto.class)
                    .addMappings(mapper -> mapper.using(classificationConverter).map(SysPermission::getStatus,ResPermissionDto::setStatus));
            List<ResPermissionDto> sysPermissions = dto.getMonitorInfo().stream().map(permissionDto -> {
                        SysPermission sysPermission = new SysPermission();
                        SysMonitor sysMonitor = new SysMonitor();
                        if (permissionDto.getId() != null) {
                            sysPermission = permissionRepository.findById(permissionDto.getId())
                                    .orElse(new SysPermission());
                            sysMonitor = sysPermission.getSysMonitor();
                        } else if (permissionDto.getMonitorId() != null) {
                            sysMonitor = monitorRepository.findById(permissionDto.getMonitorId())
                                    .orElse(new SysMonitor());
                        }
                        try {
                            if (sysPermission.getId() != null) {
                                sysPermission.setUpdatedUser
                                        (userAuthUtils.getUserInfoFromReq(false).getUsername());
                                sysPermission.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
                            } else {
                                sysPermission.setCreatedUser
                                        (userAuthUtils.getUserInfoFromReq(false).getUsername());
                                sysPermission.setCreatedDate(new Timestamp(System.currentTimeMillis()));
                            }
                            if (sysMonitor.getId() != null) {
                                sysMonitor.setUpdatedUser
                                        (userAuthUtils.getUserInfoFromReq(false).getUsername());
                                sysMonitor.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
                            } else {
                                sysMonitor.setCreatedUser
                                        (userAuthUtils.getUserInfoFromReq(false).getUsername());
                                sysMonitor.setCreatedDate(new Timestamp(System.currentTimeMillis()));
                            }
                        } catch (BestWorkBussinessException e) {
                            throw new RuntimeException(e);
                        }
                        sysMonitor.setName(permissionDto.getMonitorName());
                        sysPermission.setSysRole(role.get());
                        sysPermission.setSysMonitor(sysMonitor);
                        sysPermission.setCanDelete(permissionDto.getCanDelete());
                        sysPermission.setCanEdit(permissionDto.getCanEdit());
                        sysPermission.setCanAdd(permissionDto.getCanAdd());
                        sysPermission.setCanAccess(permissionDto.getCanAccess());
                        sysPermission.setStatus(Status.fromValue(permissionDto.getStatus()));
                        return modelMapper.map(permissionRepository.save(sysPermission),ResPermissionDto.class);
                    }
            ).collect(Collectors.toList());
            return sysPermissions;
        } catch (Exception ex) {
            logger.error(messageUtils.getMessage(CommonConstants.MessageCode.E1X0001, null), ex);
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
                    permissionRepository.findBySysMonitor_NameContainingIgnoreCaseAndSysRole_RoleNameContainingIgnoreCase(
                            dto.getConditionSearchDto().getMonitorName(), dto.getConditionSearchDto().getRoleName(), pageable);
            return responseUtils.convertPageEntityToDTO(pageSysRole, ResPermissionDto.class);
        } catch (Exception ex) {
            logger.error(messageUtils.getMessage(CommonConstants.MessageCode.E1X0001, null), ex);
            throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0001, null);
        }
    }
}
