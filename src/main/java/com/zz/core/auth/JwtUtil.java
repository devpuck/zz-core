package com.zz.core.auth;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.zz.core.util.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;

/**
 * JWT工具类
 * https://github.com/auth0/java-jwt
 **/
@Slf4j
@Component
public class JwtUtil
{

    /**
     * JWT用户名
     */
    private static final String JWT_USERNAME = "username";

    private static com.zz.core.auth.JwtProperties jwtProperties;

    public JwtUtil(com.zz.core.auth.JwtProperties jwtProperties)
    {
        JwtUtil.jwtProperties = jwtProperties;
        log.info(JSON.toJSONString(JwtUtil.jwtProperties));
    }

    /**
     * 生成JWT Token
     *
     * @param user           用户信息
     * @param salt           盐值
     * @param expireDuration 过期时间和单位
     * @return token
     */
    public static String generateToken(String user, String salt, Duration expireDuration)
    {
        try
        {
            if (StringUtils.isBlank(user))
            {
                log.error("username不能为空");
                return null;
            }
            log.debug("username:{}", user);

            // 如果盐值为空，则使用默认值：666666
            if (StringUtils.isBlank(salt))
            {
                salt = jwtProperties.getSecret();
            }
            log.debug("salt:{}", salt);

            // 过期时间，单位：秒
            Long expireSecond;
            // 默认过期时间为1小时
            if (expireDuration == null)
            {
                expireSecond = jwtProperties.getExpireSecond();
            }
            else
            {
                expireSecond = expireDuration.getSeconds();
            }
            log.debug("expireSecond:{}", expireSecond);
            Date expireDate = DateUtils.addSeconds(new Date(), expireSecond.intValue());
            log.debug("expireDate:{}", expireDate);

            // 生成token
            Algorithm algorithm = Algorithm.HMAC256(salt);
            String token = JWT.create()
                    .withClaim(JWT_USERNAME, user)
                    .withJWTId(UUIDUtil.getUUID())              // jwt唯一id
                    .withIssuer(jwtProperties.getIssuer())      // 签发人
                    .withSubject(jwtProperties.getSubject())    // 主题
                    .withAudience(jwtProperties.getAudience())  // 签发的目标
                    .withIssuedAt(new Date())                   // 签名时间
                    .withExpiresAt(expireDate)                  // token过期时间
                    .sign(algorithm);                           // 签名
            return token;
        } catch (Exception e)
        {
            log.error("generateToken exception", e);
        }
        return null;
    }

    public static boolean verifyToken(String token, String salt)
    {
        try
        {
            Algorithm algorithm = Algorithm.HMAC256(salt);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(jwtProperties.getIssuer())      // 签发人
                    .withSubject(jwtProperties.getSubject())    // 主题
                    .withAudience(jwtProperties.getAudience())  // 签发的目标
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            if (jwt != null)
            {
                return true;
            }
        } catch (Exception e)
        {
            log.error("Verify Token Exception", e);
        }
        return false;
    }

    /**
     * 解析token，获取token数据
     *
     * @param token
     * @return
     */
    public static DecodedJWT getJwtInfo(String token)
    {
        DecodedJWT decodedJWT = JWT.decode(token);
        return decodedJWT;
    }

    /**
     * 获取用户名
     *
     * @param token
     * @return
     */
    public static String getUsername(String token)
    {
        if (StringUtils.isBlank(token))
        {
            return null;
        }
        DecodedJWT decodedJWT = getJwtInfo(token);
        if (decodedJWT == null)
        {
            return null;
        }
        String username = decodedJWT.getClaim(JWT_USERNAME).asString();
        return username;
    }

    /**
     * 获取用户，因为之前框架使用来 JWT_USERNAME，后期熟悉框架后在进行改造
     * @param token
     * @return
     */
    public static String getUser(String token)
    {
        if (StringUtils.isBlank(token))
        {
            return null;
        }
        DecodedJWT decodedJWT = getJwtInfo(token);
        if (decodedJWT == null)
        {
            return null;
        }
        String username = decodedJWT.getClaim(JWT_USERNAME).asString();
        return username;
    }

    /**
     * 获取创建时间
     *
     * @param token
     * @return
     */
    public static Date getIssuedAt(String token)
    {
        DecodedJWT decodedJWT = getJwtInfo(token);
        if (decodedJWT == null)
        {
            return null;
        }
        return decodedJWT.getIssuedAt();
    }

    /**
     * 获取过期时间
     *
     * @param token
     * @return
     */
    public static Date getExpireDate(String token)
    {
        DecodedJWT decodedJWT = getJwtInfo(token);
        if (decodedJWT == null)
        {
            return null;
        }
        return decodedJWT.getExpiresAt();
    }

    /**
     * 判断token是否已过期
     *
     * @param token
     * @return
     */
    public static boolean isExpired(String token)
    {
        Date expireDate = getExpireDate(token);
        if (expireDate == null)
        {
            return true;
        }
        return expireDate.before(new Date());
    }

}
