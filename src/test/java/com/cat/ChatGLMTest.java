package com.cat;

import com.cat.chatglm.ChatClient;

import java.util.Scanner;

import static com.cat.utils.ChatGLMUtils.loadApiKey;

public class ChatGLMTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String apiKeyss = loadApiKey();                          //加载 API 密钥

        if (apiKeyss == null) {
            System.out.println("Error: ApiKey not exists!");
            return;
        }

        while (scanner.hasNext()) {
            String userInput = scanner.nextLine();

            ChatClient chats = new ChatClient(apiKeyss);      //初始 ChatClient （实例化）
            chats.AsyncInvoke(userInput);                    //将你输入的问题赋值给流式请求的
            String output = chats.getResponseMessage();
            System.out.println(output); //打印出 ChatGLM 的回答内容
            System.out.println("----回答结束-----");
        }
    }
}
