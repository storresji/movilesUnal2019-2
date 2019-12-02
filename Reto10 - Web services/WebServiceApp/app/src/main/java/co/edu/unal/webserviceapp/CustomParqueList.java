package co.edu.unal.webserviceapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import co.edu.unal.webserviceapp.models.Parque;

public class CustomParqueList extends BaseAdapter {

    private Activity context;
    private List<Parque> parques;

    public CustomParqueList(Activity context, List<Parque> parques) {
        //   super(context, R.layout.row_item, countries);
        this.context = context;
        this.parques = parques;

    }

    @Override
    public int getCount() {
        if(parques.size() <=0)
            return 1;
        return parques.size();
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
        ViewHolder vh;

        if(convertView == null) {
            vh = new ViewHolder();
            row = inflater.inflate(R.layout.row_item, null, true);
            vh.textViewId = (TextView) row.findViewById(R.id.textViewId);
            vh.textViewParque = (TextView) row.findViewById(R.id.nombreParque);
            vh.textTipoParque = (TextView) row.findViewById(R.id.tipoParque);
            vh.textLocalidadParque = (TextView) row.findViewById(R.id.localidadParque);
            // store the holder with the view.
            row.setTag(vh);
        }
        else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.textLocalidadParque.setText("Localidad: " + parques.get(posicion).getLocalidad());
        vh.textTipoParque.setText("Tipo: " + parques.get(posicion).getTipo());
        vh.textViewParque.setText("" + parques.get(posicion).getNombre());
        vh.textViewId.setText("Código: " + parques.get(posicion).getCodigo());

        return  row;
    }

    public static class ViewHolder
    {
        TextView textViewId;
        TextView textViewParque;
        TextView textTipoParque;
        TextView textLocalidadParque;
    }


}
