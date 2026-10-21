package com.example.smartpantrymanager.ui;

import android.animation.ValueAnimator;
import android.os.Build;
import android.provider.Settings;
import android.view.View;

/** Brief row entrance motion that respects the device animation preference. */
public final class RowMotion {
    private RowMotion() { }

    public static void bind(View view, boolean firstAppearance, int position) {
        // Recycled views must never retain an old row's animation state.
        view.animate().cancel();
        view.setAlpha(1f);
        view.setTranslationY(0f);
        boolean enabled = Build.VERSION.SDK_INT >= 26
                ? ValueAnimator.areAnimatorsEnabled()
                : Settings.Global.getFloat(view.getContext().getContentResolver(),
                        Settings.Global.ANIMATOR_DURATION_SCALE, 1f) != 0f;
        if (!firstAppearance || !enabled) return;

        float distance = 8f * view.getResources().getDisplayMetrics().density;
        view.setAlpha(0f);
        view.setTranslationY(distance);
        view.animate().alpha(1f).translationY(0f)
                .setStartDelay(Math.min(position, 6) * 20L)
                .setDuration(180L)
                .setInterpolator(new android.view.animation.DecelerateInterpolator())
                .start();
    }

    public static void reset(View view) {
        view.animate().cancel();
        view.setAlpha(1f);
        view.setTranslationY(0f);
    }
}
