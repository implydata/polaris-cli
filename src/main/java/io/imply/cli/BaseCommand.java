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

    private static final String ENG_URL     = "https://{0}.api.eng.imply.io";
    private static final String STAGING_URL = "https://{0}.api.saas-stg.imply.io";
    private static final String PROD_URL    = "https://{0}.api.imply.io";

    private static final String ENG_ID     = "https://id.eng.imply.io/auth/realms/{0}/protocol/openid-connect/token";
    private static final String STAGING_ID = "https://id.saas-stg.imply.io/auth/realms/{0}/protocol/openid-connect/token";
    private static final String PROD_ID    = "https://id.imply.io/auth/realms/{0}/protocol/openid-connect/token";

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

    private String buildEndpoint(String env, String org){
        if("eng".equals(env)){
            return MessageFormat.format(ENG_URL, org);
        }else if("staging".equals(env)){
            return MessageFormat.format(STAGING_URL, org);
        }else if("prod".equals(env)){
            return MessageFormat.format(PROD_URL, org);
        }
        throw new IllegalArgumentException("Unknown env:" + env);
    }

    public String getRequest(String path, Global global) throws IOException {
        try( CloseableHttpClient httpclient = HttpClientBuilder.create()
                .setDefaultRequestConfig(config)
                .setConnectionManager(manager)
                .build()){
            String url = buildEndpoint(global.environment.name(), global.organization);
            HttpGet httpget = new HttpGet(url + path);
            httpget.setHeader("Authorization", "Bearer " + global.token);
            if(global.verbose){
                print(httpget);
            }
            try(CloseableHttpResponse httpresponse = httpclient.execute(httpget)){
                HttpEntity entity = httpresponse.getEntity();
                String respEntity = EntityUtils.toString(entity);
                if(global.verbose){
                    printResponse(httpresponse);
                }
                return respEntity;
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (ConnectTimeoutException e){
                return e.getMessage();
            }
            return "Unknown error during get";
        }
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
            String uri = buildEndpoint(global.environment.name(), global.organization);

            HttpUriRequestBase baseRequest;
            if("POST".equals(method)){
                baseRequest = new HttpPost(uri + path);
            }else if("PUT".equals(method)){
                baseRequest = new HttpPut( uri + path);
            }else{
                throw new IllegalArgumentException("Unknown http method: " + method);
            }

            baseRequest.setHeader("Authorization", "Bearer " + global.token);
            baseRequest.setHeader("Accept", "application/json");
            baseRequest.setEntity(reqEntity);
            if(global.verbose){
                print(baseRequest);
            }
            try(CloseableHttpResponse httpresponse = httpclient.execute(baseRequest)){
                HttpEntity entity = httpresponse.getEntity();
                String respEntity = EntityUtils.toString(entity);
                if(global.verbose){
                    printResponse(httpresponse);
                }
                return respEntity;
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (ConnectTimeoutException e){
                return e.getMessage();
            }
            return "Unknown error during post";
        }
    }

    private String buildAuthenticateApi(String env, String org){
        if("eng".equals(env)){
            return MessageFormat.format(ENG_ID, org);
        }else if("staging".equals(env)){
            return MessageFormat.format(STAGING_ID, org);
        }else if("prod".equals(env)){
            return MessageFormat.format(PROD_ID, org);
        }
        throw new IllegalArgumentException("Unknown env:" + env);
    }

    public String retrieveToken(String env, String org, String clientId, String clientSecret, boolean verbose) throws IOException {
        String url = buildAuthenticateApi(env, org);

        try(CloseableHttpClient client = HttpClientBuilder.create()
                .setDefaultRequestConfig(config)
                .setConnectionManager(manager)
                .build()){
            HttpPost httpPost = new HttpPost(url);
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
                    printResponse(response);
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
        System.out.println("---http request---");
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
    }

    private void printHeaders(Header[] headers){
        for(Header h: headers){
            System.out.println("    "+h.getName() + ":" + h.getValue());
        }
    }

    private void printResponse(CloseableHttpResponse response){
        System.out.println("---http response---");
        printHeaders(response.getHeaders());
        System.out.println("    Status Code:"+response.getCode());
    }

}
