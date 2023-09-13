package ru.researchser.utils;

import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CryptoUtil {

    private final Hashids hashids;

    public CryptoUtil(String salt) {
        int minHashLength = 10;
        this.hashids = new Hashids(salt, minHashLength);
    }

    public String hashOf(Long value) {
        return hashids.encode(value);
    }

    public Long idOf(String value) {
        long[] res = hashids.decode(value);
        if (res != null && res.length > 0) {
            return res[0];
        }
        return null;
    }
}
