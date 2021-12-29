package es.ivan.espinardo.providers;

import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import es.ivan.espinardo.api.User;
import es.ivan.espinardo.api.installations.Installation;
import es.ivan.espinardo.api.installations.Installations;

public class UserProvider extends AbstractProvider {

    public User login(String username, String password) {
        final HashMap<String, String> body = new HashMap<>();
        body.put("username", username);
        body.put("password", password);

        User user;
        try {
            user = pool.submit(() -> this.post(User.class, "user/login", body)).get();
        } catch (ExecutionException | InterruptedException e) {
            user = null;
        }
        return user;
    }

    public User register(String username, String email, String password) {
        final HashMap<String, String> body = new HashMap<>();
        body.put("username", username);
        body.put("password", password);
        body.put("email", email);
        User user;
        try {
            user = pool.submit(() -> this.post(User.class, "user/register", body)).get();
        } catch (ExecutionException | InterruptedException e) {
            user = null;
        }
        return user;
    }

    public User fetchUserByToken(String token) {
        User user;

        try {
            user = this.pool.submit(() -> this.get(User.class, "user/" + token)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            user = null;
        }
        return user;
    }
}
