package com.example.chatup;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class Dialog extends AppCompatDialogFragment {
    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder abuilder= new AlertDialog.Builder(getActivity());
        abuilder.setTitle("Go to")
                .setMessage("Open settings")
               .setPositiveButton("ok",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {

                    }
                });
        return  abuilder.create();
    }
}
