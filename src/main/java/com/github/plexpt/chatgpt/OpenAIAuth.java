package com.github.plexpt.chatgpt;

import com.alibaba.fastjson2.JSON;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.Data;
import okhttp3.Response;

@Data
public class OpenAIAuth {

    private String sessionToken;
    private String emailAddress;
    private String password;
    private boolean useProxy;
    private String proxy;
    private Session session;
    private String accessToken;


    public OpenAIAuth(String emailAddress, String password, boolean useProxy, String proxy) {
        this.sessionToken = null;
        this.emailAddress = emailAddress;
        this.password = password;
        this.useProxy = useProxy;
        this.proxy = proxy;
        this.session = new Session();
        this.accessToken = null;
    }

    // URL encode a string
    public static String urlEncode(String string) {
        return java.net.URLEncoder.encode(string);
    }

    public void begin() {
        if (this.emailAddress == null || this.password == null) {
            return;
        }

        if (this.useProxy) {
            if (this.proxy == null) {
                return;
            }

            Map<String, String> proxies = new HashMap<String, String>() {{
                put("http", proxy);
                put("https", proxy);
            }};

            this.session.setProxies(proxies);
        }

        String url = "https://chat.openai.com/auth/login";
        Map<String, String> headers = new HashMap<>();
        headers.put("Host", "ask.openai.com");
        headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        headers.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) " +
                "AppleWebKit/605.1" +
                ".15 (KHTML, like Gecko) Version/16.1 Safari/605.1.15");
        headers.put("Accept-Language", "en-GB,en-US;q=0.9,en;q=0.8");
        headers.put("Accept-Encoding", "gzip, deflate, br");
        headers.put("Connection", "keep-alive");

        Response response = this.session.get(url, headers);
        if (response.code() == 200) {
            this.partTwo();
        } else {
            throw new RuntimeException("Error logging in");
        }
    }


    private void partTwo() {
        String url = "https://chat.openai.com/api/auth/csrf";
        Map<String, String> headers = new HashMap<>();
        headers.put("Host", "ask.openai.com");
        headers.put("Accept", "*/*");
        headers.put("Connection", "keep-alive");
        headers.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) " +
                "AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.1 Safari/605.1.15");
        headers.put("Accept-Language", "en-GB,en-US;q=0.9,en;q=0.8");
        headers.put("Referer", "https://chat.openai.com/auth/login");
        headers.put("Accept-Encoding", "gzip, deflate, br");

        String response = session.getString(url, headers);
        String csrfToken = JSON.parseObject(response).getString("csrfToken");
        System.out.println("csrfToken: " + csrfToken);
        partThree(csrfToken);
//        Response response = session.get(url, headers);

