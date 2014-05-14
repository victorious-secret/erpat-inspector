package com.techcamp.disaster.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.techcamp.disaster.LoginActivity;
import com.techcamp.disaster.MainActivity;
import com.techcamp.disaster.models.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by joseph on 5/10/14.
 */
public class Utils {

    public static String getSampleResponse(Context context, String name) {
        InputStream is = null;
        try {
            is = context.getAssets().open("sample_responses/" + name + ".json");
            StringBuilder builder = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            while (reader.ready()) {
                builder.append(reader.readLine());
            }
            return builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is!=null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    public static String getUser(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        return prefs.getString("user_id", null);
    }

    public static void saveUser(Context context, String s) {
        SharedPreferences prefs = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        prefs.edit().putString("user_id", s).commit();
    }

    public static void saveCurrentUserDate(Context context, User user) {
        SharedPreferences prefs = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("email",  user.getEmail());
        editor.putString("password", user.getPassword());
        editor.commit();
    }

    public static User getCurrentUserData(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        User user = new User();
        user.setEmail(prefs.getString("email", null));
        user.setPassword(prefs.getString("password", null));
        return user;
    }

    public static void clearPreferences(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        prefs.edit().clear().commit();
    }
}
