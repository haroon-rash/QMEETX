package com.qmeetx.apigateway.filter;

import com.qmeetx.apigateway.config.jwtProperties;
import com.qmeetx.apigateway.exceptions.JwtInvalidException;
import com.qmeetx.apigateway.exceptions.jwtMissingException;
import com.qmeetx.apigateway.utils.JwtUserPrincipal;
import com.qmeetx.apigateway.utils.KeyLoader;
import io.jsonwebtoken.*;
import org.springframework.core.env.Environment;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.security.PublicKey;
import java.util.Collections;
import java.util.List;
@Component
public class ReactiveJwtFilter implements WebFilter {

    private final Environment env;
    private final PublicKey publicKey;
    private final String headerName;
    private final String prefix;
    private final jwtProperties jwtProperties;

    public ReactiveJwtFilter(KeyLoader keyLoader, Environment env, jwtProperties jwtProperties) throws Exception{
       this.env=env;
       this.publicKey=keyLoader.loadKey();
       this.prefix=env.getProperty("jwt.prefix","Bearer");
       this.headerName=env.getProperty("jwt.header","Authorization");
        this.jwtProperties = jwtProperties;
    }

    @Override
    public Mono<Void> filter( ServerWebExchange exchange, WebFilterChain chain) {

        String path = exchange.getRequest().getPath().toString();
        List<String> publicPaths = jwtProperties.getPublicPaths();
        boolean isPublic = publicPaths != null && publicPaths.stream()
                .filter(java.util.Objects::nonNull)
                .anyMatch(p -> matchesPath(p.trim(), path));
        if (isPublic) {
            return chain.filter(exchange); // skip JWT
        }
        String token=exchange.getRequest().getHeaders().getFirst(headerName);
        if(token==null || !token.startsWith(prefix)){
            exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String jwt =token.substring(prefix.length()).trim();
        // System.out.println("Processing JWT: " + jwt);
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();

            // System.out.println("Claims parsed successfully: " + claims);

            JwtUserPrincipal principal = new JwtUserPrincipal(
                    claims.get("id", String.class),
                    claims.getSubject(),
                    claims.get("name", String.class),
                    claims.get("role", String.class),
                    claims.get("isVerified", Boolean.class)
            );
            // Create Authentication object
            Authentication auth = new UsernamePasswordAuthenticationToken(
                    principal, // principal
                    null, // credentials

                    Collections.emptyList() // roles for Future Important to implement


            );
// Add claims as headers for blocking downstream services
            ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                    .header("X-Auth-Id", principal.auth_id())
                    .header("X-User-Email", principal.email())
                    .header("X-User-Name", principal.name() != null ? principal.name() : "")
                    .header("X-User-Role", principal.role())
                    .header("X-User-IsVerified", String.valueOf(principal.isVerified()))
                    .build();

            ServerWebExchange mutatedExchange = exchange.mutate()
                    .request(mutatedRequest)
                    .build();

            return chain.filter(mutatedExchange)
                    .contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth));
        } catch (ExpiredJwtException e) {
             System.err.println("JWT Expired: " + e.getMessage());
            return Mono.error(new JwtInvalidException("JWT token expired"));
        } catch (UnsupportedJwtException e) {
             System.err.println("JWT Unsupported: " + e.getMessage());
            return Mono.error(new JwtInvalidException("Unsupported JWT token"));
        } catch (MalformedJwtException e) {
             System.err.println("JWT Malformed: " + e.getMessage());
            return Mono.error(new JwtInvalidException("Malformed JWT token"));
        } catch (SignatureException e) {
             System.err.println("JWT Signature Invalid: " + e.getMessage());
            return Mono.error(new JwtInvalidException("JWT signature is invalid"));
        } catch (IllegalArgumentException e) {
             System.err.println("JWT IllegalArgument: " + e.getMessage());
             e.printStackTrace();
            return Mono.error(new JwtInvalidException("JWT claims string is empty"));
        } catch (Exception e) {
             System.err.println("Unexpected error in JWT filter: " + e.getMessage());
             e.printStackTrace();
             return Mono.error(new JwtInvalidException("Internal Gateway Security Error"));
        }


        }
    private boolean matchesPath(String pattern, String path) {
        if (pattern.endsWith("/**")) {
            String base = pattern.substring(0, pattern.length() - 3);
            return path.startsWith(base);
        } else {
            return path.equals(pattern);
        }
    }
}
