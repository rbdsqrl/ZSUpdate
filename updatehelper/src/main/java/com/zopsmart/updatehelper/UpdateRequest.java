package com.zopsmart.updatehelper;

import android.content.Context;

import com.zopsmart.updatehelper.exception.ZSException;
import com.zopsmart.updatehelper.util.NetworkUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;


public class UpdateRequest {

    public enum Method {
        GET, POST, PUT, DELETE;
    }

    private Context context;

    private ZSRequest zsRequest;

    UpdateRequest(Method method, String url, HashMap headers, JSONObject params, Context context) throws JSONException {
        this.context =  context;
        zsRequest = new ZSRequest(method,url,headers,params);

    }

    public JSONObject fetch() throws JSONException, ZSException, IOException {
        return zsRequest.fetch();
    }

    class ZSRequest {

        private String url;
        Method method;
        OkHttpClient httpClient;
        Headers headerBuild;
        RequestBody requestBody;

        ZSRequest(Method method, String url, HashMap headers, JSONObject params) throws JSONException {
            httpClient = new OkHttpClient();
            this.method = method;
            this.url = url;
            headerBuild = getRequestHeaders(headers);
            requestBody = getRequestBody(params);
        }
        private Headers getRequestHeaders(HashMap params) throws JSONException {
            if (params == null)
                params = new HashMap();

            return Headers.of(params);
        }

        private FormBody getRequestBody(JSONObject params) throws JSONException {
            FormBody.Builder parameters = new FormBody.Builder();
            if (params != null) {
                Iterator<String> iterator = params.keys();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    Object value = params.get(key);
                    parameters.add(key, value.toString());
                }
            }
            return parameters.build();
        }

        JSONObject fetch() throws IOException, JSONException, ZSException {
            if (!NetworkUtils.isNetworkAvailable(context)) {
                throw new ZSException("No internet connection");
            }
            okhttp3.Request.Builder requestBuilder = new okhttp3.Request.Builder()
                    .headers(headerBuild)
                    .url(url);
            switch (method) {
                case GET:
                    requestBuilder.get();
                    break;
                case PUT:
                    requestBuilder.put(requestBody);
                    break;
                case POST:
                    requestBuilder.post(requestBody);
                    break;
                case DELETE:
                    requestBuilder.delete(requestBody);
                    break;
                default:
                    throw new ZSException("Http Method has not been implement");
            }
            okhttp3.Request request = requestBuilder.build();
            okhttp3.Response response = httpClient.newCall(request).execute();
            ResponseBody body = response.body();
            if (!response.isSuccessful()) {
                JSONObject error = new JSONObject(response.body().string());
                throw new ZSException(error.getString("message"));
            }
            String result = body.string();
            JSONObject data = new JSONObject(result);
            return data;
        }
    }

}
