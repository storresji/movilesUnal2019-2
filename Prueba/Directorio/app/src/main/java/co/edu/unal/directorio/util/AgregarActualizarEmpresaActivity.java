package co.edu.unal.directorio.util;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import co.edu.unal.directorio.MainActivity;
import co.edu.unal.directorio.R;
import co.edu.unal.directorio.models.Clasificacion;
import co.edu.unal.directorio.models.Empresa;

public class AgregarActualizarEmpresaActivity extends AppCompatActivity {

    private static final String EXTRA_EMP_ID = "co.edu.unal.empId";
    private static final String EXTRA_ADD_UPDATE = "co.edu.unal.add_update";
    private static final String DIALOG_DATE = "DialogDate";

    private RadioGroup radioGroup;
    private RadioButton consultoriaRadioButton;
    private RadioButton sofwareRadioButton;
    private RadioButton fabricaRadioButton;
    private EditText nombreEditText;
    private EditText urlEditText;
    private EditText telefonoEditText;
    private EditText correoEditText;
    private EditText productosEditText;
    private EditText serviciosEditText;
    private Button addUpdateButton;
    private Empresa newEmpresa;
    private Empresa oldEmpresa;
    private String mode;
    private long empId;
    private EmpresaOperations empresaData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update_employee);

        newEmpresa = new Empresa();
        oldEmpresa = new Empresa();
        nombreEditText = (EditText)findViewById(R.id.editar_nombre);
        urlEditText = (EditText)findViewById(R.id.editar_url);
        telefonoEditText = (EditText) findViewById(R.id.editar_telefono);
        correoEditText = (EditText) findViewById(R.id.editar_correo);
        productosEditText = (EditText) findViewById(R.id.editar_productos);
        serviciosEditText = (EditText) findViewById(R.id.editar_servicios);

        radioGroup = (RadioGroup) findViewById(R.id.radio_clasificacion);
        consultoriaRadioButton = (RadioButton) findViewById(R.id.radio_consultoria);
        sofwareRadioButton = (RadioButton) findViewById(R.id.radio_desarrollo);
        fabricaRadioButton = (RadioButton) findViewById(R.id.radio_fabrica);

        addUpdateButton = (Button)findViewById(R.id.button_add_update_empresa);
        empresaData = new EmpresaOperations(this);
        empresaData.open();


        mode = getIntent().getStringExtra(EXTRA_ADD_UPDATE);

        if(mode.equals("Update")){

            addUpdateButton.setText("Actualizar");
            empId = getIntent().getLongExtra(EXTRA_EMP_ID,0);

            inicializarEmpresa(empId);

        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if (checkedId == R.id.radio_consultoria) {
                    newEmpresa.setClasificacion(Clasificacion.CONSULTORIA);
                    if(mode.equals("Update")){
                        oldEmpresa.setClasificacion(Clasificacion.CONSULTORIA);
                    }
                } else if (checkedId == R.id.radio_desarrollo) {
                    newEmpresa.setClasificacion(Clasificacion.DESARROLLO_MEDIDA);
                    if(mode.equals("Update")){
                        oldEmpresa.setClasificacion(Clasificacion.DESARROLLO_MEDIDA);
                    }

                }else if(checkedId == R.id.radio_fabrica){
                    newEmpresa.setClasificacion(Clasificacion.FABRICA_SOFTWARE);
                    if(mode.equals("Update"))
                        oldEmpresa.setClasificacion(Clasificacion.FABRICA_SOFTWARE);
                }
            }

        });


        addUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mode.equals("agregar")) {
                    newEmpresa.setNombre(nombreEditText.getText().toString());
                    newEmpresa.setUrlPaginaWeb(urlEditText.getText().toString());
                    newEmpresa.setTelefono(telefonoEditText.getText().toString());
                    newEmpresa.setCorreo(correoEditText.getText().toString());
                    newEmpresa.setProductos(productosEditText.getText().toString());
                    newEmpresa.setServicios(serviciosEditText.getText().toString());

                    empresaData.addEmpresa(newEmpresa);
                    Toast t = Toast.makeText(AgregarActualizarEmpresaActivity.this, "Empresa "+ newEmpresa.getNombre() + " ha sido agregada exitosamente!", Toast.LENGTH_SHORT);
                    t.show();
                    Intent i = new Intent(AgregarActualizarEmpresaActivity.this, MainActivity.class);
                    startActivity(i);
                }else {
                    oldEmpresa.setNombre(nombreEditText.getText().toString());
                    oldEmpresa.setUrlPaginaWeb(urlEditText.getText().toString());
                    oldEmpresa.setTelefono(telefonoEditText.getText().toString());
                    oldEmpresa.setCorreo(correoEditText.getText().toString());
                    oldEmpresa.setProductos(productosEditText.getText().toString());
                    oldEmpresa.setServicios(serviciosEditText.getText().toString());

                    empresaData.updateEmpresa(oldEmpresa);
                    Toast t = Toast.makeText(AgregarActualizarEmpresaActivity.this, "Empresa "+ oldEmpresa.getNombre() + " ha sido actualizada existosamente!", Toast.LENGTH_SHORT);
                    t.show();
                    Intent i = new Intent(AgregarActualizarEmpresaActivity.this, MainActivity.class);
                    startActivity(i);
                }
            }
        });
    }

    private void inicializarEmpresa(long empId) {

        oldEmpresa = empresaData.getEmpresa(empId);

        if(oldEmpresa != null) {

        nombreEditText.setText(oldEmpresa.getNombre());
        urlEditText.setText(oldEmpresa.getUrlPaginaWeb());
        telefonoEditText.setText(oldEmpresa.getTelefono());
        correoEditText.setText(oldEmpresa.getCorreo());
        productosEditText.setText(oldEmpresa.getProductos());
        serviciosEditText.setText(oldEmpresa.getServicios());

        if(oldEmpresa.getClasificacion().equals(Clasificacion.CONSULTORIA))
            radioGroup.check(R.id.radio_consultoria);
        else if(oldEmpresa.getClasificacion().equals(Clasificacion.DESARROLLO_MEDIDA))
            radioGroup.check(R.id.radio_desarrollo);
        else if(oldEmpresa.getClasificacion().equals(Clasificacion.FABRICA_SOFTWARE))
            radioGroup.check(R.id.radio_fabrica);

        }else{
            Toast t = Toast.makeText(this, "La empresa con id = " + empId + " no existe", Toast.LENGTH_SHORT);
            t.show();

        }
    }

}
