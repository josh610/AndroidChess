package com.example.androidchess;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class GameDialogFragment extends DialogFragment {
    public static final String MESSAGE_KEY = "message_key";

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(bundle.getString(MESSAGE_KEY)).setPositiveButton("Yes", (dialog, id) -> {});
        builder.setMessage(bundle.getString(MESSAGE_KEY)).setNegativeButton("No", (dialog, id) -> {});

        return builder.create();
    }
}
