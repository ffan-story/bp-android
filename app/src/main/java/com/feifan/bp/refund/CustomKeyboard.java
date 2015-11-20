package com.feifan.bp.refund;

import android.app.Activity;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.text.Editable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.EditText;

/**
 * congjing
 */
class CustomKeyboard{
    private KeyboardView mKeyboardView;
    private Activity mHostActivity;
    public  EditText mEditText;

    /**
     * Create a custom keyboard, that uses the KeyboardView (with resource id
     * @param host  The hosting activity.
     * @param keyboardView  The id of the KeyboardView.
     * @param layoutid The id of the xml file containing the keyboard layout.
     */
    public CustomKeyboard(Activity host,KeyboardView keyboardView, int layoutid,EditText edit){
        mHostActivity = host;
        this.mKeyboardView  = keyboardView;
        this.mEditText = edit;
        mKeyboardView.setKeyboard(new Keyboard(mHostActivity, layoutid));
        mKeyboardView.setEnabled(true);
        mKeyboardView.setPreviewEnabled(false);
        mKeyboardView.setOnKeyboardActionListener(mOnKeyboardActionListener);
    }

    public void showCustomKeyboard(View v){
        mKeyboardView.setVisibility(View.VISIBLE);
        mKeyboardView.setEnabled(true);
    }

    public void hideCustomKeyboard(){
        mKeyboardView.setVisibility(View.GONE);
        mKeyboardView.setEnabled(false);
    }

    public void registerEditText() {
        mEditText.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int inType = mEditText.getInputType();
                mEditText.onTouchEvent(event);
                mEditText.setInputType(inType);
                return false;
            }
        });
    }

    private OnKeyboardActionListener mOnKeyboardActionListener = new OnKeyboardActionListener(){
        public final static int CodeDelete = -5; // Keyboard.KEYCODE_DELETE
        public final static int CodeCancel = -3; // Keyboard.KEYCODE_CANCEL

        @Override
        public void onKey(int primaryCode, int[] keyCodes){
            Editable editable = mEditText.getText();
            int start = mEditText.getSelectionStart();
            if (primaryCode == CodeCancel){
                hideCustomKeyboard();
            }else if (primaryCode == CodeDelete){
                if (editable != null && start > 0)
                    editable.delete(start - 1, start);
            }else if(primaryCode == 46){

            }else { // insert character
                editable.insert(start, Character.toString((char) primaryCode));
            }
        }

        @Override
        public void onPress(int arg0)
        {
        }

        @Override
        public void onRelease(int primaryCode)
        {
        }

        @Override
        public void onText(CharSequence text)
        {
        }

        @Override
        public void swipeDown()
        {
        }

        @Override
        public void swipeLeft()
        {
        }

        @Override
        public void swipeRight()
        {
        }

        @Override
        public void swipeUp()
        {
        }
    };
}