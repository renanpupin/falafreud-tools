package com.falafreud.tools.modules.floatwidget.icon.magnet;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.falafreud.tools.R;

/**
 * ViewHolder for the remove Icon.
 */
public class RemoveView {

    private static final String TAG = "FloatWidget";

    protected View layout;
    protected View button;
    protected View shadow;
    protected ImageView buttonImage;
    protected WindowManager windowManager;
    protected Animator showAnim;
    protected Animator hideAnim;

    protected Animator shadowFadeOut;
    protected Animator shadowFadeIn;

    protected final int buttonBottomPadding;

    protected boolean shouldBeResponsive = true;
    protected boolean isShowing;

    protected RemoveView(Context context) {

        layout = LayoutInflater.from(context).inflate(R.layout.remove_float_layout, null);
        button = layout.findViewById(R.id.remove_button);
        buttonImage = (ImageView) layout.findViewById(R.id.remove_view);
        buttonImage.setImageResource(R.drawable.remove_shape);
        buttonBottomPadding = button.getPaddingBottom();
        shadow = layout.findViewById(R.id.shadow);
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        showAnim = new Animator(button, R.anim.remove_icon_slide_up_animation);
        hideAnim = new Animator(button, R.anim.remove_icon_slide_down_animation);
        shadowFadeIn = new Animator(shadow, android.R.anim.fade_in);
        shadowFadeOut = new Animator(shadow, android.R.anim.fade_out);
    }

    protected void setIconResId(int id) {

        buttonImage.setImageResource(id);
    }

    protected void setShadowBG(int shadowBG) {

        shadow.setBackgroundResource(shadowBG);
    }

    protected void show() {

        if (layout != null && layout.getParent() == null) {
            addToWindow(layout);
        }
        shadowFadeIn.startAnimation();
        showAnim.startAnimation(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

                isShowing = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    protected boolean isShowing() {

        return isShowing;
    }

    protected void hide() {

        shadowFadeOut.startAnimation();
        hideAnim.startAnimation(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                if (layout != null && layout.getParent() != null) {
                    isShowing = false;
                    layout.post(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                if (layout != null && layout.getParent() != null) {
                                    windowManager.removeView(layout);
                                }
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                                Log.d(TAG, "RemoveView onAnimationEnd run: " + e.toString());
                            }
                        }
                    });
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    protected void onMove(final float x, final float y) {

        if (shouldBeResponsive) {
            final int midpoint = button.getContext().getResources().getDisplayMetrics().widthPixels / 2;
            final float xDelta = x - midpoint;
            final int xTransformed = (int) Math.abs(xDelta * 100 / midpoint);
            final int bottomPadding = buttonBottomPadding - (xTransformed / 5);
            if (xDelta < 0) {
                button.setPadding(0, 0, xTransformed, bottomPadding);
            } else {
                button.setPadding(xTransformed, 0, 0, bottomPadding);
            }
        }
    }

    protected void destroy() {

        try {
            if (layout != null && layout.getParent() != null) {
                windowManager.removeView(layout);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            Log.d(TAG, "RemoveView onAnimationEnd run: " + e.toString());
        }
        layout = null;
        windowManager = null;
    }

    private void addToWindow(View layout) {

        int overlayFlag;
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//      overlayFlag = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
//    } else {
        overlayFlag = WindowManager.LayoutParams.TYPE_PHONE;
//    }
        WindowManager.LayoutParams params =
                new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.MATCH_PARENT, overlayFlag,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN, PixelFormat.TRANSLUCENT);
        windowManager.addView(layout, params);
    }
}
