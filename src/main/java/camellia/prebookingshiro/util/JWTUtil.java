package camellia.prebookingshiro.util;

import io.jsonwebtoken.*;

import java.util.Date;

/**
 * @author anthea
 * @date 2023/11/2 14:38
 */
public class JWTUtil {
    private final static String JWT_KEY = "fqrgq988ujregqeyhewg";

    public final static Long REFRESH_KEY = 10L;

    private final static long ttlMillis = 1000 * 60 * 60 ;

    public static String createToken(String username, String password, String subject, Long nowMillis) {
        Date now = new Date(nowMillis);
        Date expireTime = new Date(nowMillis + ttlMillis);
        JwtBuilder builder = Jwts.builder()
                .setId(username)
                .setSubject(subject)
                .setIssuedAt(now)
                .claim("psw", password)
                .claim("currMills", nowMillis)
                .setExpiration(expireTime)
                .signWith(SignatureAlgorithm.HS256, JWT_KEY);
        return builder.compact();
    }

    public static Claims parseJWT(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(JWT_KEY)
                    .parseClaimsJws(token)
                    .getBody();
            return claims;
        } catch (ExpiredJwtException e) {
            return null;
        }
    }

    public static boolean isTokenExpired(Date expiration) {
        return expiration.before(new Date());
    }
}
