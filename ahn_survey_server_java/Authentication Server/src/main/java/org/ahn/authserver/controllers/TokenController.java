/*
 * Copyright 2017 - Allegheny Health Network
 * @author Roy Gustafson <roy.gustafson@ahn.org> <royagustafson@gmail.com>
 */
package org.ahn.authserver.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 *
 * @author rgustafs
 */
@Controller
public class TokenController {

    @Resource(name = "tokenServices")
    ConsumerTokenServices tokenServices;

    @Resource(name = "tokenStore")
    TokenStore tokenStore;

    @RequestMapping(value = "/oauth/token/revokeById/{tokenId}", method = POST)
    @ResponseBody
    public void revokeToken(
            HttpServletRequest request, @PathVariable String tokenId) {

        tokenServices.revokeToken(tokenId);

    }

    @RequestMapping(value = "/tokens", method = GET)
    @ResponseBody
    public List<String> getTokens() {
        List<String> tokenValues = new ArrayList<>();
        Collection<OAuth2AccessToken> tokens;
        tokens = tokenStore.findTokensByClientId("sampleClientId");
        if (tokens != null) {
            tokens.forEach(new ConsumerImpl(tokenValues));
        }
        return tokenValues;
    }

    @RequestMapping(value = "/tokens/revokeRefreshToken/{tokenId:.*}", method = POST)
    @ResponseBody
    public String revokeRefreshToken(@PathVariable String tokenId) {
        if (tokenStore instanceof JdbcTokenStore) {
            ((JdbcTokenStore) tokenStore).removeRefreshToken(tokenId);
        }
        return tokenId;
    }

    private static class ConsumerImpl implements Consumer<OAuth2AccessToken> {

        private final List<String> tokenValues;

        public ConsumerImpl(List<String> tokenValues) {
            this.tokenValues = tokenValues;
        }

        @Override
        public void accept(OAuth2AccessToken token) {
            tokenValues.add(token.getValue());
        }
    }

}
