package co.edu.unal.directorio.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import co.edu.unal.directorio.R;


import co.edu.unal.directorio.models.Empresa;


public class ListViewAdapter extends BaseAdapter {

    Context mContext;
    LayoutInflater inflater;
    private List<Empresa> listasEmpresas = null;
    private ArrayList<Empresa> arrayList;

    public ListViewAdapter(Context mContext, List<Empresa> listasEmpresas) {
        this.mContext = mContext;
        this.listasEmpresas = listasEmpresas;
        this.inflater = LayoutInflater.from(this.mContext);
        this.arrayList = new ArrayList<>();
        this.arrayList.addAll(listasEmpresas);
    }

    public class ViewHolder{
        TextView id;
        TextView name;
        TextView url;
        TextView phone;
        TextView email;
        TextView products;
        TextView services;
        TextView classification;
    }

    @Override
    public int getCount() {
        return listasEmpresas.size();
    }

    @Override
    public Object getItem(int position) {
        return listasEmpresas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.filtro_buscar_empresas, null);
            // Buscar los datos y presentarlos en el listview_item.xml
            holder.id= (TextView) view.findViewById(R.id.filLayoutId);
            holder.name= (TextView) view.findViewById(R.id.filLayoutName);
            holder.url= (TextView) view.findViewById(R.id.filLayoutUrl);
            holder.phone= (TextView) view.findViewById(R.id.filLayoutPhone);
            holder.email= (TextView) view.findViewById(R.id.filLayoutEmail);
            holder.products= (TextView) view.findViewById(R.id.filLayoutProducts);
            holder.services = (TextView) view.findViewById(R.id.filLayoutServices);
            holder.classification= (TextView) view.findViewById(R.id.filLayoutClass);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Establecer resultados en el TextView
        holder.id.setText("Id: " + listasEmpresas.get(position).getEmpId());
        holder.name.setText("Nombre: " + listasEmpresas.get(position).getNombre());
        holder.url.setText("URL: " + listasEmpresas.get(position).getUrlPaginaWeb());
        holder.phone.setText("Teléfono: " + listasEmpresas.get(position).getTelefono());
        holder.email.setText("E-mail: " + listasEmpresas.get(position).getCorreo());
        holder.products.setText("Productos: " + listasEmpresas.get(position).getProductos());
        holder.services.setText("Servicios: " + listasEmpresas.get(position).getServicios());
        holder.classification.setText("Clasificación: " + listasEmpresas.get(position).getClasificacion());
        return view;
    }

    // Función filtrar
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        listasEmpresas.clear();
        if (charText.length() == 0) {
            listasEmpresas.addAll(arrayList);
        } else {
            for (Empresa empresa : arrayList) {
                if (empresa.getNombre().toLowerCase(Locale.getDefault()).contains(charText) || empresa.getClasificacion().getDescripcionText().toLowerCase(Locale.getDefault()).contains(charText)) {
                    listasEmpresas.add(empresa);
                }
            }
        }
        notifyDataSetChanged();
    }
}
