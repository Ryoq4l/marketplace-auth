package com.adelok.marketplace_auth.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.util.Assert;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class MultiSessionOAuth2AuthorizationRequestRepository
        implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    private static final String ATTR = "oauth2_authorization_requests";

    private static final int MAX_PENDING = 10;

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        String state = request.getParameter("state");
        if (state == null) {
            return null;
        }
        Map<String, OAuth2AuthorizationRequest> map = getMap(request);
        return map != null ? map.get(state) : null;
    }

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authRequest,
                                         HttpServletRequest request,
                                         HttpServletResponse response) {
        Assert.notNull(request, "request cannot be null");
        Assert.notNull(response, "response cannot be null");

        if (authRequest == null) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.removeAttribute(ATTR);
            }
            return;
        }

        Map<String, OAuth2AuthorizationRequest> map = getMap(request);
        if (map == null) {
            map = new LinkedHashMap<>();
            request.getSession().setAttribute(ATTR, map);
        }
        map.put(authRequest.getState(), authRequest);

        while (map.size() > MAX_PENDING) {
            Iterator<String> it = map.keySet().iterator();
            it.next();
            it.remove();
        }
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request,
                                                                 HttpServletResponse response) {
        OAuth2AuthorizationRequest req = loadAuthorizationRequest(request);
        if (req != null) {
            Map<String, OAuth2AuthorizationRequest> map = getMap(request);
            if (map != null) {
                map.remove(req.getState());
                if (map.isEmpty()) {
                    HttpSession session = request.getSession(false);
                    if (session != null) {
                        session.removeAttribute(ATTR);
                    }
                }
            }
        }
        return req;
    }

    @SuppressWarnings("unchecked")
    private Map<String, OAuth2AuthorizationRequest> getMap(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null
                ? (Map<String, OAuth2AuthorizationRequest>) session.getAttribute(ATTR)
                : null;
    }
}