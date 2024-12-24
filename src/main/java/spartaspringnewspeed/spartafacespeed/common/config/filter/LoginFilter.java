package spartaspringnewspeed.spartafacespeed.common.config.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


import java.io.IOException;
import java.util.Set;

public class LoginFilter implements Filter {
    private static final Set<String> WHITE_LIST = Set.of("/users/login", "/users/signup");

    @Override
    public void doFilter(
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain filterChain
            ) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        String requestURI = httpRequest.getRequestURI();

        if(!isWhiteList(requestURI)) {
            HttpSession session = httpRequest.getSession(false);
            if(session == null || session.getAttribute("userId") == null) {
                exceptionHandler((HttpServletResponse) servletResponse, "로그인이 필요합니다.");
                return;
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private boolean isWhiteList(String requestURI) {
        return WHITE_LIST.contains(requestURI);
    }

    private void exceptionHandler(HttpServletResponse response, String errorMessage) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String jsonResponse = String.format(
                "{\"error\": \"UNAUTHORIZED\", \"message\": \"%s\"}",
                errorMessage
        );
        response.getWriter().write(jsonResponse);
    }
}
