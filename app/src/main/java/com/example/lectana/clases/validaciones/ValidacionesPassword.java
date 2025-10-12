package com.example.lectana.clases.validaciones;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.example.lectana.R;

public class ValidacionesPassword {



    public static boolean esPasswordValida(String password) {

        if (password.length() < 6) {

            return false;
        }

        // Acepta letras, números y caracteres especiales comunes (incluyendo _ - . , ; : !)
        String regex = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[@#$%^&+=!_\\-.,;:?*]).*$";

        return password.matches(regex);
    }


    public static void mostrarEstadoPassword(TextView estadoPassword, boolean esValida, TextView errorPassword, String pass1) {

        if (pass1.length()<6){
            estadoPassword.setText("Mala");
            estadoPassword.setTextColor(Color.RED);
            errorPassword.setText("La contraseña debe tener al menos 6 caracteres");
            errorPassword.setVisibility(View.VISIBLE);
            return;
        }

        if (esValida) {
            estadoPassword.setText("Buena");
            estadoPassword.setTextColor(Color.GREEN);
            errorPassword.setVisibility(View.GONE);
        } else {
            estadoPassword.setText("Mala");
            estadoPassword.setTextColor(Color.RED);
            errorPassword.setVisibility(View.VISIBLE);
            errorPassword.setText("La contraseña debe contener al menos una mayúscula, un número y un carácter especial.");
        }
    }


    public static boolean sonPasswordsIguales(String pass1, String pass2) {
        return pass1 != null && pass1.equals(pass2);
    }

    public static boolean mostrarCoincidenciaPasswords(TextView estado, boolean coinciden, String textoCoinciden, String textoNoCoinciden) {
        if (!coinciden && estado != null) {
            estado.setText(textoNoCoinciden);
            estado.setTextColor(Color.RED);
            estado.setVisibility(View.VISIBLE);
            return false;
        } else if (coinciden && estado != null) {

            estado.setVisibility(View.GONE);
            return true;
        }

        return false;
    }



}
