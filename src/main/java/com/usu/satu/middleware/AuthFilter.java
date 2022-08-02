package com.usu.satu.middleware;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.usu.satu.dto.UserJWT;
import com.usu.satu.helper.ResponseBody;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.invoke.StringConcatException;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;

@Component
public class AuthFilter implements Filter {

    private static final Logger logger = LogManager.getLogger(AuthFilter.class);

    public AuthFilter() {
        logger.info("authFilter init");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;

        logger.info("hit : " + req.getMethod() + " - " + req.getRequestURL() + " - " + req.getHeader("Origin"));

        // Authorize (allow) all domains to consume the content
//        res.setHeader("Access-Control-Allow-Origin", "*");
//        res.setHeader("Access-Control-Allow-Credentials", "false");
//        res.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
//        res.setHeader("Access-Control-Max-Age", "4800");
//        res.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, remember-me");

        // For HTTP OPTIONS verb/method reply with ACCEPTED status code -- per CORS handshake
//        if (req.getMethod().equals("OPTIONS")) {
//            res.setStatus(HttpServletResponse.SC_ACCEPTED);
//            logger.info("authFilter CORSFilter HTTP Request Options - " + res.getStatus());
//            return;
//        }

//        if(res.getHeader("Access-Control-Allow-Origin") == null || res.getHeader("Origin") == null){
//            res.setStatus(200);
//            logger.info("authFilter : res getHeader - " + req.getHeader("Origin") + " - " + req.getHeader("Access-Control-Allow-Origin") + " - " + res.getStatus());
//        }

        try {
            logger.info("authFilter: try");
            if (!req.getHeaders("Authorization").hasMoreElements()) {
                logger.info("authFilter: if Authorization");
                if (req.getRequestURI().contains("/files")) {
                    filterChain.doFilter(req, res);
                } else if (req.getRequestURI().contains("/sia") || req.getRequestURI().contains("/data") ||
                        req.getRequestURI().contains("/lecture") || req.getRequestURI().contains("/billing")) {
                    filterChain.doFilter(req, res);
                } else {
                    throw new StringConcatException("Please provide authorization header");
                }
            }
            else {
                logger.info("authFilter: Else Authorization");
                Enumeration<String> authorization = req.getHeaders("Authorization");
                String header = authorization.nextElement();
                String[] parts = header.split(" ");
                String token = parts[1];
                Boolean validateJwt = this.validateJwt(res, token);
                logger.info("authFilter: validateJwt " + validateJwt);
                UserJWT userdata = new UserJWT(getValueJwt(token).getJSONObject("payload"));
//                JSONObject usr = new JSONObject();
//                usr.put("user_id", "9748");
//                usr.put("identity", "96122221022001");
//                usr.put("name", "Afzalurrahmah");
//                usr.put("email", "afzalurrahmah@usu.ac.id");
//                UserJWT userdata = new UserJWT(usr);

                if (validateJwt) {
                    req.setAttribute("userdata", userdata);
                    req.setAttribute("ssoToken", token);
                    filterChain.doFilter(req, res);
                } else {
                    logger.info("authFilter: else validateJwt " + validateJwt);
//                    filterChain.doFilter(req, res);
                }
            }
        }catch (Exception e){
            this.responseData(res,e.getMessage());
        }
    }

    private JSONObject getValueJwt(String token) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String data = restTemplate.exchange("https://akun.usu.ac.id/auth/listen/" + token, HttpMethod.GET, entity, String.class).getBody();

        return new JSONObject(data);
    }

    private Boolean validateJwt(HttpServletResponse res, String token) throws IOException {
        Date date = new Date();
        JSONObject payload = getValueJwt(token);
        if(!payload.has("logged_in"))
        {
            this.responseData(res,"You Have been logged out, Please Login");
            return false;
        }

        if(!payload.getJSONObject("payload").getString("iss").equalsIgnoreCase("akun.usu.ac.id")){
            this.responseData(res,"Issuer is unauthorized");
            return false;
        }

        if(date.getTime()/1000 > Integer.parseInt(payload.getJSONObject("payload").get("exp").toString()))
        {
            this.responseData(res,"Token Expired");
            return false;
        }
        return true;
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    private void responseData(HttpServletResponse res, String responseMessage) throws IOException {
        res.resetBuffer();
        res.setStatus(403);
        res.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");

        res.getOutputStream().print(new ObjectMapper().writeValueAsString(new ResponseBody().unAuth(responseMessage, null)));
        res.flushBuffer();
    }

}
