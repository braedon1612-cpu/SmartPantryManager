package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.example.smartpantrymanager.ui.Navigation;
import com.example.smartpantrymanager.ui.ScreenInsets;

/** Stores a small UI preference in SharedPreferences; pantry data remains in SQLite. */
public class SettingsActivity extends AppCompatActivity {
    public static final String PREFERENCES = "pantry_settings";
    public static final String DEFAULT_UNIT = "default_unit";

    @Override protected void onCreate(Bundle state) {
        super.onCreate(state);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);
        ScreenInsets.apply(findViewById(R.id.main));
        Navigation.setup(this);
        Spinner units = findViewById(R.id.defaultUnitInput);
        if (state == null) {
            String selected = getSharedPreferences(PREFERENCES, MODE_PRIVATE)
                    .getString(DEFAULT_UNIT, "g");
            String[] options = getResources().getStringArray(R.array.pantry_units);
            for (int index = 0; index < options.length; index++) {
                if (options[index].equals(selected)) units.setSelection(index);
            }
        }
        findViewById(R.id.saveSettings).setOnClickListener(v -> {
            getSharedPreferences(PREFERENCES, MODE_PRIVATE).edit()
                    .putString(DEFAULT_UNIT, units.getSelectedItem().toString()).apply();
            Toast.makeText(this, R.string.settings_saved, Toast.LENGTH_SHORT).show();
        });
    }
}
