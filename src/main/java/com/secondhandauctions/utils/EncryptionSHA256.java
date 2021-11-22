package com.secondhandauctions.utils;

import com.secondhandauctions.vo.MemberVo;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
@Component
@Slf4j
public class EncryptionSHA256 {
    public String encrypt(String planText) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(planText.getBytes());
            byte byteData[] = messageDigest.digest();
            StringBuffer stringBuffer = new StringBuffer();

            for (int i = 0; i < byteData.length; i++) {
                stringBuffer.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }

            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < byteData.length; i++) {
                String hex = Integer.toHexString(0xff & byteData[i]);
                if (hex.length() == 1) {
                    hexString.append(0);
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            log.error("sha256 error", e);
            throw new RuntimeException();
        }
    }

    public void memberSetEncryptionPassword(String target, MemberVo memberVo) throws Exception {
        String encryptionStr = "";

        encryptionStr = encrypt(target);

        memberVo.setMemberPassword(encryptionStr);
    }
}
