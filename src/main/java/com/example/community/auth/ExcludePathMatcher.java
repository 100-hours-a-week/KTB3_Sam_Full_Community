package com.example.community.auth;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.List;

@Component
public class ExcludePathMatcher {
    private final List<Rule> excludeRules = List.of(
            Rule.of("POST", "/auth"),
            Rule.of("POST", "/users")
    );

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    public boolean isExcluded(HttpServletRequest req) {
        String method = req.getMethod();
        String path = req.getRequestURI();

        return excludeRules.stream()
                .anyMatch(rule -> rule.method.equalsIgnoreCase(method)
                    && pathMatcher.match(rule.path, path));
    }

    private static class Rule {
        private final String method;
        private final String path;

        private Rule(String method, String path) {
            this.method = method;
            this.path = path;
        }

        static Rule of(String method, String path) {
            return new Rule(method,path);
        }
    }
}
