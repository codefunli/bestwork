package com.nineplus.bestwork.services;
import com.nineplus.bestwork.dto.PageResDto;
import com.nineplus.bestwork.dto.ActionResDto;
import com.nineplus.bestwork.dto.SearchDto;
import com.nineplus.bestwork.entity.SysActionEntity;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.model.UserAuthDetected;
import com.nineplus.bestwork.model.enumtype.Status;
import com.nineplus.bestwork.repository.SysActionRepository;
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
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Optional;

@Service
@Transactional
public class ActionService {


    private final Logger logger = LoggerFactory.getLogger(CompanyService.class);

    @Autowired
    UserAuthUtils userAuthUtils;

    @Autowired
    MessageUtils messageUtils;

    @Autowired
    SysActionRepository actionRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    private PageUtils responseUtils;

    public ActionResDto getAction(Long id) throws BestWorkBussinessException {
        Optional<SysActionEntity> action = actionRepository.findById(id);
        if (action.isPresent()) {
            return modelMapper.map(action.get(), ActionResDto.class);
        }
        throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0003, null);

    }

    @Transactional(rollbackFor = {Exception.class})
    public ActionResDto addAction(ActionResDto dto) throws BestWorkBussinessException {
        UserAuthDetected userAuthRoleReq = userAuthUtils.getUserInfoFromReq(false);
        // Only system admin can do this
        if (!userAuthRoleReq.getIsSysAdmin()) {
            logger.error(messageUtils.getMessage(CommonConstants.MessageCode.E1X0014, null));
            throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0014, null);
        }
        SysActionEntity action = null;
        try {
            action = actionRepository.findSysActionByName(dto.getName());
            if (!ObjectUtils.isEmpty(action)) {
                logger.error(messageUtils.getMessage(CommonConstants.MessageCode.E1X0014, null));
                throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0014, null);
            }
            action = new SysActionEntity();
            action.setName(dto.getName());
            action.setIcon(dto.getIcon());
            action.setUrl(dto.getUrl());
            action.setStatus(Status.fromValue(Integer.parseInt(dto.getStatus())));
            action.setHttpMethod(HttpMethod.resolve(dto.getMethod()));
            action.setCreatedDate(new Timestamp(System.currentTimeMillis()));
//            action.setCreatedUser();
            actionRepository.save(action);
            return modelMapper.map(action, ActionResDto.class);
        } catch (Exception e) {
            logger.error(messageUtils.getMessage(CommonConstants.MessageCode.E1X0001, null), e);
            throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0001, null);
        }
    }

    @Transactional(rollbackFor = {Exception.class})
    public ActionResDto updateAction(ActionResDto dto) throws BestWorkBussinessException {
        UserAuthDetected userAuthRoleReq = userAuthUtils.getUserInfoFromReq(false);
        // Only system admin can do this
        if (!userAuthRoleReq.getIsSysAdmin()) {
            logger.error(messageUtils.getMessage(CommonConstants.MessageCode.E1X0014, null));
            throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0014, null);
        }
        Optional<SysActionEntity> checkExist;
        try {
            checkExist = actionRepository.findById(dto.getId());
            if (checkExist.isEmpty()) {
                logger.error(messageUtils.getMessage(CommonConstants.MessageCode.E1X0014, null));
                throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0014, null);
            }
            SysActionEntity action = checkExist.get();
            action.setName(dto.getName());
            action.setIcon(dto.getIcon());
            action.setUrl(dto.getUrl());
            action.setStatus(Status.fromValue(Integer.parseInt(dto.getStatus())));
            action.setHttpMethod(HttpMethod.resolve(dto.getMethod()));
            action.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
//            action.setCreatedUser();
            actionRepository.save(action);
            return modelMapper.map(action, ActionResDto.class);
        }catch (Exception ex) {
            logger.error(messageUtils.getMessage(CommonConstants.MessageCode.E1X0001, null), ex);
            throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0001, null);
        }
    }

    public PageResDto<ActionResDto> getActions(SearchDto dto) throws BestWorkBussinessException {
        try {
            int pageNumber = NumberUtils.toInt(dto.getPageConditon().getPage());
            if (pageNumber > 0) {
                pageNumber = pageNumber - 1;
            }
            Pageable pageable = PageRequest.of(pageNumber, Integer.parseInt(dto.getPageConditon().getSize()),
                    Sort.by(dto.getPageConditon().getSortDirection(),
                            dto.getPageConditon().getSortBy()));
            Page<SysActionEntity> pageSysRole = actionRepository.findAllByNameContains(dto.getConditionSearchDto().getName(), pageable);
            return responseUtils.convertPageEntityToDTO(pageSysRole, ActionResDto.class);
        } catch (Exception ex) {
            logger.error(messageUtils.getMessage(CommonConstants.MessageCode.E1X0001, null), ex);
            throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0001, null);
        }
    }

    public void deleteAction(Long id) throws BestWorkBussinessException {
        try {
            UserAuthDetected userAuthRoleReq = userAuthUtils.getUserInfoFromReq(false);
            // Only system admin can do this
            if (!userAuthRoleReq.getIsSysAdmin()) {
                logger.error(messageUtils.getMessage(CommonConstants.MessageCode.E1X0014, null));
                throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0014, null);
            }
            Optional<SysActionEntity> action = actionRepository.findById(id);
            action.ifPresent(sysAction -> actionRepository.delete(sysAction));
        } catch (Exception ex) {
            logger.error(messageUtils.getMessage(CommonConstants.MessageCode.RLF0002, null), ex);
            throw new BestWorkBussinessException(CommonConstants.MessageCode.RLF0002, null);
        }
    }
}
