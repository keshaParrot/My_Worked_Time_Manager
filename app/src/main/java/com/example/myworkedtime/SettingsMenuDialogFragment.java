package com.example.myworkedtime;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.io.IOException;

public class SettingsMenuDialogFragment extends DialogFragment {
    private final String TAG = "SettingsMenuDialogFragment";
    private Switch themePickerSwitch;
    private EditText setRatePerHourEditText;
    private Spinner languagePicker;
    private ImageButton closeMenuButton;
    private TextView backupInfoTextView;
    private Button createBackupButton, loadBackupButton, actualizeRateButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NO_FRAME, 0);
        //getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //хуйня, пів коду треба лапатити
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.settings_dialog_layout, null);
        closeMenuButton = view.findViewById(R.id.closeMenuButton);
        createBackupButton = view.findViewById(R.id.createBackupButton);
        actualizeRateButton = view.findViewById(R.id.actualizeRateButton);
        loadBackupButton = view.findViewById(R.id.loadBackupButton);
        backupInfoTextView = view.findViewById(R.id.infoAboutBackupTextView);
        setRatePerHourEditText = view.findViewById(R.id.setRateEditText);

        actualizeRateButton.setActivated(false);
        actualizeSettings();

        try {
            changeCreateBackupButtonMode();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        closeMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: close settings menu");
                getDialog().dismiss();
            }
        });
        createBackupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    changeCreateBackupButtonMode();
                    if(!changeCreateBackupButtonMode()) HoursWriterClass.getInstance().saveBackupOfProfile();
                    else HoursWriterClass.getInstance().deleteBackupOfProfile();
                    changeCreateBackupButtonMode();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        actualizeRateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double lRateTMP = 0;
                String rateText = setRatePerHourEditText.getText().toString();
                if (!rateText.isEmpty()) {
                    lRateTMP = Double.parseDouble(rateText);
                    SettingsWriterClass.getInstance().setRatePerHour(lRateTMP);
                    Log.e(TAG, "onClick: rate is" + SettingsWriterClass.getInstance().getRatePerHour());
                } else {
                    Log.e(TAG, "onClick: edit text is empty");
                }
            }
        });



        return view;
    }


    public void changeTheme(){

    }

    private void actualizeSettings(){
        setRatePerHourEditText.setText(String.valueOf(SettingsWriterClass.getInstance().getRatePerHour()));

    }
    public boolean changeCreateBackupButtonMode() throws IOException {
        if(SettingsWriterClass.getInstance().getBackupOfProfile().exists()){
            createBackupButton.setText("delete");
            createBackupButton.getBackground().setColorFilter(getResources().getColor(R.color.Red), PorterDuff.Mode.SRC_ATOP);
            loadBackupButton.setEnabled(true);
            backupInfoTextView.setText(SettingsWriterClass.getInstance().getDateOfCreateBackup());
            return true;
        }else{
            createBackupButton.getBackground().clearColorFilter();
            createBackupButton.setText("create");
            loadBackupButton.setEnabled(false);
            backupInfoTextView.setText("backup is not exist");
            return false;
        }
    }
}