package com.example.mytaint;


import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.AppCompatEditText;
import android.text.SpannableStringBuilder;
import android.widget.EditText;
import android.widget.TextView;
import dalvik.system.Taint;
import android.util.Log;

public class MyTaint {
    private static final String TAG = "MyTaint";

    //final static int taintTag = 0;
    final static int taintTag = Taint.TAINT_TEST;


    static private void addTaintSpannableStringBuilder(SpannableStringBuilder sp){
        char c = Taint.addTaintChar('1', taintTag );
        //char c= '0';
        sp.append(c);
        sp.delete(sp.length() - 1, sp.length());
        System.out.println("tainting SpannableStringBuilder");
        Log.v(TAG, "tainting SpannableStringBuilder");

    }

    static public int addTaintInt(int i){
        return Taint.addTaintInt(i, taintTag);
    }

    static public void addTaint(Object obj){
        //Taint.addTaintChar('a', Taint.TAINT_TEST);
        Class clz = obj.getClass();
        if(clz == SpannableStringBuilder.class) {
            SpannableStringBuilder sp = (SpannableStringBuilder) obj;
            addTaintSpannableStringBuilder(sp);
        }else if (clz == String.class){
            Taint.addTaintString((String)obj, taintTag);
            System.out.println("tainting String");
            Log.v(TAG, "tainting String");
        }else if (clz == EditText.class || clz == TextView.class || clz == AppCompatEditText.class || clz == AppCompatTextView.class){
            TextView et = (TextView)obj; //base class
            if(et.getText().getClass() == SpannableStringBuilder.class)
                addTaintSpannableStringBuilder((SpannableStringBuilder)et.getText());
            else{
                System.out.println("tainting unknown TextView.getText" + et.getClass() );
                Log.v(TAG, "tainting unknown TextView.getText" + et.getClass() );
            }
            System.out.println("tainting TextView");
            Log.v(TAG, "tainting TextView");
        }else {
            System.out.println("tainting unknow class" + obj.getClass());
            Log.v(TAG, "tainting unknow class" + obj.getClass());
        }
    }
}
