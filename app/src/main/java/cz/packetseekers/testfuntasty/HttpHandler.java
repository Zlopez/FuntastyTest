package cz.packetseekers.testfuntasty;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by Michal Konecny on 8.12.14.
 * Class for handling Http requests.
 */
public class HttpHandler {
    public final static int GET = 1;
    public final static int POST = 2;

    /**
     * Making http service calls
     * @param url - url to make request
     * @param method - http request method
     * @return response
     */
    public String makeServiceCall(String url, int method)
    {
        return this.makeServiceCall(url, method, null);
    }

    public String makeServiceCall(String url, int method, List<NameValuePair> params)
    {
        String response = null;
        try
        {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpEntity httpEntity = null;
            HttpResponse httpResponse = null;

            if(method == POST)
            {
                HttpPost httpPost = new HttpPost(url);
                if(params != null)
                {
                    httpPost.setEntity(new UrlEncodedFormEntity(params));
                }

                httpResponse = httpClient.execute(httpPost);
            }
            else if(method == GET)
            {
                if(params != null)
                {
                    String paramString = URLEncodedUtils.format(params,"utf-8");
                    url += "?" + paramString;
                }
                HttpGet httpGet = new HttpGet(url);

                httpResponse = httpClient.execute(httpGet);
            }
            httpEntity = httpResponse.getEntity();
            response = EntityUtils.toString(httpEntity);
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        catch (ClientProtocolException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return response;
    }
}
