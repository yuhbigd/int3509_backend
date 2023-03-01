package com.project.nhatrotot.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.project.nhatrotot.rest.advice.CustomException.GeneralException;

@Service
public class PaymentUtil {
    @Value("${app.properties.vnp.vnp_HashSecret}")
    private String vnp_HashSecret;
    @Value("${app.properties.vnp.vnp_ReturnUrl}")
    private String vnp_ReturnUrl;
    @Value("${app.properties.vnp.vnp_redirectURL}")
    private String vnp_RedirectURL;

    public String getVnp_RedirectURL() {
        return vnp_RedirectURL;
    }

    public String getVnp_ReturnUrl() {
        return vnp_ReturnUrl;
    }

    public String createSHA512(String data) {
        String hmac = new HmacUtils("hmacSHA512", vnp_HashSecret).hmacHex(data);
        return hmac;
    }

    public String createUrlValueFromMap(Map<String, String> map) {
        try {
            List<String> fieldNames = new ArrayList<>(map.keySet());
            Collections.sort(fieldNames);
            StringBuilder hashData = new StringBuilder();
            Iterator<String> itr = fieldNames.iterator();
            while (itr.hasNext()) {
                String fieldName = (String) itr.next();
                String fieldValue = (String) map.get(fieldName);
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    // Build hash data
                    hashData.append(fieldName);
                    hashData.append('=');

                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    if (itr.hasNext()) {
                        hashData.append('&');
                    }
                }
            }
            return hashData.toString();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            throw new GeneralException("Can not create Hash value for vnp");
        }
    }
}
