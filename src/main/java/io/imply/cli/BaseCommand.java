package io.imply.cli;

import io.imply.cli.model.Global;
import org.apache.hc.client5.http.ConnectTimeoutException;
import org.apache.hc.client5.http.SystemDefaultDnsResolver;
import org.apache.hc.client5.http.classic.methods.*;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.entity.mime.FileBody;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.util.Timeout;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;



public abstract class BaseCommand {

    private static final String ENG_URL     = "https://{0}.api.eng.imply.io{1}";
    private static final String STAGING_URL = "https://{0}.api.saas-stg.imply.io{1}";
    private static final String PROD_URL    = "https://{0}.api.imply.io{1}";

    private static final String ENG_ID     = "https://id.eng.imply.io/auth/realms/{0}{1}";
    private static final String STAGING_ID = "https://id.saas-stg.imply.io/auth/realms/{0}{2}";
    private static final String PROD_ID    = "https://id.imply.io/auth/realms/{0}{2}";

    private final Random ran = new Random();
    private final Timeout timeout = Timeout.of(3, TimeUnit.SECONDS);
    private final RequestConfig config = RequestConfig.custom()
            .setConnectTimeout(timeout)
            .setConnectionRequestTimeout(timeout ).build();

    private final HttpClientConnectionManager manager = PoolingHttpClientConnectionManagerBuilder.create()
            .setDnsResolver(new SystemDefaultDnsResolver() {
        @Override
        public InetAddress[] resolve(String host) throws UnknownHostException {
            InetAddress[] addresses = InetAddress.getAllByName(host);
            int i = ran.nextInt(addresses.length);
            return new InetAddress[]{addresses[i]};
        }
    }).build();

    private String buildEndpoint(String env, String org, String path){
        if(isIDRequest(path)){
            if("eng".equals(env)){
                return MessageFormat.format(ENG_ID, org, path);
            }else if("staging".equals(env)){
                return MessageFormat.format(STAGING_ID, org, path);
            }else if("prod".equals(env)){
                return MessageFormat.format(PROD_ID, org, path);
            }
        }else{
            if("eng".equals(env)){
                return MessageFormat.format(ENG_URL, org, path);
            }else if("staging".equals(env)){
                return MessageFormat.format(STAGING_URL, org, path);
            }else if("prod".equals(env)){
                return MessageFormat.format(PROD_URL, org, path);
            }
        }
        throw new IllegalArgumentException("Unknown env:" + env);
    }

    private boolean isIDRequest(String path){
        return "/protocol/openid-connect/token".equals(path) || path.startsWith("/apikeys");
    }

    public String getRequest(String path, Global global) throws IOException {
        return service("GET", null, path, global);
    }

    public String deleteRequest(String path, Global global) throws IOException {
        return service("DELETE", null, path, global);
    }

    public String patchRequest(String content, String path, Global global) throws IOException {
        StringEntity requestEntity = new StringEntity(
                content,
                ContentType.APPLICATION_JSON);
        return service("PATCH", requestEntity, path, global);
    }

    public String putJSON(String content, String path, Global global) throws IOException {
        StringEntity requestEntity = new StringEntity(
                content,
                ContentType.APPLICATION_JSON);
        return service("PUT",requestEntity, path, global);
    }

    public String postJson(String content, String path, Global global) throws IOException {
        StringEntity requestEntity = new StringEntity(
                content,
                ContentType.APPLICATION_JSON);
        return service("POST",requestEntity, path, global);
    }

    public String upload(File file, String path, Global global) throws IOException{
        FileBody fileBody = new FileBody(file);
        HttpEntity reqEntity = MultipartEntityBuilder.create()
                .addPart("bin", fileBody)
                .build();
        return service("POST", reqEntity, path, global);
    }


    private String service(String method, HttpEntity reqEntity, String path, Global global) throws IOException{
        try( CloseableHttpClient httpclient = HttpClientBuilder.create()
                .setDefaultRequestConfig(config)
                .setConnectionManager(manager)
                .build()){
            String url = buildEndpoint(global.environment.name(), global.organization, path);

            HttpUriRequestBase baseRequest;
            if("POST".equals(method)){
                baseRequest = new HttpPost(url);
            }else if("PUT".equals(method)){
                baseRequest = new HttpPut( url);
            }else if("GET".equals(method)){
                baseRequest = new HttpGet(url);
            }else if("DELETE".equals(method)){
                baseRequest = new HttpDelete(url);
            }else if("PATCH".equals(method)){
                baseRequest = new HttpPatch(url);
            }else{
                throw new IllegalArgumentException("Unknown http method: " + method);
            }

            if(global.authorization == Global.Authorization.token || isIDRequest(path)){
                baseRequest.setHeader("Authorization", "Bearer " + global.authSection.token);
            }else{
                baseRequest.setHeader("Authorization", "Basic " + global.authSection.apiKey);
            }
            if (reqEntity !=null ){
                baseRequest.setHeader("Accept", "application/json");
                baseRequest.setEntity(reqEntity);
            }

            if(global.verbose){
                print(baseRequest);
            }
            try(CloseableHttpResponse httpresponse = httpclient.execute(baseRequest)){
                HttpEntity entity = httpresponse.getEntity();
                String respEntity = null;
                if (entity !=null){
                    respEntity = EntityUtils.toString(entity);
                }
                if(global.verbose){
                    printResponse(httpresponse, respEntity);
                }
                return respEntity;
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (ConnectTimeoutException e){
                return e.getMessage();
            }
            return "Unknown error during request";
        }
    }

    public String retrieveToken(String env, String org, String path, String clientId, String clientSecret, boolean verbose) throws IOException {
        String url = buildEndpoint(env, org, path);

        try(CloseableHttpClient client = HttpClientBuilder.create()
                .setDefaultRequestConfig(config)
                .setConnectionManager(manager)
                .build()){
            HttpPost httpPost = new HttpPost(url );
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("client_id", clientId));
            params.add(new BasicNameValuePair("client_secret", clientSecret));
            params.add(new BasicNameValuePair("grant_type", "client_credentials"));
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            if(verbose){
                print(httpPost);
            }

            try(CloseableHttpResponse response = client.execute(httpPost)){
                HttpEntity entity = response.getEntity();
                String result = EntityUtils.toString(entity);
                if(verbose){
                    printResponse(response, result);
                }
                JSONObject obj = new JSONObject(result);
                return obj.getString("access_token");
            } catch (ParseException e){
                e.printStackTrace();
            }
        }
        return "Unknown error during retrieve token";
    }

    private void print(HttpUriRequest request){
        System.out.println("---http request begin---");
        try {
            System.out.println("    "+request.getMethod() + " " + request.getUri());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        printHeaders(request.getHeaders());
        HttpEntity entity = request.getEntity();
        if(entity!=null){
            try {
                String s = EntityUtils.toString(entity);
                System.out.println("    "+ s);
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }
        System.out.println("---http request end---");
    }

    private void printHeaders(Header[] headers){
        for(Header h: headers){
            System.out.println("    "+h.getName() + ":" + h.getValue());
        }
    }

    private void printResponse(CloseableHttpResponse response, String result){
        System.out.println("---http response begin---");
//        printHeaders(response.getHeaders());
        System.out.println("    Status Code:" + response.getCode());
        System.out.println("    Response Body:" + result);
        System.out.println("---http response end---");
    }

}
