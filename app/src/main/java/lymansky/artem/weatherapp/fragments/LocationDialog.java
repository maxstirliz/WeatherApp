package lymansky.artem.weatherapp.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import lymansky.artem.weatherapp.R;

public class LocationDialog extends DialogFragment {

    private DialogButtonsListener mListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        return new AlertDialog.Builder(getActivity())
                .setIcon(R.drawable.ic_my_location_blue)
                .setTitle(R.string.dialog_title)
                .setPositiveButton(R.string.positive_answer,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mListener.onDialogButtonClick(which);
                            }
                        })
                .setNegativeButton(R.string.negative_answer,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mListener.onDialogButtonClick(which);
                            }
                        })
                .create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (DialogButtonsListener) context;
    }

    public interface DialogButtonsListener {
        void onDialogButtonClick(int button);
    }
}
