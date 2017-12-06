package com.mute.conversion.txt.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mute.conversion.txt.service.TxtService;
import com.mute.conversion.util.OkHttpUtil;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author H
 * @date 2017-12-06
 */
@Service
public class TxtServiceImpl implements TxtService {

    private static final Logger logger = LoggerFactory.getLogger(TxtServiceImpl.class);

    @Value("${mute.get-token-url}")
    private String getTokenUrl;
    @Value("${mute.get-token-params.grant-type}")
    private String grantType;
    @Value("${mute.get-token-params.client-id}")
    private String clientId;
    @Value("${mute.get-token-params.client-secret}")
    private String clientSecret;
    @Value("${mute.get-mp3-url}")
    private String getMp3Url;

    private static String token;

    private static Long getTokenTime = System.currentTimeMillis();

    private static Long tokenWorkTime;

    @Override
    public String getToken() {
        if (token != null && System.currentTimeMillis() - getTokenTime <= tokenWorkTime) {
            return token;
        }
        Map<String, String> params = new HashMap<>(8);
        params.put("grant_type", grantType);
        params.put("client_id", clientId);
        params.put("client_secret", clientSecret);
        Map<String,String> headers = new HashMap<>(4);
        headers.put("Content-Type","application/json");
        Response response = OkHttpUtil.getInstance().doGet(getTokenUrl, params);
        String responseString = null;
        try {
            responseString = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = JSON.parseObject(responseString);
        String accessToken = jsonObject.getString("access_token");
        if (accessToken == null) {
            logger.info("获取access_token失败");
            return token = null;
        }
        token = accessToken;
        getTokenTime = System.currentTimeMillis();
        tokenWorkTime = jsonObject.getLong("expires_in");
        return token;
    }

    @Override
    public String getMp3Url(String txt) {
        String thisToken = getToken();
        if (thisToken == null) {
            return null;
        }
        Map<String, String> params = new HashMap<>(16);
        // 固定值zh。语言选择,目前只有中英文混合模式，填写固定值zh
        params.put("lan", "zh");
        // 客户端类型选择，web端填写固定值1
        params.put("ctp", "1");
        // 用户唯一标识，用来区分用户，计算UV值。建议填写能区分用户的机器 MAC 地址或 IMEI 码，长度为60字符以内
        params.put("cuid", UUID.randomUUID().toString());
        // 开放平台获取到的开发者access_token（见上面的“鉴权认证机制”段落）
        params.put("tok", thisToken);
        // 合成的文本，使用UTF-8编码。小于512个中文字或者英文数字。（文本在百度服务器内转换为GBK后，长度必须小于1024字节）
        try {
            params.put("tex", URLEncoder.encode(txt, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 音量，取值0-15，默认为5中音量
        params.put("vol", "9");
        // 语速，取值0-9，默认为5中语速
        params.put("spd", "3");
        // 发音人选择, 0为普通女声，1为普通男生，3为情感合成-度逍遥，4为情感合成-度丫丫，默认为普通女声
        params.put("per", "0");
        // 音调，取值0-9，默认为5中语调
        params.put("pit", "5");

        return OkHttpUtil.getInstance().getIntactUrl(getMp3Url, params);
    }
}
