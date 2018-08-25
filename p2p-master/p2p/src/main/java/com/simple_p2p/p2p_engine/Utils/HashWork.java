package com.simple_p2p.p2p_engine.Utils;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashWork {

    private static HashFunction fileBodyHashFunction = Hashing.sha1();

    private HashWork() {
    }

    public static String giveMyHash() {
        byte[] part1 = NetworkEnvironment.getLocalAddress().getAddress();
        byte[] part2 = NetworkEnvironment.getLocalMacAddress();
        byte[] part3 = System.getProperty("user.name").getBytes();
        byte[] resultBytes = new byte[part1.length + part2.length + part3.length];
        System.arraycopy(part1, 0, resultBytes, 0, part1.length);
        System.arraycopy(part2, 0, resultBytes, part1.length - 1, part2.length);
        System.arraycopy(part3, 0, resultBytes, part1.length + part2.length - 1, part3.length);
        String result = toSHA1(resultBytes);
        return result;
    }

    public static String getUserHash(String name, String password){
        byte[] part1 = name.getBytes(Charset.forName("UTF8"));
        byte[] part2 = password.getBytes(Charset.forName("UTF8"));
        byte[] resultBytes = new byte[part1.length + part2.length];
        System.arraycopy(part1, 0, resultBytes, 0, part1.length);
        System.arraycopy(part2, 0, resultBytes, part1.length - 1, part2.length);
        String result = toSHA1(resultBytes);
        return result;
    }

    public static String toSHA1(byte[] resultBytes){
        return fileBodyHashFunction.newHasher().putBytes(resultBytes).hash().toString();
    }


}
