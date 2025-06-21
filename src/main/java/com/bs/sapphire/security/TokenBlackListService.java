package com.bs.sapphire.security;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class TokenBlackListService {
    private final Set<String> blackListedTokens = new HashSet<>();

    public void blackListToken(String token) {
        blackListedTokens.add(token);
    }

    public boolean isTokenBlackListed(String token) {
        return blackListedTokens.contains(token);
    }
}
