package com.nineplus.bestwork.services;

import com.nineplus.bestwork.dto.PageResponseDto;
import com.nineplus.bestwork.dto.ResRoleDto;
import com.nineplus.bestwork.dto.SearchDto;
import com.nineplus.bestwork.entity.TRole;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.model.UserAuthDetected;
import com.nineplus.bestwork.repository.TRoleRepository;
import com.nineplus.bestwork.utils.CommonConstants;
import com.nineplus.bestwork.utils.MessageUtils;
import com.nineplus.bestwork.utils.PageUtils;
import com.nineplus.bestwork.utils.UserAuthUtils;
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

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class RoleService {

    private final Logger logger = LoggerFactory.getLogger(CompanyService.class);

    @Autowired
    UserAuthUtils userAuthUtils;

    @Autowired
    MessageUtils messageUtils;

    @Autowired
    TRoleRepository roleRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    private PageUtils responseUtils;

    public ResRoleDto getRole(Long id) throws BestWorkBussinessException {
        Optional<TRole> role = roleRepository.findById(id);
        if (role.isPresent()) {
            return modelMapper.map(role.get(), ResRoleDto.class);
        }
        throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0003, null);
    }

    @Transactional(rollbackFor = {Exception.class})
    public ResRoleDto addRole(ResRoleDto dto) throws BestWorkBussinessException {
        UserAuthDetected userAuthRoleReq = userAuthUtils.getUserInfoFromReq(false);
        // Only system admin can do this
        if (!userAuthRoleReq.getIsSysAdmin()) {
            logger.error(messageUtils.getMessage(CommonConstants.MessageCode.E1X0014, null));
            throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0014, null);
        }
        TRole role = null;
        try {
            role = roleRepository.findRole(dto.getName());
            if (!ObjectUtils.isEmpty(role)) {
                logger.error(messageUtils.getMessage(CommonConstants.MessageCode.E1X0014, null));
                throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0014, null);
            }
            role = new TRole();
            role.setRoleName(dto.getName());
            role.setDescription(dto.getDescription());
            role.setCreateDate(LocalDateTime.now());
            role.setCreateBy(userAuthUtils.getUserInfoFromReq(false).getUsername());
            roleRepository.save(role);
            return modelMapper.map(role, ResRoleDto.class);
        } catch (Exception e) {
            logger.error(messageUtils.getMessage(CommonConstants.MessageCode.E1X0001, null), e);
            throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0001, null);
        }
    }

    @Transactional(rollbackFor = {Exception.class})
    public ResRoleDto updateRole(ResRoleDto dto) throws BestWorkBussinessException {
        UserAuthDetected userAuthRoleReq = userAuthUtils.getUserInfoFromReq(false);
        // Only system admin can do this
        if (!userAuthRoleReq.getIsSysAdmin()) {
            logger.error(messageUtils.getMessage(CommonConstants.MessageCode.E1X0014, null));
            throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0014, null);
        }
        TRole role = null;
        try {
            role = roleRepository.findById(dto.getId()).orElse(null);
            if (ObjectUtils.isEmpty(role)) {
                logger.error(messageUtils.getMessage(CommonConstants.MessageCode.E1X0013, null));
                throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0013, null);
            }
            role.setRoleName(dto.getName());
            role.setDescription(dto.getDescription());
            role.setUpdateDate(LocalDateTime.now());
            role.setUpdateBy(userAuthUtils.getUserInfoFromReq(false).getUsername());
            roleRepository.save(role);
            return modelMapper.map(role, ResRoleDto.class);
        } catch (Exception e) {
            logger.error(messageUtils.getMessage(CommonConstants.MessageCode.E1X0001, null), e);
            throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0001, null);
        }
    }

    public PageResponseDto<ResRoleDto> getRoles(SearchDto pageSearchDto) throws BestWorkBussinessException {
        try {
            int pageNumber = NumberUtils.toInt(pageSearchDto.getPageConditon().getPage());
            if (pageNumber > 0) {
                pageNumber = pageNumber - 1;
            }
            Pageable pageable = PageRequest.of(pageNumber, Integer.parseInt(pageSearchDto.getPageConditon().getSize()),
                    Sort.by(pageSearchDto.getPageConditon().getSortDirection(),
                            pageSearchDto.getPageConditon().getSortBy()));
            Page<TRole> pageSysRole = roleRepository.findTRolesByRoleNameContaining
                    (pageSearchDto.getConditionSearchDto().getName(), pageable);
            return responseUtils.convertPageEntityToDTO(pageSysRole, ResRoleDto.class);
        } catch (Exception ex) {
            logger.error(messageUtils.getMessage(CommonConstants.MessageCode.E1X0001, null), ex);
            throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0001, null);
        }
    }

    public void deleteRole(Long id) throws BestWorkBussinessException {
        try {
            UserAuthDetected userAuthRoleReq = userAuthUtils.getUserInfoFromReq(false);
            // Only system admin can do this
            if (!userAuthRoleReq.getIsSysAdmin()) {
                logger.error(messageUtils.getMessage(CommonConstants.MessageCode.E1X0014, null));
                throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0014, null);
            }
            Optional<TRole> role = roleRepository.findById(id);
            role.ifPresent(sysRole -> roleRepository.delete(sysRole));
        } catch (Exception ex) {
            logger.error(messageUtils.getMessage(CommonConstants.MessageCode.RLF0002, null), ex);
            throw new BestWorkBussinessException(CommonConstants.MessageCode.RLF0002, null);
        }
    }
}
