package co.edu.unal.directorio.util;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import java.util.List;

import co.edu.unal.directorio.R;
import co.edu.unal.directorio.models.Empresa;

public class VerEmpresasActivity extends ListActivity {

    private EmpresaOperations empresaOperations;
    List<Empresa> empresas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_empresas);
        empresaOperations = new EmpresaOperations(this);
        empresaOperations.open();
        empresas = empresaOperations.getAllEmpresas();
        empresaOperations.close();
        ArrayAdapter<Empresa> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, empresas);
        setListAdapter(adapter);
    }
}
