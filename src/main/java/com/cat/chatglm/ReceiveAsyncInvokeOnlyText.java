package com.cat.chatglm;

import java.util.concurrent.CompletableFuture;

public class ReceiveAsyncInvokeOnlyText {

    private String responseAsyncMessage;
    private String DefaultUrl = "https://open.bigmodel.cn/api/paas/v4/async/chat/completions";
    private String AsyncInvokeCheckUrl = "https://open.bigmodel.cn/api/paas/v4/async-result/";
    private boolean cat;
    private String history;

    public ReceiveAsyncInvokeOnlyText(String token, String message) {
        sendRequestAndWait(token, message, DefaultUrl);
    }

    public ReceiveAsyncInvokeOnlyText(String token, String message, boolean cat, String history) {
        this.cat = cat;
        this.history = history;
        sendRequestAndWait(token, message, DefaultUrl);
    }

    private void sendRequestAndWait(String token, String message, String urls) {

        AsyncInvokeModel asyncInvokeModel = new AsyncInvokeModel();
        CompletableFuture<String> result;
        if(cat)
            result = asyncInvokeModel.asyncRequest(token, message, urls, AsyncInvokeCheckUrl, cat, history);
        else
            result = asyncInvokeModel.asyncRequest(token, message, urls, AsyncInvokeCheckUrl);
        result.thenAccept(response -> {
            responseAsyncMessage = asyncInvokeModel.getContentMessage();
        }).exceptionally(ex -> {
            System.err.println("Error: " + ex.getMessage());
            return null;
        });
        result.join();
    }

    public String getReponse() {
        return responseAsyncMessage;
    }
}
