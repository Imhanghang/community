package com.gonghang.community.provider;


import com.alibaba.fastjson.JSON;
import com.gonghang.community.DTO.AccessTokenDTO;
import com.gonghang.community.DTO.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component //使用这个注解后，下面的类就自动进入了IOC容器，要使用实例化对象就不需要new，直接可以使用
public class GithubProvider {
    public String getAccessToken(AccessTokenDTO  accessTokenDTO)
    {
        MediaType  mediaType = MediaType.get("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {

            String token = response.body().string().split("&")[0].split("=")[1];
            System.out.println("token: " + token);
            return token;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public GithubUser getUser(String accessToken){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token=" + accessToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            GithubUser githubUser = JSON.parseObject(string, GithubUser.class);
            return githubUser;
        } catch (IOException e) {
        }
        return null;
    }
}
