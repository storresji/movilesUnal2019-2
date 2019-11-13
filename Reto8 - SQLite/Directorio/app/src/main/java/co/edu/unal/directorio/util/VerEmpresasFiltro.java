package co.edu.unal.directorio.util;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Path;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SearchView;

import co.edu.unal.directorio.R;

import java.util.ArrayList;
import java.util.List;

import co.edu.unal.directorio.models.Empresa;

public class VerEmpresasFiltro extends AppCompatActivity  implements SearchView.OnQueryTextListener {

    ListView list;
    ListViewAdapter adapter;
    SearchView editsearch;
    private EmpresaOperations companyOps;
    List<Empresa> companies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_empresas_filtro);

        list = (ListView) findViewById(R.id.listViewCompaniesFilter);

        companyOps = new EmpresaOperations(this);
        companyOps.open();
        companies = companyOps.getAllEmpresas();
        companyOps.close();

        adapter = new ListViewAdapter(this, companies);
        list.setAdapter(adapter);

        editsearch = (SearchView) findViewById(R.id.searchView);
        editsearch.setOnQueryTextListener(this);
        editsearch.setQueryHint("Escriba el nombre o clasificación");
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String text = newText;
        adapter.filter(text);
        return false;
    }
}
