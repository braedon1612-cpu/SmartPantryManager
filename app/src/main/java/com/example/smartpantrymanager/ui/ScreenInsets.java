package com.example.smartpantrymanager.ui;

import android.view.View;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/** Preserve XML padding while keeping content clear of system bars and keyboard. */
public final class ScreenInsets {
    private ScreenInsets() { }
    public static void apply(View root) {
        int left = root.getPaddingLeft(), top = root.getPaddingTop();
        int right = root.getPaddingRight(), bottom = root.getPaddingBottom();
        ViewCompat.setOnApplyWindowInsetsListener(root, (view, windowInsets) -> {
            Insets bars = windowInsets.getInsets(
                    WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.ime());
            view.setPadding(left + bars.left, top + bars.top,
                    right + bars.right, bottom + bars.bottom);
            return windowInsets;
        });
        ViewCompat.requestApplyInsets(root);
    }
}
