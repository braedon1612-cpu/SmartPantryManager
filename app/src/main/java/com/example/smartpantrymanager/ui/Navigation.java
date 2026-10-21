package com.example.smartpantrymanager.ui;

import android.app.Activity;
import android.content.Intent;
import androidx.appcompat.widget.Toolbar;
import com.example.smartpantrymanager.MainActivity;
import com.example.smartpantrymanager.SuggestedRecipesActivity;
import com.example.smartpantrymanager.R;

/** Shared toolbar navigation without building duplicate Activity stacks. */
public final class Navigation {
    private Navigation() { }

    public static void setup(Activity activity) {
        Toolbar toolbar = activity.findViewById(R.id.navigationToolbar);
        toolbar.inflateMenu(R.menu.pantry_navigation);
        toolbar.setOnMenuItemClickListener(item -> {
            Class<?> target;
            if (item.getItemId() == R.id.navPantry) target = MainActivity.class;
            else if (item.getItemId() == R.id.navRecipes) target = SuggestedRecipesActivity.class;
            else return false;
            if (!activity.getClass().equals(target)) {
                Intent intent = new Intent(activity, target);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                activity.startActivity(intent);
            }
            return true;
        });
    }
}
