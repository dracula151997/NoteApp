package com.project.semicolon.noteapp.utils;

import android.graphics.Color;
import android.text.Html;

import java.util.Random;
import java.util.UUID;

public class MethodHelper {

    public static String generateRandomUUID() {
        return UUID.randomUUID().toString();
    }

    public static String setHtmlText() {
        return Html.fromHtml("&#8226").toString();
    }

    public static int generateRandomColor() {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }
}
