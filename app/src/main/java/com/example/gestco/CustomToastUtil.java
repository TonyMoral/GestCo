package com.example.gestco;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CustomToastUtil {

    //Constante para el icono
    private static final int ICONO_TOAST = R.drawable.icons;
    public static void  mostrarToastPersonalizado (Context context, String mensaje){
        //Personalizamos el layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.custom_toast, null);

        //Establecemos la imágen y el texto del layout personalizado
        ImageView toastImage = layout.findViewById(R.id.toast_image);
        toastImage.setImageResource(ICONO_TOAST);

        TextView textToast = layout.findViewById(R.id.toast_text);
        textToast.setText(mensaje);

        //Creamos el toast personalizado
        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.setView(layout);

        //Mostramos el toast personalizado
        toast.show();

    }
}
