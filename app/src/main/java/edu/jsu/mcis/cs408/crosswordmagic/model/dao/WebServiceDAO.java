package edu.jsu.mcis.cs408.crosswordmagic.model.dao;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class WebServiceDAO {

    private final String TAG = "WebServiceDAO";
    private final DAOFactory daoFactory;
    private final String HTTP_METHOD = "GET";
    private final String ROOT_URL = "http://ec2-3-142-171-53.us-east-2.compute.amazonaws.com:8080/CrosswordMagicServer/puzzle";
    private String requestUrl;
    private ExecutorService pool;

    WebServiceDAO(DAOFactory daoFactory) { this.daoFactory = daoFactory; }

    public JSONArray list() {
        requestUrl = ROOT_URL;
        JSONArray result = null;
        try {
            pool = Executors.newSingleThreadExecutor();
            Future<String> pending = pool.submit(new CallableHTTPRequest());
            String response = pending.get();
            pool.shutdown();
            result = new JSONArray(response);
        }
        catch (Exception e) { e.printStackTrace(); }
        return result;
    }
    public JSONObject list(int id) {
        requestUrl = ROOT_URL + "?id=" + id;
        JSONObject result = null;
        try {
            pool = Executors.newSingleThreadExecutor();
            Future<String> pending = pool.submit(new CallableHTTPRequest());
            String response = pending.get();
            pool.shutdown();
            result = new JSONObject(response);
        }
        catch (Exception e) { e.printStackTrace(); }
        Log.d("Testing", result.toString());
        return result;
    }

    public class CallableHTTPRequest implements Callable<String> {

        @Override
        public String call() {
            StringBuilder r = new StringBuilder();
            String line;
            HttpURLConnection conn = null;

            try {
                URL url = new URL(requestUrl);
                Log.d("WebDAO", url.toString());
                conn = (HttpURLConnection) url.openConnection();

                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);

                conn.connect();

                int code = conn.getResponseCode();
                if (code == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line = reader.readLine()) != null) {
                        r.append(line);
                    }
                }

            } catch (Exception e) {
                Log.e(TAG, "Exception: ", e);
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
            Log.d("WebDAO", r.toString().trim());
            return r.toString().trim();
        }
    }
}
