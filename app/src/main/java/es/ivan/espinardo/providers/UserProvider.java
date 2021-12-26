package es.ivan.espinardo.providers;

import java.util.HashMap;

import es.ivan.espinardo.api.User;

public class UserProvider extends AbstractProvider {

    public User login(String username, String password) {
        final HashMap<String, String> body = new HashMap<>();
        body.put("username", username);
        body.put("password", password);
        return this.post(User.class, "user/login", body);
    }

    public User register(String username, String email, String password) {
        final HashMap<String, String> body = new HashMap<>();
        body.put("username", username);
        body.put("password", password);
        body.put("email", email);
        return this.post(User.class, "user/register", body);
    }
}
