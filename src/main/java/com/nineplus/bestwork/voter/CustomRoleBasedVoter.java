package com.nineplus.bestwork.voter;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.util.UriTemplate;

import com.nineplus.bestwork.entity.SysActionEntity;
import com.nineplus.bestwork.model.enumtype.Status;
import com.nineplus.bestwork.services.SysActionService;
import com.nineplus.bestwork.utils.CommonConstants;

public class CustomRoleBasedVoter implements AccessDecisionVoter<FilterInvocation> {
    private SysActionService sysActionService;
    public static String[] PUBLIC_URL;

    public CustomRoleBasedVoter(String[] PUBLIC_URL_LIST) {
        PUBLIC_URL = PUBLIC_URL_LIST;
    }

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
        List<SysActionEntity> actionList = sysActionService.getSysActionBySysRole(roleNames, methodType);
        if (!actionList.isEmpty()) {
            if (actionList.stream().anyMatch(sysAction -> {
                UriTemplate uriTemplate = new UriTemplate(sysAction.getUrl());
                Map<String, String> parameters = uriTemplate.match(url);
                boolean isPublicUrl = false;
                for (String publicUrl : PUBLIC_URL) {
                    uriTemplate = new UriTemplate(publicUrl);
                    if (!uriTemplate.match(url).isEmpty()) {
                        isPublicUrl = true;
                        break;
                    }
                }
                return ((!parameters.isEmpty() && sysAction.getStatus().equals(Status.ACTIVE))
                        || url.equals(sysAction.getUrl()) || isPublicUrl);
            })) {
                return ACCESS_GRANTED;
            }
        }
        return ACCESS_DENIED;
    }
}
