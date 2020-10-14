package org.EvalPar.torres.torres_evalparcial.ui.ui.buscarusuarios;

import androidx.lifecycle.ViewModelProviders;

import android.content.ContentValues;
import android.content.IntentSender;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.style.UpdateAppearance;
import android.text.style.UpdateLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.EvalPar.torres.torres_evalparcial.R;
import org.EvalPar.torres.torres_evalparcial.ui.ui.entidad.AdminSQLiteOpenHelper;
import org.EvalPar.torres.torres_evalparcial.ui.ui.entidad.Usuarios;

import java.util.ArrayList;
import java.util.List;

public class BuscarUsuario extends Fragment implements View.OnClickListener {
    ListView lvUsuarios;
    EditText et_buscar_id, et_buscar_nombres, et_buscar_apellidos, et_buscar_telefono, et_buscar_email, et_buscar_pagina;
    Button btnEditar, btnEliminar;

    List<String> lista_Usuarios ;
    List<Usuarios> users ;

    AdminSQLiteOpenHelper admin;
    SQLiteDatabase BaseDatos;
    Cursor fila;

    private BuscarUsuarioViewModel mViewModel;

    public static BuscarUsuario newInstance() {
        return new BuscarUsuario();
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.buscar_usuarios_fragment, container, false);
        lvUsuarios = v.findViewById(R.id.lvUsuarios);

        et_buscar_id =v.findViewById(R.id.et_buscar_id);
        et_buscar_nombres =v.findViewById(R.id.et_buscar_nombres);
        et_buscar_apellidos =v.findViewById(R.id.et_buscar_apellidos);
        et_buscar_telefono =v.findViewById(R.id.et_buscar_telefono);
        et_buscar_email =v.findViewById(R.id.et_buscar_email);
        et_buscar_pagina =v.findViewById(R.id.et_buscar_pagina);
        btnEditar = v.findViewById(R.id.btn_editar_usuario);
        btnEditar.setOnClickListener(this);
        btnEliminar= v.findViewById(R.id.btn_eliminar_usuario);
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Eliminar();
            }
        });

        admin = new AdminSQLiteOpenHelper(getContext(),"db_gestion_usuarios",null,1);

        Consultar_usuarios();
        lvUsuarios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                et_buscar_id.setText(users.get(position).getId().toString());
                et_buscar_nombres.setText(users.get(position).getNombre().toString());
                et_buscar_apellidos.setText(users.get(position).getApellido().toString());
                et_buscar_telefono.setText(users.get(position).getTelefono().toString());
                et_buscar_email.setText(users.get(position).getEmail().toString());
                et_buscar_pagina.setText(users.get(position).getPagina().toString());
            }
        });
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(BuscarUsuarioViewModel.class);
        // TODO: Use the ViewModel
    }

    public void listar_consulta_usuarios () {
        lista_Usuarios = new ArrayList<String>();
        for (Integer i=0; i<users.size();i++) {
            lista_Usuarios.add(users.get(i).getId()+ " " + users.get(i).getNombre() +" "+ users.get(i).getApellido());
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.listview_2,lista_Usuarios);
            lvUsuarios.setAdapter(adapter);
        }
    }

    public void Consultar_usuarios() {
        BaseDatos = admin.getWritableDatabase();
        Usuarios usuarios = null;
        users  = new ArrayList<Usuarios>();
        fila = BaseDatos.rawQuery("select  * from usuarios ",null);
        while(fila.moveToNext()) {
            usuarios = new Usuarios(
                    fila.getInt(0),
                    fila.getString(1),
                    fila.getString(2),
                    fila.getString(3),
                    fila.getString(4),
                    fila.getString(5));
            users.add(usuarios);
        }
        listar_consulta_usuarios ();
    }

    public void Editar () {
        admin = new AdminSQLiteOpenHelper(getContext(),"db_gestion_usuarios",null,1);
        BaseDatos = admin.getWritableDatabase();

        String id_editar = et_buscar_id.getText().toString();
        String nombres = et_buscar_nombres.getText().toString();
        String apellidos = et_buscar_apellidos.getText().toString();
        String telefono = et_buscar_telefono.getText().toString();
        String email = et_buscar_email.getText().toString();
        String pagina = et_buscar_pagina.getText().toString();

        if (!id_editar.isEmpty() && !nombres.isEmpty() && !apellidos.isEmpty() && !telefono.isEmpty() && !email.isEmpty() && !pagina.isEmpty()){
            ContentValues edicion = new ContentValues();
            //edicion.put("codigo",id_editar);
            edicion.put("nombre",nombres);
            edicion.put("apellido",apellidos);
            edicion.put("telefono",telefono);
            edicion.put("email",email);
            edicion.put("pagina",pagina);

            int cantidad = BaseDatos.update("usuarios",edicion,"codigo = "+ id_editar,null);
            BaseDatos.close();
            if ( cantidad == 1) {
                Toast.makeText(getContext(), "Se ha modificado un usuario", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "El usuario no existe", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "Debes rellenar todos lo campos", Toast.LENGTH_SHORT).show();
        }
    }

    public void Eliminar () {
        admin = new AdminSQLiteOpenHelper(getContext(),"db_gestion_usuarios",null,1);
        BaseDatos = admin.getWritableDatabase();

        String codigo = et_buscar_id.getText().toString();

        if (!codigo.isEmpty()) {
            int cantidad = BaseDatos.delete("usuarios", "codigo="+codigo,null );
            BaseDatos.close();

            et_buscar_id.setText("");
             et_buscar_nombres.setText("");
            et_buscar_apellidos.setText("");
            et_buscar_telefono.setText("");
            et_buscar_email.setText("");
            et_buscar_pagina.setText("");

            if (cantidad == 1) {
                Toast.makeText(getContext(), "Usuario eliminado", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "No se encuentra registros", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(getContext(), "Debes introducir un texto", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) { Editar(); }
}
