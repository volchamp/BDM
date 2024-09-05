package com.kmvc.jeesite.common.utils;

import java.io.Serializable;
import java.security.SecureRandom;
import java.util.UUID;

import com.thinkgem.jeesite.common.utils.Encodes;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
@Lazy(false)
public class IdGen implements SessionIdGenerator {
    private static SecureRandom random = new SecureRandom();

    public IdGen() {
    }

    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static long randomLong() {
        return Math.abs(random.nextLong());
    }

    public static String randomBase62(int length) {
        byte[] randomBytes = new byte[length];
        random.nextBytes(randomBytes);
        return Encodes.encodeBase62(randomBytes);
    }

    public Serializable generateId(Session session) {
        return uuid();
    }

    public static void main(String[] args) {
        System.out.println(uuid());
        System.out.println(uuid().length());

        for(int i = 0; i < 1000; ++i) {
            System.out.println(randomLong() + "  " + randomBase62(5));
        }

    }
}
