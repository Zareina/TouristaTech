package com.zareinaflameniano.sample.ui.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.zareinaflameniano.sample.R;
import com.zareinaflameniano.sample.databinding.FragmentSettingsBinding;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        //load ntn ang sharedprefs
        //PALITAN UNG PACKAGE NAME NG PACKAGE NAME NYO
        SharedPreferences prefs = root.getContext().getSharedPreferences("com.jaysontamayo.drawer", Context.MODE_PRIVATE);

        //load natin yung default setting sa prefs tungkol sa darkmode.
        //false ung default value, kng sakaling wala pang na-save na ganyan dati
        //ung text na "enableDarkMode" ay ung "key"
        //bawat item na sinasave sa SharedPrefs dapat may sariling key
        boolean isOn = prefs.getBoolean("enableDarkMode", false);

        Switch enableDarkMode = root.findViewById(R.id.enableDarkMode);

        //Ung niload nating value kanina, un ung iseset natin dto
        //Para kung naka-on sya dati, naka-on din ung hitsura ng switch natin
        enableDarkMode.setChecked(isOn);

        //Pag nagbago ung Switch, ito ang ma-trigger
        enableDarkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //Ito specifically
                if (isChecked) {
                    //Ito ung nag-eenable ng night mode
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    //Sympre, i-save natin sa SharedPrefs ung bagong value ng switch natin
                    prefs.edit().putBoolean("enableDarkMode", isChecked).commit();
                } else {
                    //Ito ung nag-didisable ng night mode
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    //Sympre, i-save natin sa SharedPrefs ung bagong value ng switch natin
                    prefs.edit().putBoolean("enableDarkMode", isChecked).commit();
                }
            }
        });

        return root;
    }
}