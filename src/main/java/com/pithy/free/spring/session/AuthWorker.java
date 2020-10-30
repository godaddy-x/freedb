package com.pithy.free.spring.session;

import com.pithy.free.spring.exception.AuthErrorEx;
import com.pithy.free.spring.utils.JsonUtil;
import com.pithy.free.sqlcode.utils.StringUtils;
import com.pithy.free.utils.AES;
import com.pithy.free.utils.HMAC256;
import com.pithy.free.utils.IdWorker;
import org.springframework.util.Base64Utils;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class AuthWorker {

    private AuthConfig config;

    public AuthWorker(AuthConfig config) {
        this.config = config;
    }

    /**
     * 生成token令牌
     *
     * @param userId 用户ID
     * @param secret 用户密钥
     * @return 授权token令牌
     */
    public String createToken(String userId, String secret) throws AuthErrorEx {
        checkAuthConfig();
        if (StringUtils.isEmpty(userId)) {
            outError("用户主体不能为空");
        }
        Subject subject = new Subject();
        subject.setAud(config.getHost());
        subject.setIss(config.getHost());
        subject.setSub(userId);
        subject.setIat(System.currentTimeMillis());
        subject.setExp(subject.getIat() + config.getExpire());
        subject.setRxp(0l);
        subject.setNbf(0l);
        subject.setJti(IdWorker.getSID());
        subject.setExt(new HashMap<>());
        String token = JsonUtil.toJSONString(subject);
        if (StringUtils.isEmpty(token)) {
            outError("生成授权令牌失败");
        }
        String token_b64 = null;
        token_b64 = Base64Utils.encodeToString(token.getBytes(StandardCharsets.UTF_8));
        String token_hmac = HMAC256.create(token_b64, config.getSecret() + secret);
        return token_b64 + "." + token_hmac;
    }

    // 校验token令牌
    public Subject validToken(String token, String secret) throws AuthErrorEx {
        checkAuthConfig();
        if (StringUtils.isEmpty(token)) {
            outError("授权令牌不能为空");
        }
        String[] part = StringUtils.split(token, "\\.");
        if (part == null || part.length != 2) {
            outError("授权令牌格式无效");
        }
        String token_b64 = part[0];
        String token_hmac = HMAC256.create(token_b64, config.getSecret() + secret);
        if (!token_hmac.equals(part[1])) {
            outError("授权令牌校验失败");
        }
        byte[] token_bs = Base64Utils.decode(token_b64.getBytes(StandardCharsets.UTF_8));
        if (token_bs == null || token_bs.length == 0) {
            outError("无效的令牌数据");
        }
        Subject subject = JsonUtil.parseObject(new String(token_bs, StandardCharsets.UTF_8), Subject.class);
        if (subject == null) {
            outError("无法获得令牌数据");
        }
        if (subject.getExp() <= System.currentTimeMillis()) {
            throw new AuthErrorEx(AuthErrorEx.AUTH_OUT_FAIL, "授权令牌已失效");
        }
        return subject;
    }

    // 通过授权令牌生成用户密钥
    public String createSecret(String data) {
        String sha256 = HMAC256.create(data, config.getSecret());
        return sha256.substring(3, 43);
    }

    // AES加密用户密钥
    public String encryptSecret(String data, String secret) {
        String[] split = StringUtils.split(data, "\\.");
        if (split.length != 2) {
            return null;
        }
        String key = HMAC256.create(split[0], split[1]);
        return AES.encrypt(secret, key.substring(4, 44));
    }

    // AES解密用户密钥
    public String decryptSecret(String data, String secret) {
        String[] split = StringUtils.split(data, "\\.");
        if (split.length != 2) {
            return null;
        }
        String key = HMAC256.create(split[0], split[1]);
        return AES.decrypt(secret, key.substring(4, 44));
    }

    private void checkAuthConfig() throws AuthErrorEx {
        if (config == null) {
            outError("授权配置尚未初始化");
        }
        if (StringUtils.isEmpty(config.getHost())) {
            outError("授权配置host为空");
        }
        if (StringUtils.isEmpty(config.getSecret())) {
            outError("授权配置secret为空");
        }
        if (config.getExpire() == null || config.getExpire() < 1800000 || config.getExpire() > 604800000) {
            outError("授权配置expire不能少于30分钟或大于7天");
        }
    }

    private AuthErrorEx outError(String message) throws AuthErrorEx {
        throw new AuthErrorEx(AuthErrorEx.AUTH_FAIL, message);
    }

}
