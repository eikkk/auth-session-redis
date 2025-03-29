package com.plainprog.auth_session_redis.controllers;

import com.plainprog.auth_session_redis.model.SessionData;
import com.plainprog.auth_session_redis.service.SessionExplorerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/session")
public class SessionController {
    @Autowired
    private SessionExplorerService sessionExplorerService;

    @PostMapping("/validate")
    public ResponseEntity<SessionData> validateSession(HttpSession session) {
        //this endpoint protected by spring boot authentication
        //if we are here, then session cookie is valid
        SessionData data = sessionExplorerService.getSessionData(session.getId());
        return ResponseEntity.ok(data);
    }

    @PostMapping("/initiate")
    public String initiateSession(@RequestBody Object data, @RequestHeader String user, @RequestHeader String authorities, HttpSession session) {
        List<String> authoritiesList = List.of(authorities.split(","));
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user,
                null,
                authoritiesList.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
        session.setAttribute("data", data);
        return "Session initiated";
    }
}
