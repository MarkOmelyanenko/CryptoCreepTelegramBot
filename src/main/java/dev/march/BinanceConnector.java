package dev.march;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class BinanceConnector {

    private static final String BASE_URL = "https://api.binance.com";
    private OkHttpClient client;
    static int timeToSleep = 1;

    public BinanceConnector() {
        client = new OkHttpClient();
    }

    public String sendRequest(String endpoint) throws Exception {
        handleRateLimit(timeToSleep);

        Request request = new Request.Builder()
                .url(BASE_URL + endpoint)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            return response.body().string();
        }
    }

    public String safeSendRequest(String endpoint) {
        int retryCount = 0;
        while (retryCount < 3) {
            try {
                return sendRequest(endpoint);
            } catch (Exception e) {
                retryCount++;
                System.out.println("Retrying request, attempt: " + retryCount);
            }
        }
        return null;
    }

    private void handleRateLimit(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
