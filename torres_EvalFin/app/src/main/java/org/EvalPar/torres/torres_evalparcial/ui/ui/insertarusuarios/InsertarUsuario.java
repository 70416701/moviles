package org.EvalPar.torres.torres_evalparcial.ui.ui.insertarusuarios;

import androidx.lifecycle.ViewModelProviders;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.EvalPar.torres.torres_evalparcial.R;
import org.EvalPar.torres.torres_evalparcial.ui.ui.entidad.AdminSQLiteOpenHelper;

public class InsertarUsuario extends Fragment implements View.OnClickListener{
    EditText et_id, ed_nombres, ed_apellidos, ed_telefono, ed_email, ed_pagina;
    Button btnRegistrar;
    private InsertarUsuarioViewModel mViewModel;

    public static InsertarUsuario newInstance() {
        return new InsertarUsuario();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.insertar_usuario_fragment, container, false);

        ed_nombres =v.findViewById(R.id.et_registro_nombres);
        ed_apellidos =v.findViewById(R.id.et_registro_apellidos);
        ed_telefono =v.findViewById(R.id.et_registro_telefono);
        ed_email =v.findViewById(R.id.et_registro_email);
        ed_pagina =v.findViewById(R.id.et_registro_pagina);
        btnRegistrar=v.findViewById(R.id.btnIUSregusuario);
        btnRegistrar.setOnClickListener(this);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(InsertarUsuarioViewModel.class);
        // TODO: Use the ViewModel
    }
    @Override
    public void onClick(View v) {
        Insertar();
    }

    public void Insertar() {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getContext(),"db_gestion_usuarios",null, 1);
        SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();

        String nombre = ed_nombres.getText().toString();
        String apellido = ed_apellidos.getText().toString();
        String telefono = ed_telefono.getText().toString();
        String email = ed_email.getText().toString();
        String pagina = ed_pagina.getText().toString();

        if (!nombre.isEmpty() && !apellido.isEmpty() && !telefono.isEmpty() && !email.isEmpty() && !pagina.isEmpty()) {
            ContentValues insert = new ContentValues();

            insert.put("nombre",nombre);
            insert.put("apellido",apellido);
            insert.put("telefono",telefono);
            insert.put("email",email);
            insert.put("pagina",pagina);

            BaseDeDatos.insert("usuarios", null, insert);

            BaseDeDatos.close();

            ed_nombres.setText("");
            ed_apellidos.setText("");
            ed_telefono.setText("");
            ed_email.setText("");
            ed_pagina.setText("");
            Toast.makeText(getContext(), "Registro exitoso", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Debes llenar todos lo campos", Toast.LENGTH_SHORT).show();
        }
    }

}
