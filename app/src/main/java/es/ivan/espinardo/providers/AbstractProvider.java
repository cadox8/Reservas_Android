package es.ivan.espinardo.providers;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import es.ivan.espinardo.api.AbstractAPI;
import es.ivan.espinardo.api.Error;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public abstract class AbstractProvider {

    protected final String fetchURL = "http://cadox8.es:3010/";

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    protected final ExecutorService pool = Executors.newFixedThreadPool(1);

    private final OkHttpClient client = new OkHttpClient();
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    public <T extends AbstractAPI> T get(Class<T> apiReturn, String url) {
        final Request request = new Request.Builder().url(fetchURL + url).get().build();

        try (final Response response = client.newCall(request).execute()) {
            return gson.fromJson(response.body().string(), apiReturn);
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }

    public <T extends AbstractAPI> T post(Class<T> apiReturn, String url, HashMap<String, String> bodyData) {
        final JsonObject body = new JsonObject();

        final String[] keys = bodyData.keySet().toArray(new String[0]);

        for (int i = 0; i < bodyData.size(); i++) body.addProperty(keys[i], bodyData.get(keys[i]));

        final Request request = new Request.Builder().url(fetchURL + url).post(RequestBody.create(body.toString(), JSON)).build();

        try (final Response response = client.newCall(request).execute()) {
            return gson.fromJson(response.body().string(), apiReturn);
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }

    public <T extends AbstractAPI> T delete(Class<T> apiReturn, String url) {
        final Request request = new Request.Builder().url(fetchURL + url).delete().build();

        try (final Response response = client.newCall(request).execute()) {
            return gson.fromJson(response.body().string(), apiReturn);
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }
}
