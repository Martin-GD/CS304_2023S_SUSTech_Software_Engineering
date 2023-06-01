package com.team127.atom.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;
import io.swagger.models.auth.In;
import lombok.*;

import java.util.HashMap;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
@ToString
public class Response {
    private Integer code;

    private String message;

    @JSONField(name="Content-Type")
    private String contentType;

    private Object data;

    public Response(Integer code, String message, Object data){
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static Response success(Object data) {
        return new Response(200, "Success", data);
    }

    public static Response success(String contentType, Object data) {
        Response r = new Response(200, "Success", data);
        r.setContentType("image/png");
        return r;
    }

    public static Response error(Integer code, String message) {
        return new Response(code, message, null);
    }
}