package com.cat.utils;

import com.cat.chatglm.ChatClient;
import com.cat.common.AppProperties;
import com.cat.common.CommonErrorCode;
import com.cat.common.CommonException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;


public class ChatGLMUtils {

    public static String loadApiKey() {
        return AppProperties.getApiKey();
    }

    public static String sendRequest(String input) {
        if (loadApiKey() == null) {
            throw new CommonException(CommonErrorCode.API_KEY_NOT_FOUND);
        }
        input = "这是一些用户文本，从中总结出用户最近遇到的麻烦或问题，忽略冗余无关的内容，以第一人称回答。\n如果用户的文本中没有表现出任何详细的麻烦或问题，你必须只回答三个字\"没问题\"。\n" + "'''" + input + "'''";
        ChatClient chats = new ChatClient(loadApiKey());
        chats.AsyncInvoke(input);
        return chats.getResponseMessage();
    }

    public static String createJson(String role, String content) {
        JsonObject history = new JsonObject();
        history.addProperty("role", role);
        history.addProperty("content", content);
        Gson gson = new Gson();
        return gson.toJson(history);
    }
}
