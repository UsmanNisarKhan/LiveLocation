package com.live.location.Util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.live.location.R;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import static android.content.Context.WINDOW_SERVICE;

public class Utils {

    public static String removeCode(String mobile) {
        Phonenumber.PhoneNumber number;
        PhoneNumberUtil utils = PhoneNumberUtil.getInstance();
        try {
            for (String region : utils.getSupportedRegions()) {
                // Check whether it's a valid number.
                boolean isValid = utils.isPossibleNumber(mobile, region);
                if (isValid) {
                    number = utils.parse(mobile, region);
                    // Check whether it's a valid number for the given region.
                    isValid = utils.isValidNumberForRegion(number, region);
                    if (isValid) {
                        Log.d("Region:", region); // IN
                        Log.d("Phone Code", number.getCountryCode() + ""); // 91
                        Log.d("Phone No.", number.getNationalNumber() + ""); // 99xxxxxxxxxx
                    }

                    return String.valueOf(number.getNationalNumber());
                }
            }
        } catch (NumberParseException e) {
            e.printStackTrace();
        }
        return "Unknown";
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isAcceptingText()) {
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(),
                    0
            );
        }
    }

    public static String sha256(String base) {
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

}