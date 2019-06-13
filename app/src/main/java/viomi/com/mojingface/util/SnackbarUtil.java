package viomi.com.mojingface.util;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import viomi.com.mojingface.R;


/**
 * Snackbar  封装工具
 */

public class SnackbarUtil {

    private static final String TAG = SnackbarUtil.class.getName();

    private static Snackbar snackbar;
    private static boolean isShowing = false;
//    private static long lastTime;

    public static void make(View view, CharSequence text, int duration) {
        make(view, text, 0, duration, 0, null, 0, null);
    }

    public static void make(View view, CharSequence text, int duration, int backgroundColor) {
        make(view, text, 0, duration, backgroundColor, null, 0, null);
    }

    public static void make(View view, CharSequence text, int messageColor, int duration, int backgroundColor) {
        make(view, text, messageColor, duration, backgroundColor, null, 0, null);
    }

    public static void make(View view, CharSequence text, int messageColor, int duration, int backgroundColor, CharSequence actionText, int actionTextColor, ActionCallback callback) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
//        long currentTime = System.currentTimeMillis();
//        if (currentTime - lastTime <= 3 * 1000) {
//            return;
//        }
//        lastTime = currentTime;

        snackbar = Snackbar.make(view, text, duration);

        if (!TextUtils.isEmpty(actionText)) {
            snackbar.setAction(actionText, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callback != null) {
                        callback.onAction(v);
                    }
                }
            });
        }

        if (callback != null) {
            snackbar.addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                @Override
                public void onDismissed(Snackbar transientBottomBar, int event) {
                    isShowing = false;
                    switch (event) {
                        case Snackbar.Callback.DISMISS_EVENT_CONSECUTIVE:
                            break;
                        case Snackbar.Callback.DISMISS_EVENT_MANUAL:
                            break;
                        case Snackbar.Callback.DISMISS_EVENT_SWIPE:
                            break;
                        case Snackbar.Callback.DISMISS_EVENT_TIMEOUT:
                            break;
                        case Snackbar.Callback.DISMISS_EVENT_ACTION:
                            break;
                        default:

                    }
                }

                @Override
                public void onShown(Snackbar transientBottomBar) {
                    LogUtils.i(TAG, "onShown");
                    isShowing = true;
                }
            });
        }

        if (messageColor != 0) {
            View snackbarView = snackbar.getView();
            if (snackbarView != null) {
                ((TextView) snackbarView.findViewById(R.id.snackbar_text)).setTextColor(messageColor);
            }
        }

        if (backgroundColor != 0) {
            View snackbarView = snackbar.getView();
            if (snackbarView != null) {
                snackbarView.setBackgroundColor(backgroundColor);
            }
        }

        if (actionTextColor != 0) {
            snackbar.setActionTextColor(actionTextColor);
        }

        snackbar.show();
    }

    public static void dismiss() {
        if (snackbar != null) {
            snackbar.dismiss();
        }
    }

    public static Snackbar getSnackbar() {
        return snackbar;
    }

    public static boolean isShowing() {
        return isShowing;
    }

    public interface ActionCallback {
        void onAction(View view);
    }
}
