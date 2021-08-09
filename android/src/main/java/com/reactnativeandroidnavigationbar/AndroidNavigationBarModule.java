package com.reactnativeandroidnavigationbar;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.uimanager.IllegalViewOperationException;

import org.jetbrains.annotations.NotNull;

import static com.facebook.react.bridge.UiThreadUtil.runOnUiThread;

public class AndroidNavigationBarModule extends ReactContextBaseJavaModule {
    public static final String NAME = "AndroidNavigationBar";
    public static final String API_LEVEL_ERROR = "AndroidNavigationBar module requires API level >= 23";
    public static final String NO_ACTIVITY_ERROR = "AndroidNavigationBar called without an activity";
    public static final String UPDATE_ERROR = "Error while updating navigation bar settings";

    public AndroidNavigationBarModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @NotNull
    @Override
    public String getName() {
        return NAME;
    }

    @ReactMethod
    public void show(final Promise promise) {
        safeRun(createRunnableForClearFlag(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION, promise), promise);
    }

    @ReactMethod
    public void hide(final Promise promise) {
        safeRun(createRunnableForAddFlag(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION, promise), promise);
    }

    @ReactMethod
    public void setFullscreen(final Boolean fullscreen, final Promise promise) {
        safeRun(createRunnableForSetFullscreen(fullscreen, promise), promise);
    }

    @ReactMethod
    public void setImmersive(final Boolean immersive, final Promise promise) {
        safeRun(createRunnableForSetImmersive(immersive, promise), promise);
    }

    @ReactMethod
    public void setLight(final Boolean light, final Promise promise) {
        safeRun(createRunnableForLight(light, promise), promise);
    }

    @ReactMethod
    public void setColor(final String color, final Integer duration, final Promise promise) {
        safeRun(createRunnableForColor(color, duration, promise), promise);
    }

    protected void safeRun(final Runnable runnable, final Promise promise) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (getCurrentActivity() != null) {
                try {
                    runOnUiThread(runnable);
                } catch (IllegalViewOperationException e) {
                    promise.reject(UPDATE_ERROR, new Throwable(e));
                }
            } else {
                promise.reject(NO_ACTIVITY_ERROR, new Throwable(NO_ACTIVITY_ERROR));
            }
        } else {
            promise.reject(API_LEVEL_ERROR, new Throwable(API_LEVEL_ERROR));
        }
    }

    protected Runnable createRunnableForSetFullscreen(final boolean fullscreen, final Promise promise) {
        return () -> {
            Activity activity = getCurrentActivity();
            if (activity == null) {
                promise.reject(NO_ACTIVITY_ERROR, new Throwable(NO_ACTIVITY_ERROR));
            } else {
                Window window = activity.getWindow();
                int flags = window.getDecorView().getSystemUiVisibility();
                if (fullscreen) {
                    flags = flags | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
                } else {
                    flags = flags & ~View.SYSTEM_UI_FLAG_HIDE_NAVIGATION & ~View.SYSTEM_UI_FLAG_FULLSCREEN;
                }
                window.getDecorView().setSystemUiVisibility(flags);
                promise.resolve(true);
            }
        };
    }

    protected Runnable createRunnableForSetImmersive(final boolean immersive, final Promise promise) {
        return () -> {
            Activity activity = getCurrentActivity();
            if (activity == null) {
                promise.reject(NO_ACTIVITY_ERROR, new Throwable(NO_ACTIVITY_ERROR));
            } else {
                Window window = getCurrentActivity().getWindow();
                int flags = window.getDecorView().getSystemUiVisibility();
                if (immersive) {
                    flags = flags | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
                } else {
                    flags = flags & ~View.SYSTEM_UI_FLAG_HIDE_NAVIGATION & ~View.SYSTEM_UI_FLAG_FULLSCREEN & ~View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
                }
                window.getDecorView().setSystemUiVisibility(flags);
                promise.resolve(true);
            }
        };
    }

    protected Runnable createRunnableForAddFlag(final int flag, final Promise promise) {
        return () -> {
            Activity activity = getCurrentActivity();
            if (activity == null) {
                promise.reject(NO_ACTIVITY_ERROR, new Throwable(NO_ACTIVITY_ERROR));
            } else {
                Window window = activity.getWindow();
                int flags = window.getDecorView().getSystemUiVisibility();
                flags |= flag;
                window.getDecorView().setSystemUiVisibility(flags);
                promise.resolve(true);
            }
        };
    }


    protected Runnable createRunnableForClearFlag(final int flag, final Promise promise) {
        return () -> {
            Activity activity = getCurrentActivity();
            if (activity == null) {
                promise.reject(NO_ACTIVITY_ERROR, new Throwable(NO_ACTIVITY_ERROR));
            } else {
                Window window = activity.getWindow();
                int flags = window.getDecorView().getSystemUiVisibility();
                flags &= ~flag;
                window.getDecorView().setSystemUiVisibility(flags);
                promise.resolve(true);
            }
        };
    }

    @TargetApi(Build.VERSION_CODES.O)
    protected Runnable createRunnableForLight(final Boolean light, final Promise promise) {
        return () -> {
            Activity activity = getCurrentActivity();
            if (activity == null) {
                promise.reject(NO_ACTIVITY_ERROR, new Throwable(NO_ACTIVITY_ERROR));
            } else {
                Window window = activity.getWindow();
                int flags = window.getDecorView().getSystemUiVisibility();
                if (light) {
                    flags = flags | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
                } else {
                    flags &= ~View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
                }
                window.getDecorView().setSystemUiVisibility(flags);
                promise.resolve(true);
            }
        };
    }

    protected Runnable createRunnableForColor(final String color, final Integer duration, final Promise promise) {
        return () -> {
            try {
                Activity activity = getCurrentActivity();
                if (activity == null) {
                    promise.reject(NO_ACTIVITY_ERROR, new Throwable(NO_ACTIVITY_ERROR));
                } else {
                    Integer colorFrom = activity.getWindow().getNavigationBarColor();
                    Integer colorTo = Color.parseColor(String.valueOf(color));
                    ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
                    colorAnimation.addUpdateListener(createAnimationListener());
                    colorAnimation.setDuration(duration);
                    colorAnimation.start();
                    promise.resolve(true);
                }
            } catch (Exception e) {
                promise.reject(UPDATE_ERROR, new Throwable(e));
            }
        };
    }

    protected AnimatorUpdateListener createAnimationListener() {
        Activity activity = getCurrentActivity();

        if (activity != null) {
            return animator -> activity.getWindow().setNavigationBarColor((Integer) animator.getAnimatedValue());
        }

        return null;
    }
}
