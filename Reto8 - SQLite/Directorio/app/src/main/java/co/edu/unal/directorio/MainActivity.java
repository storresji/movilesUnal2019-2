package co.edu.unal.directorio;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import co.edu.unal.directorio.models.Empresa;
import co.edu.unal.directorio.util.AgregarActualizarEmpresaActivity;
import co.edu.unal.directorio.util.EmpresaOperations;
import co.edu.unal.directorio.util.VerEmpresasActivity;

public class MainActivity extends AppCompatActivity {

    private Button agregarEmpresaButton;
    private Button editarEmpresaButton;
    private Button eliminarEmpresaButton;
    private Button verEmpresasButton;
    private EmpresaOperations empresaOperations;
    private static final String EXTRA_EMP_ID = "co.edu.unal.empId";
    private static final String EXTRA_ADD_UPDATE = "co.edu.unal.add_update";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        empresaOperations = new EmpresaOperations(this);

        agregarEmpresaButton = (Button) findViewById(R.id.button_agregar_empresa);
        editarEmpresaButton = (Button) findViewById(R.id.button_editar_empresa);
        eliminarEmpresaButton = (Button) findViewById(R.id.button_eliminar_empresa);
        verEmpresasButton = (Button)findViewById(R.id.button_ver_empresas);

        agregarEmpresaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AgregarActualizarEmpresaActivity.class);
                i.putExtra(EXTRA_ADD_UPDATE, "agregar");
                startActivity(i);
            }
        });

        editarEmpresaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEmpIdAndUpdateEmp();
            }
        });
        eliminarEmpresaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEmpIdAndRemoveEmp();
            }
        });
        verEmpresasButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, VerEmpresasActivity.class);
                startActivity(i);
            }
        });

    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.employee_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_item_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_item_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/

    public void getEmpIdAndUpdateEmp(){

        LayoutInflater li = LayoutInflater.from(this);
        View getEmpIdView = li.inflate(R.layout.dialog_get_emp_id, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // set dialog_get_emp_id.xml to alertdialog builder
        alertDialogBuilder.setView(getEmpIdView);

        final EditText userInput = (EditText) getEmpIdView.findViewById(R.id.editTextDialogUserInput);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // get user input and set it to result
                        // edit text
                        Intent i = new Intent(MainActivity.this, AgregarActualizarEmpresaActivity.class);
                        i.putExtra(EXTRA_ADD_UPDATE, "Update");
                        i.putExtra(EXTRA_EMP_ID, Long.parseLong(userInput.getText().toString()));
                        startActivity(i);
                    }
                }).create()
                .show();

    }

    public void getEmpIdAndRemoveEmp(){

        LayoutInflater li = LayoutInflater.from(this);
        View getEmpIdView = li.inflate(R.layout.dialog_get_emp_id, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // set dialog_get_emp_id.xml to alertdialog builder
        alertDialogBuilder.setView(getEmpIdView);

        final EditText userInput = (EditText) getEmpIdView.findViewById(R.id.editTextDialogUserInput);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // get user input and set it to result
                        // edit text
                        //empresaOperations = new EmpresaOperations(MainActivity.this);
                        Empresa empresa = empresaOperations.getEmpresa(Long.parseLong(userInput.getText().toString()));
                        empresaOperations.eliminarEmpresa(empresa);
                        Toast t = Toast.makeText(MainActivity.this,"Empresa eliminada exitosamente: " + empresa.getEmpId(),Toast.LENGTH_SHORT);
                        t.show();
                    }
                }).create()
                .show();

    }

    @Override
    protected void onResume() {
        super.onResume();
        empresaOperations.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
        empresaOperations.close();
    }
}
