package com.project.semicolon.noteapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class NoteDialogFragment extends DialogFragment{
    private EditText editText;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.note_dialog_fragment_layout, container);
        editText = view.findViewById(R.id.input_note);
        editText.requestFocus();
        getDialog().setTitle("New Note");

        return view;
    }

    @Override
    public void onResume() {
        Window window = getDialog().getWindow();

        Point size = new Point();

        // Store dimensions of the screen in `size`

        Display display = window.getWindowManager().getDefaultDisplay();

        display.getSize(size);

        // Set the width of the dialog proportional to 75% of the screen width

        window.setLayout((int) (size.x * 0.75), WindowManager.LayoutParams.WRAP_CONTENT);

        window.setGravity(Gravity.CENTER);
        super.onResume();
    }


}
