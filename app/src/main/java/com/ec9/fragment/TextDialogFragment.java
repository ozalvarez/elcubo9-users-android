package com.ec9.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.ec9.R;

/**
 * Created by Oswaldo on 11/19/2014.
 */
public class TextDialogFragment extends DialogFragment {
    private NoticeDialogListener mListener;
    private EditText etText;
    private String text;
    private String title;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String Text) {
        text = Text;
    }

    public interface NoticeDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog, String text);

        public void onDialogNegativeClick(DialogFragment dialog);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) activity;

        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View v = inflater.inflate(R.layout.dialog_text, null);
        builder.setView(v)
                // Add action buttons
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogPositiveClick(TextDialogFragment.this,etText.getText().toString());
                    }
                })
                .setNegativeButton("Cerrar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogNegativeClick(TextDialogFragment.this);
                    }
                });
        etText = ((EditText) v.findViewById(R.id.etText));
        etText.setText(text);
        return builder.create();
    }
}
