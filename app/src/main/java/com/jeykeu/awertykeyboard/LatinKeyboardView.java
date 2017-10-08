/*
 * Copyright (C) 2008-2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jeykeu.awertykeyboard;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.Keyboard.Key;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodSubtype;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import java.util.List;

import static android.R.attr.x;
import static android.R.attr.y;
import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class LatinKeyboardView extends KeyboardView {

    static final int KEYCODE_OPTIONS = -100;
    // TODO: Move this into android.inputmethodservice.Keyboard
    static final int KEYCODE_LANGUAGE_SWITCH = -101;
    static final int KEYCODE_DIAL_PAD = -102;
    private float mDownX;
    private float mDownY;
    private final float SCROLL_THRESHOLD = 10;
    private boolean isOnClick;
    public PopupWindow popup;

    public LatinKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
//        this.createPopUpWindow(context);
    }

    public LatinKeyboardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected PopupWindow createPopUpWindow() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.keyboard_popup_layout, new FrameLayout(getContext()));

        final PopupWindow popup = new PopupWindow(popupView,
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        popup.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popup.showAtLocation(this, Gravity.CENTER, x, y);
        popup.setBackgroundDrawable(new BitmapDrawable());
        popup.setOutsideTouchable(true);
        popup.setTouchable(true);
        popup.setOutsideTouchable(true);
        popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                // some action ....
                Log.d("popup", "Dismissed");
            }

        });
        return popup;
    }
    /*public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mDownX = ev.getX();
                mDownY = ev.getY();
                isOnClick = true;
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (isOnClick) {
                    Log.i("onTouchEvent", "onClick ");
                    if (popup != null) {
                        popup.dismiss();
                    }
                    //TODO onClick code
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (isOnClick && (Math.abs(mDownX - ev.getX()) > SCROLL_THRESHOLD || Math.abs(mDownY - ev.getY()) > SCROLL_THRESHOLD)) {
                    Log.i("onTouchEvent", "movement detected");
                    isOnClick = false;
                }
                break;
            default:
                break;
        }
        return true;
    }*/

    @Override
    protected boolean onLongPress(Key popupKey) {
        Log.w("LatinKeyboardView", "Long press: " + popupKey.codes[0]);
        if (popupKey.codes[0] == Keyboard.KEYCODE_CANCEL) {
            Log.w("LatinKeyboardView", "Long press: A");
            getOnKeyboardActionListener().onKey(KEYCODE_OPTIONS, null);
            return true;
        /*} else if (key.codes[0] == 113) {

            return true; */
        } else {
            Log.w("LatinKeyboardView", "Long press: B");
            Log.d("LatinKeyboardView", "KEY: " + popupKey.codes[0]);
            /*if (popupKey.popupCharacters != null && popupKey.popupCharacters.length ==1) {
                //getOnKeyboardActionListener().onKey(popupKey.popupCharacters.subSequence(0,1), null);
            }*/

            //this.createPopUpWindow();
            /*popup = createPopUpWindow();
            if (popup != null && popup.isShowing()) {
                popup.update(x, y, width, height);
            } else if (popup != null) {
                popup.setWidth(width);
                popup.setHeight(height);
                popup.showAtLocation(this, Gravity.NO_GRAVITY, x, y);
            }*/
            //this.setPreviewEnabled(true);
            return super.onLongPress(popupKey);
        }
    }


    void setSubtypeOnSpaceKey(final InputMethodSubtype subtype) {
        final LatinKeyboard keyboard = (LatinKeyboard) getKeyboard();
        keyboard.setSpaceIcon(getResources().getDrawable(subtype.getIconResId()));
        invalidateAllKeys();
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(28);
        paint.setColor(Color.LTGRAY);

        List<Key> keys = getKeyboard().getKeys();
        for (Key key : keys) {
            if (key.popupCharacters != null) {
                canvas.drawText(key.popupCharacters.toString(), key.x + (key.width - 25), key.y + 40, paint);
            }

        }
    }


}
