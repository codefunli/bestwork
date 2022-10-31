package com.nineplus.bestwork.voter;

import com.nineplus.bestwork.entity.SysAction;
import com.nineplus.bestwork.model.enumtype.Status;
import com.nineplus.bestwork.services.SysActionService;
import com.nineplus.bestwork.utils.CommonConstants;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.util.UriTemplate;

import javax.servlet.ServletContext;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomRoleBasedVoter implements AccessDecisionVoter<FilterInvocation> {
    private SysActionService sysActionService;

    @Override
    public boolean supports(ConfigAttribute attribute) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean supports(Class clazz) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public int vote(Authentication authentication, FilterInvocation fi, Collection<ConfigAttribute> attributes) {
        String url = fi.getRequestUrl().split("\\?")[0].split(CommonConstants.ApiPath.BASE_PATH)[1];
//        TO_DO
//        String controllerPath = url.substring(0, url.indexOf("/", 1) - 1);
        String methodType = fi.getRequest().getMethod();
        if (sysActionService == null) {
            ServletContext servletContext = fi.getRequest().getServletContext();
            WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
            sysActionService = webApplicationContext.getBean(SysActionService.class);
        }
        fi.getHttpRequest().getMethod();
        List<String> roleNames = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        List<SysAction> actionList = sysActionService.getSysActionBySysRole(roleNames, methodType);
        if (!actionList.isEmpty()) {
            if (actionList.stream().anyMatch(sysAction -> {
                UriTemplate uriTemplate = new UriTemplate(sysAction.getUrl());
                Map<String, String> parameters = uriTemplate.match(url);
                return !parameters.isEmpty() && sysAction.getStatus().equals(Status.ACTIVE);
            })) {
                return ACCESS_GRANTED;
            }
        }
        return ACCESS_DENIED;
    }
}
