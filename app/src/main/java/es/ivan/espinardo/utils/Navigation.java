package es.ivan.espinardo.utils;

import static es.ivan.espinardo.R.id.bottom_navigation;

import android.app.Activity;
import android.content.Intent;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import es.ivan.espinardo.R;
import es.ivan.espinardo.activities.main.InstallationsActivity;
import es.ivan.espinardo.activities.main.BookingsActivity;
import es.ivan.espinardo.activities.main.ProfileActivity;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Navigation {

    private final Activity activity;

    private int lastSelected;

    public void changePage(int viewId) {
        final BottomNavigationView bottomNavigationView = this.activity.findViewById(bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Intent intent = null;

            if (item.getItemId() == this.lastSelected) return true;

            switch (item.getItemId()) {
                case R.id.reservas:
                    intent = new Intent(this.activity, BookingsActivity.class);
                    break;
                case R.id.instalaciones:
                    intent = new Intent(this.activity, InstallationsActivity.class);
                    break;
                case R.id.perfil:
                    intent = new Intent(this.activity, ProfileActivity.class);
                    break;
                default:
            }
            this.activity.startActivity(intent);
            return true;
        });
        this.selectedItemOnBottomBar(viewId);
    }

    public void selectedItemOnBottomBar(int viewId) {
        this.lastSelected = viewId;
        ((BottomNavigationView)this.activity.findViewById(bottom_navigation)).setSelectedItemId(viewId);
    }
}
