package com.example.lectana.clases.validaciones;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.example.lectana.R;

public class ValidacionesPassword {



    public static boolean esPasswordValida(String password) {
        String regex = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[@#$%^&+=!]).*$";
        return password.matches(regex);
    }


    public static void mostrarEstadoPassword(TextView estadoPassword, boolean esValida) {
        if (esValida) {
            estadoPassword.setText("Buena");
            estadoPassword.setTextColor(Color.GREEN);
        } else {
            estadoPassword.setText("Mala");
            estadoPassword.setTextColor(Color.RED);
        }
    }


    public static boolean sonPasswordsIguales(String pass1, String pass2) {
        return pass1 != null && pass1.equals(pass2);
    }

    public static void mostrarCoincidenciaPasswords(TextView estado, boolean coinciden, String textoCoinciden, String textoNoCoinciden) {
        if (!coinciden && estado != null) {
            estado.setText(textoNoCoinciden);
            estado.setTextColor(Color.RED);
            estado.setVisibility(View.VISIBLE);
        } else if (coinciden && estado != null) {

            estado.setVisibility(View.GONE);
        }
    }



}