//        if (response.code() == 200 && response.header("Content-Type").contains("json")) {
////            System.out.println(response.body().toString());
//            String csrfToken = Chatbot.resJson(response).getString("csrfToken");
//            partThree(csrfToken);
//        } else {
//            throw new RuntimeException("Error logging in");
//        }
    }


    public void partThree(String token) {
        String url = "https://chat.openai.com/api/auth/signin/auth0?prompt=login";
        String payload = "callbackUrl=%2F&csrfToken=" + token + "&json=true";
        Map<String, String> headers = new HashMap<>();
        headers.put("Host", "ask.openai.com");
        headers.put("Origin", "https://chat.openai.com");
        headers.put("Connection", "keep-alive");
        headers.put("Accept", "*/*");
        headers.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) " +
                "AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.1 Safari/605.1.15");
        headers.put("Referer", "https://chat.openai.com/auth/login");
        headers.put("Content-Length", "100");
        headers.put("Accept-Language", "en-GB,en-US;q=0.9,en;q=0.8");
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        Response response = session.post(url, headers, payload);
        if (response.code() == 200 && response.headers().names().contains("Content-Type") &&
                response.header("Content-Type").contains("json")) {

            String url2 = JSON.parseObject(response.toString()).getString("url");
            partFour(url2);
        } else if (response.code() == 400) {
            throw new RuntimeException("Invalid credentials");
        } else {
            throw new RuntimeException("Unknown error");
        }
    }

    public void partFour(String url) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Host", "auth0.openai.com");
        headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        headers.put("Connection", "keep-alive");
        headers.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) " +
                "AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.1 Safari/605.1.15");
        headers.put("Accept-Language", "en-US,en;q=0.9");
        headers.put("Referer", "https://chat.openai.com/");
        Response response = session.get(url, headers);
        if (response.code() == 302) {
            String state =
                    Pattern.compile("state=(.*)").matcher(response.body().toString()).group(1);
            state = state.split("\"")[0];
            partFive(state);
        } else {
            throw new RuntimeException("Unknown error");
        }
    }

    public void partFive(String state) {
        String url = String.format("https://auth0.openai.com/u/login/identifier?state=%s", state);
        Map<String, String> headers = new HashMap<>();
        headers.put("Host", "auth0.openai.com");
        headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        headers.put("Connection", "keep-alive");
        headers.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) " +
                "AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.1 Safari/605.1.15");
        headers.put("Accept-Language", "en-US,en;q=0.9");
        headers.put("Referer", "https://chat.openai.com/");
        Response response = session.get(url, headers);
        if (response.code() == 200) {
            if (Pattern.compile("<img[^>]+alt=\"captcha\"[^>]+>").matcher(response.body().toString()).find()) {
                System.out.println("Captcha detected");
                throw new RuntimeException("Captcha detected");
            }
            partSix(state, null);
        } else {
            throw new RuntimeException("Invalid response code");
        }
    }

    public void partSix(String state, String captcha) {
        String url = String.format("https://auth0.openai.com/u/login/identifier?state=%s", state);
        String emailUrlEncoded = urlEncode(emailAddress);
        String payload = String.format("state=%s&username=%s&captcha=%s&js-available=true" +
                "&webauthn-available=true&is-brave=false&webauthn-platform-available=true&action" +
                "=default ", state, emailUrlEncoded, captcha);
        if (captcha == null) {
            payload = String.format("state=%s&username=%s&js-available=false&webauthn-available" +
                            "=true&is-brave=false&webauthn-platform-available=true&action=default ",
                    state, emailUrlEncoded);
        }
        Map<String, String> headers = new HashMap<>();
        headers.put("Host", "auth0.openai.com");
        headers.put("Origin", "https://auth0.openai.com");
        headers.put("Connection", "keep-alive");
        headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        headers.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) " +
                "AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.1 Safari/605.1.15");
        headers.put("Referer", String.format("https://auth0.openai" +
                ".com/u/login/identifier?state=%s", state));
        headers.put("Accept-Language", "en-US,en;q=0.9");
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        Response response = session.post(url, headers, payload);
        if (response.code() == 302) {
            partSeven(state);
        } else {
            throw new RuntimeException("Unknown error");
        }
    }

    public void partSeven(String state) {
        String url = String.format("https://auth0.openai.com/u/login/password?state=%s", state);
        String emailUrlEncoded = urlEncode(emailAddress);
        String passwordUrlEncoded = urlEncode(password);
        String payload = String.format("state=%s&username=%s&password=%s&action=default", state,
                emailUrlEncoded, passwordUrlEncoded);
        Map<String, String> headers = new HashMap<>();
        headers.put("Host", "auth0.openai.com");
        headers.put("Origin", "https://auth0.openai.com");
        headers.put("Connection", "keep-alive");
        headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        headers.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) " +
                "AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.1 Safari/605.1.15");
        headers.put("Referer", String.format("https://auth0.openai.com/u/login/password?state=%s"
                , state));
        headers.put("Accept-Language", "en-US,en;q=0.9");
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        Response response = session.post(url, headers, payload);
        boolean is302 = response.code() == 302;
        if (is302) {
            String newStateArray = response.body().toString().split("state=")[0].split("\"")[0];
            partEight(state, newStateArray);
        } else {
            throw new RuntimeException("Unknown error");
        }
    }

    private void partEight(String oldState, String newState) {
        String url = "https://auth0.openai.com/authorize/resume?state=" + newState;
        Map<String, String> headers = new HashMap<>();
        headers.put("Host", "auth0.openai.com");
        headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        headers.put("Connection", "keep-alive");
        headers.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) " +
                "AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.1 Safari/605.1.15");
        headers.put("Accept-Language", "en-GB,en-US;q=0.9,en;q=0.8");
        headers.put("Referer", "https://auth0.openai.com/u/login/password?state=" + oldState);

        Response response = session.get(url, headers, true);
        boolean is200 = response.code() == 200;
        if (is200) {
            // Access Token
            Pattern pattern = Pattern.compile("accessToken\":\"(.*)\"");
            Matcher matcher = pattern.matcher(response.body().toString());

            // Access Token
            if (matcher.find()) {

                // Save access_token and an hour from now on ./classes/auth.json
                saveAccessToken(matcher.group());
            } else {
                System.out.println("Invalid credentials");
                throw new RuntimeException("Invalid credentials");
            }
        } else {
            System.out.println("Invalid credentials");
            throw new RuntimeException("Failed to find accessToken");
        }
    }

    public void saveAccessToken(String accessToken) {
        if (partNine()) {
            this.accessToken = accessToken;
        } else {
            System.out.println("Failed to login");
            throw new RuntimeException("Failed to login");
        }
    }


    public boolean partNine() {
        String url = "https://auth0.openai.com/user/ssodata";
        Map<String, String> headers = new HashMap<>();
        headers.put("Host", "auth0.openai.com");
        headers.put("Connection", "keep-alive");
        headers.put("Accept", "application/json, text/javascript, */*; q=0.01");
        headers.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) " +
                "AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.1 Safari/605.1.15");
        headers.put("Referer", "https://auth0.openai.com/authorize/resume");
        headers.put("Accept-Language", "en-GB,en-US;q=0.9,en;q=0.8");
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        Response response = this.session.get(url, headers);
        boolean is200 = response.code() == 200;
        if (is200) {
            return true;
        } else {
            return false;
        }
    }


}
