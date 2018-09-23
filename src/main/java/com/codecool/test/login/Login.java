package com.codecool.test.login;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.*;
import java.net.HttpCookie;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Login implements HttpHandler {

    private Map<String, String> session = new HashMap<>();

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        String cookieStr = httpExchange.getRequestHeaders().getFirst("Cookie");
        String response = "";


        if (method.equals("GET")) {
            String sessionId = null;

            if (cookieStr != null) {
                sessionId = HttpCookie.parse(cookieStr).get(0).getValue();
            }

            if (sessionId != null && session.containsKey(sessionId)) {
                response = getAccountPage(session.get(sessionId));
            } else {
                response = getLoginPage("");
            }
        }

        if (method.equals("POST")) {
            InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(isr);
            String formData = br.readLine();
            Map<String, String> inputs = parseFormData(formData);

            if (inputs.containsKey("log_out")) {
                session.remove(HttpCookie.parse(cookieStr).get(0).getValue());
                response = getLoginPage("");

            } else if (isPasswordCorrect(inputs)) {
                String userName = inputs.get("userName");
                String uniqueID = UUID.randomUUID().toString();

                session.put(uniqueID, userName);

                HttpCookie cookie = new HttpCookie("sessionId", uniqueID);
                httpExchange.getResponseHeaders().add("Set-Cookie", cookie.toString());

                response = getAccountPage(userName);

            } else {
                response = getLoginPage("Incorrect login or password");
            }

        }

        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();

    }

    private String getLoginPage(String errMessage) {
        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/login.twig");

        JtwigModel model = JtwigModel.newModel();
        model.with("errMessage", errMessage);

        return template.render(model);
    }

    private String getAccountPage(String userName) {
        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/account.twig");

        JtwigModel model = JtwigModel.newModel();
        model.with("userName", userName);

        return template.render(model);
    }

    private Map<String, String> parseFormData(String formData) throws UnsupportedEncodingException {
        Map<String, String> map = new HashMap<>();
        String[] pairs = formData.split("&");
        for(String pair : pairs){
            String[] keyValue = pair.split("=");
            String value = URLDecoder.decode(keyValue[1], "UTF-8");
            map.put(keyValue[0], value);
        }
        return map;
    }

    private boolean isPasswordCorrect(Map<String, String> inputs) {
        String userName = inputs.get("userName");
        String pass = inputs.get("pass");
        LoginDAO dao = new LoginDAOFactory().getDao();
        return pass.equals(dao.getPasswordByLogin(userName));
    }
}
