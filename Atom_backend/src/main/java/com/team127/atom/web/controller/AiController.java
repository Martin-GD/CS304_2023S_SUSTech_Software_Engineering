package com.team127.atom.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import io.github.asleepyfish.util.OpenAiUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.team127.atom.model.Response;
import com.team127.atom.model.User;
import com.team127.atom.service.UserService;
import com.team127.atom.util.JWTUtil;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin
@Slf4j
public class AiController {

    @Autowired
    private UserService userService;

    @Resource
    RedisTemplate<String, Object> redisTemplate;

    public int queryUserChance(String id) {
        if (Boolean.FALSE.equals(redisTemplate.hasKey(id))) {
            redisTemplate.opsForValue().set(id, 9, 1, TimeUnit.DAYS);
            return 9;
        } else if ((Integer) redisTemplate.opsForValue().get(id) > 0) {
            int chance_left = (Integer) redisTemplate.opsForValue().get(id) - 1;
            redisTemplate.opsForValue().set(id, chance_left);
            return chance_left;
        } else {
            return 0;
        }
    }

    @PostMapping("/chat")
    public Response chat(@RequestParam("token") String token, @RequestBody String content) {
        User user = userService.getUserById(JWTUtil.getUserId(token));
        if (!JWTUtil.verify(token, user.getId(), user.getPassword())) {
            return Response.error(1004, "Token verification failed");
        }

        try {
            int chance_left = queryUserChance(user.getId());
            if (chance_left == 0) {
                return Response.error(1005, "Your chances for today has been used up, please come back tomorrow");
            } else {
                List<String> response = OpenAiUtils.createChatCompletion(content, user.getId());
                JSONObject jsonObject = JSONUtil.createObj().putOnce("chance_left", chance_left).putOnce("response",
                        response.get(0));
                return Response.success(jsonObject.toStringPretty());
            }
        } catch (Exception e) {
            return Response.error(1005, "Chat failed");
        }
    }


}
