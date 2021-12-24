package es.ivan.espinardo.managers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import es.ivan.espinardo.activities.user.LoginActivity;

public class SessionManager {

    private final Context context;
    private final SharedPreferences sharedPreferences;

    public SessionManager(Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
    }

    public void saveToken(String token) {
        final SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putString("token", token);
        editor.apply();
    }

    public boolean hasToken() {
        return this.sharedPreferences.contains("token");
    }

    public void removeToken() {
        final SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.remove("token");
        editor.apply();
    }

    public boolean canAccess() {
        if (!this.hasToken()) {
            this.context.startActivity(new Intent(this.context, LoginActivity.class));
            return false;
        }
        return true;
    }
}
