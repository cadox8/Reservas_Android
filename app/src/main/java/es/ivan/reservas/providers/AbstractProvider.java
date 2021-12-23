package es.ivan.reservas.providers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import es.ivan.reservas.api.AbstractAPI;
import es.ivan.reservas.api.Error;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public abstract class AbstractProvider extends Thread {

    protected final String fetchURL = "http://cadox8.es/reservas/";

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private final OkHttpClient client = new OkHttpClient();
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    public <T extends AbstractAPI> T get(Class<T> apiReturn, String url) {
        final Request request = new Request.Builder().url(fetchURL + url).get().build();

        try (final Response response = client.newCall(request).execute()) {
            return gson.fromJson(response.body().string(), apiReturn);
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            return (T) new Error("Error al hacer la petición al servidor");
        }
    }

    public <T extends AbstractAPI> T post(Class<T> apiReturn, String url, String bodyJSON) {
        final Request request = new Request.Builder().url(fetchURL + url).post(RequestBody.create(bodyJSON, JSON)).build();

        try (final Response response = client.newCall(request).execute()) {
            return gson.fromJson(response.body().string(), apiReturn);
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            return (T) new Error("Error al hacer la petición al servidor");
        }
    }

    @Override
    public void run() {

    }
}
