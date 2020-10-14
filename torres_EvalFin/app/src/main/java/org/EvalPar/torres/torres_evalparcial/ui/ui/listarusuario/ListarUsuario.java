package org.EvalPar.torres.torres_evalparcial.ui.ui.listarusuario;

import androidx.lifecycle.ViewModelProviders;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

public class ListarUsuario extends Fragment {
    Spinner spiUsuarios;
    ListView lvUsuariosAccion;

    List<String> lista_Usuarios ;
    List<Usuarios> users ;

    AdminSQLiteOpenHelper admin;
    SQLiteDatabase BaseDatos;
    Cursor fila;

    EditText et_correo_destino, et_correo_asunto, et_correo_cuerpo, et_sms_destino, et_sms_cuerpo;
    Button enviar_correo, enviar_sms, llamar;

    private ListarUsuarioViewModel mViewModel;

    public static ListarUsuario newInstance() {
        return new ListarUsuario();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.listar_usuarios_fragment, container, false);
        spiUsuarios = v.findViewById(R.id.spiUsuarios);
        lvUsuariosAccion = v.findViewById(R.id.lvUsuariosAccion);
        admin = new AdminSQLiteOpenHelper(getContext(),"db_gestion_usuarios",null,1);
        Consultar_usuarios();
        spiUsuarios.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String [] contacto = {
                        users.get(position).getNombre().toString(),
                        users.get(position).getTelefono().toString(),
                        users.get(position).getEmail().toString(),
                        users.get(position).getPagina().toString()};
                ArrayAdapter<String> contacto_adapter = new ArrayAdapter<String>(getContext(), R.layout.listview_1,contacto);
                lvUsuariosAccion.setAdapter(contacto_adapter);

                lvUsuariosAccion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //String accion = lvUsuariosAccion.getItemAtPosition(position).toString();
                        switch (position){
                            case 2 :
                                Dialog correo = new Dialog(getContext());
                                correo.setTitle("Correo");
                                correo.setCancelable(true);
                                correo.setContentView(R.layout.correo);
                                correo.show();

                                String correo_destino = lvUsuariosAccion.getItemAtPosition(position).toString();

                                et_correo_destino = correo.findViewById(R.id.et_correo_destino);
                                et_correo_destino.setText(correo_destino);
                                et_correo_asunto = correo.findViewById(R.id.et_correo_asunto);
                                et_correo_cuerpo = correo.findViewById(R.id.et_correo_cuerpo);
                                enviar_correo = correo.findViewById(R.id.btnEnviarCorreo);

                                enviar_correo.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String correo_destino = et_correo_destino.getText().toString();
                                        String correo_asunto = et_correo_asunto.getText().toString();
                                        String correo_cuerpo = et_correo_cuerpo.getText().toString();

                                        Intent email = new Intent();
                                        email.setAction(Intent.ACTION_SEND);
                                        email.putExtra(Intent.EXTRA_TEXT, correo_cuerpo);
                                        email.setType("message/rfc822");
                                        email.putExtra(Intent.EXTRA_EMAIL, new String[]{correo_destino});
                                        email.putExtra(Intent.EXTRA_SUBJECT, correo_asunto);
                                        Intent shareInten = Intent.createChooser(email,null);
                                        startActivity(shareInten);
                                    }
                                });
                                ;
                            break;
                            case 1 :
                                Dialog sms = new Dialog(getContext());
                                sms.setTitle("SMS");
                                sms.setCancelable(true);
                                sms.setContentView(R.layout.sms);
                                sms.show();

                                String sms_destino = lvUsuariosAccion.getItemAtPosition(position).toString();

                                et_sms_destino = sms.findViewById(R.id.et_sms_destino);
                                et_sms_destino.setText(sms_destino);
                                et_sms_cuerpo = sms.findViewById(R.id.et_sms_cuerpo);
                                enviar_sms = sms.findViewById(R.id.btnEnviarSMS);

                                enviar_sms.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        String sms_destino = et_sms_destino.getText().toString();
                                        String sms_cuerpo = et_sms_cuerpo.getText().toString();

                                        Uri uri = Uri.parse("smsto:"+sms_destino);
                                        Intent sms = new Intent(Intent.ACTION_SENDTO,uri);
                                        sms.putExtra("sms_body",sms_cuerpo);
                                        startActivity(sms);
                                    }
                                });

                                llamar = sms.findViewById(R.id.btnLlamar);
                                llamar.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String numero_llamada = et_sms_destino.getText().toString();

                                        Uri numero = Uri.parse("tel:" + numero_llamada);
                                        Intent hacerllamada = new Intent(Intent.ACTION_DIAL, numero);
                                        startActivity(hacerllamada);
                                    }
                                });
                                break;
                            case 3:
                                String pagina_url = lvUsuariosAccion.getItemAtPosition(position).toString();

                                Uri uriurl = Uri.parse("http://"+pagina_url);
                                Intent pagina = new Intent(Intent.ACTION_VIEW,uriurl);
                                startActivity(pagina);
                                break;
                            default:
                                Toast.makeText(getContext(), "Elige alguna acción!", Toast.LENGTH_SHORT).show();
                        }


                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ListarUsuarioViewModel.class);
        // TODO: Use the ViewModel
    }

    public void listar_consulta_usuarios () {
        lista_Usuarios = new ArrayList<String>();
        for (Integer i=0; i<users.size();i++) {
            lista_Usuarios.add(users.get(i).getId()+ " " + users.get(i).getNombre() +" "+ users.get(i).getApellido());
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_1,lista_Usuarios);
            spiUsuarios.setAdapter(adapter);
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
}
