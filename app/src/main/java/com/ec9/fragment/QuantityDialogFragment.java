package com.ec9.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import com.ec9.R;

/**
 * Created by Oswaldo on 11/19/2014.
 */
public class QuantityDialogFragment extends DialogFragment {
    NoticeDialogListener mListener;
    NumberPicker np;
    private int Quantity;

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public interface NoticeDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog, int quantity);

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
        builder.setTitle("Cantidad");
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View v = inflater.inflate(R.layout.dialog_number, null);
        builder.setView(v)
                // Add action buttons
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogPositiveClick(QuantityDialogFragment.this,np.getValue());
                    }
                })
                .setNegativeButton("Cerrar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogNegativeClick(QuantityDialogFragment.this);
                    }
                });
        np = ((NumberPicker) v.findViewById(R.id.npQuantity));
        np.setMinValue(1);
        np.setMaxValue(100);
        np.setWrapSelectorWheel(false);
        np.setValue(Quantity);
        return builder.create();
    }
}
