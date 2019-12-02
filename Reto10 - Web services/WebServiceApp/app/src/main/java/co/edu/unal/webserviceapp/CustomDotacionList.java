package co.edu.unal.webserviceapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import co.edu.unal.webserviceapp.models.Dotacion;
import co.edu.unal.webserviceapp.models.Parque;

public class CustomDotacionList extends BaseAdapter {

    private Activity context;
    private List<Dotacion> dotaciones;

    public CustomDotacionList(Activity context, List<Dotacion> parques) {
        //   super(context, R.layout.row_item, countries);
        this.context = context;
        this.dotaciones = parques;

    }

    @Override
    public int getCount() {
        if(dotaciones.size() <=0)
            return 1;
        return dotaciones.size();
    }

    @Override
    public Object getItem(int posicion) {
        return posicion;
    }

    @Override
    public long getItemId(int posicion) {
        return posicion;
    }

    @Override
    public View getView(int posicion, View convertView, ViewGroup viewGroup) {

        View row = convertView;

        LayoutInflater inflater = context.getLayoutInflater();
        CustomDotacionList.ViewHolderDotacion vh;

        if(convertView == null) {
            vh = new CustomDotacionList.ViewHolderDotacion();
            row = inflater.inflate(R.layout.row_item_dotacion, null, true);
            vh.textNombre = (TextView) row.findViewById(R.id.nombreDotacion);
            vh.textEquipamiento = (TextView) row.findViewById(R.id.tipoEquipamiento);
            vh.textMaterial = (TextView) row.findViewById(R.id.material);
            vh.textCerramiento = (TextView) row.findViewById(R.id.cerramiento);
            // store the holder with the view.
            row.setTag(vh);
        }
        else {
            vh = (CustomDotacionList.ViewHolderDotacion) convertView.getTag();
        }
        vh.textNombre.setText("Dotación: " + dotaciones.get(posicion).getNombre());
        vh.textEquipamiento.setText("Tipo equipamiento: " + dotaciones.get(posicion).getTipoEquipamiento());
        vh.textMaterial.setText("Material: " + dotaciones.get(posicion).getMaterial());
        vh.textCerramiento.setText("Cerramiento: " + dotaciones.get(posicion).getCerramiento());

        return  row;
    }

        public static class ViewHolderDotacion
        {
            TextView textNombre;
            TextView textEquipamiento;
            TextView textMaterial;
            TextView textCerramiento;
        }



    }

