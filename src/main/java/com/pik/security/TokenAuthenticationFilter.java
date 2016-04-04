package com.pik.security;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.web.util.NestedServletException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.MessageFormat;

/**
 * Created by Hubert on 01.04.2016.
 */
public class TokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final String SECURITY_TOKEN_KEY = "token";
    private static final String SECURITY_TOKEN_HEADER = "X-Token";

    public TokenAuthenticationFilter() {

        super( "/" );
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain ) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String token = request.getParameter( SECURITY_TOKEN_KEY );
        // or this.token = request.getHeader(SECURITY_TOKEN_HEADER);

        if ( token != null ) {

            Authentication authResult;
            try {
                authResult = attemptAuthentication( request, response, token );
                if ( authResult == null ) {
                    notAuthenticated( request, response, new LockedException( "User Not found" ) );
                    return;
                }
            } catch ( AuthenticationException failed ) {
                notAuthenticated( request, response, failed );
                return;
            }

            try {
                successfulAuthentication( request, response, chain, authResult );
                return;
            } catch ( NestedServletException e ) {
                logger.error( e.getMessage( ), e );
                if ( e.getCause( ) instanceof AccessDeniedException) {
                    notAuthenticated( request, response, new LockedException( "Forbidden" ) );
                    return;
                }
            }
        }
        System.out.println("go ahead");
        chain.doFilter( request, response );// return to others spring security filters
    }

    public void notAuthenticated( HttpServletRequest request, HttpServletResponse response, AuthenticationException failed ) throws IOException {

        response.sendRedirect( "http://www.google.ro" );
        // unsuccessfulAuthentication( request, response, failed );
    }

    public Authentication attemptAuthentication( HttpServletRequest request, HttpServletResponse response, String token ) throws AuthenticationException, IOException, ServletException {

        AbstractAuthenticationToken userAuthenticationToken = authUserByToken( token );
        if ( userAuthenticationToken == null )
            throw new AuthenticationServiceException( MessageFormat.format( "Error | {0}", "Bad Token" ) );

        return userAuthenticationToken;
    }

    private AbstractAuthenticationToken authUserByToken( String tokenRaw ) {

        AbstractAuthenticationToken authToken = null;
        try {
            // check your input token, identify the user
            // if success create AbstractAuthenticationToken for user to return
            // eg:
            // authToken = new UsernamePasswordAuthenticationToken( username, userHash, userAuthorities );
            // authToken = new UsernamePasswordAuthenticationToken( tokenRaw, authToken, )
            logger.info( "token received = " + tokenRaw );
            // obtain user by your methods
            // if ( user != null ) {
            // SecurityUser securityUser = new SecurityUser( user );
            // return new PreAuthenticatedAuthenticationToken( securityUser, securityUser.getPassword( ), securityUser.getAuthorities( ) );
            // }
        } catch ( Exception e ) {
            logger.error( "Error during authUserByToken", e );
        }
        return authToken;
    }

    protected void successfulAuthentication( HttpServletRequest request, HttpServletResponse response, Authentication authResult ) throws IOException, ServletException {

        SecurityContextHolder.getContext( ).setAuthentication( authResult );

        new CustomAuthenticationSuccessHandler( ).onAuthenticationSuccess( request, response, authResult );
    }

    @Override
    public Authentication attemptAuthentication( HttpServletRequest request, HttpServletResponse response ) throws AuthenticationException, IOException, ServletException {

        logger.error( "No TOKEN PROVIDED" );
        return null;
    }

}