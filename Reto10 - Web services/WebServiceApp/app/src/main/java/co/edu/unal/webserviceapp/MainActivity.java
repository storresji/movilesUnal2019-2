package co.edu.unal.webserviceapp;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Parcelable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.Buffer;
import java.nio.charset.CodingErrorAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import android.app.ProgressDialog;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;

import co.edu.unal.webserviceapp.models.Dotacion;
import co.edu.unal.webserviceapp.models.Parque;

import static android.content.ContentValues.TAG;


public class MainActivity extends AppCompatActivity {

    private Button btnSubmit;
    private String responseText;
    private StringBuffer response;
    private URL url;
    private Activity activity;
    private List<Parque> parques = new ArrayList<>();
    private ProgressDialog processDialog;
    private ListView listView;
    private Spinner localidadesSpinner;

    private LinearLayout mLinearLayout;
    private PopupWindow mPopupWindow;


    private String path = "https://www.datos.gov.co/resource/ds3e-3tc7.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        activity = this;
        addListenerOnSpinnerItemSelection();
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        listView = (ListView) findViewById(R.id.listViewCompaniesFilter);

        btnSubmit.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                parques.clear();
                //Call WebService
                new GetServerData().execute();
            }
        });

        mLinearLayout = (LinearLayout) findViewById(R.id.linear_layout);


        /*FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/


    }

    public void addListenerOnSpinnerItemSelection() {
        localidadesSpinner = (Spinner) findViewById(R.id.spinner1);

        /*ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, R);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(dataAdapter);*/

        localidadesSpinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }

    class GetServerData extends AsyncTask{

        private List<String> codigos = new ArrayList<>();
        private List<String> equipamientos = new ArrayList<>();
        private List<String> dotaciones = new ArrayList<>();
        private final String CODIGO = "codigo";
        private String TIPO = "tipo";
        private String NOMBRE = "nombre_del_parque";
        private String LOCALIDAD = "localidad";
        private String EQUIPAMIENTO = "equipamiento";
        private String AREA = "area";
        private String DOTACION = "dotaci_n";
        private String MATERIAL = "material";
        private String CERRAMIENTO = "cerramiento";

        /*        "tipo": "Parque de escala metropolitano",
                "localidad": "Santafe",
                "nombre_del_parque": "PARQUE NACIONAL (PM-2A) ENRIQUE OLAYA HERRERA ( SECTOR HISTORICO )",
                "codigo_dotacion": "13585",
                "equipamiento": "CANCHAS DEPORTIVAS",
                "aprovechamiento_economico": "Si",
                "area": "610",
                "dotaci_n": "Tennis",
                "material": "Polvo de ladrillo",
                "cerramiento": "Parcial",
                "camerino": "No",
                "capacidad": "0",
                "carril": "0",
                "ba_o": "0"*/
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            //Showing process dialog
            processDialog = new ProgressDialog(MainActivity.this);
            processDialog.setMessage("Obteniendo datos...");
            processDialog.setCancelable(false);
            processDialog.show();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            return getWebServiceResponseData();
        }

        @Override
        protected void onPostExecute(Object o){
            super.onPostExecute(o);

            //Dismiss the progress dialog
            if(processDialog.isShowing())
                processDialog.dismiss();
            //For populating list data
            CustomParqueList customParqueList = new CustomParqueList(activity, parques);
            listView.setAdapter(customParqueList);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int posicion, long l) {
                    Toast.makeText(getApplicationContext(), "Usted seleccionó " + parques.get(posicion).getNombre(), Toast.LENGTH_SHORT).show();

                    LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);

                    // Inflate the custom layout/view
                    View customView = inflater.inflate(R.layout.custom_layout,null);
                    ListView dotacionesView = (ListView) customView.findViewById(R.id.listViewDotaciones);
                    if(dotacionesView == null)
                        System.out.println("Dotacionesview null");
                    else
                        System.out.println("Dotacionesview no null");
                    CustomDotacionList customDotacionList = new CustomDotacionList(activity, parques.get(posicion).getDotaciones());
                    dotacionesView.setAdapter(customDotacionList);
                    // Initialize a new instance of popup window
                    mPopupWindow = new PopupWindow(
                            customView,
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT
                    );

                    // Set an elevation value for popup window
                    // Call requires API level 21
                        mPopupWindow.setElevation(5.0f);


                    // Get a reference for the custom view close button
                    Button closeButton = (Button) customView.findViewById(R.id.ib_close);

                    // Set a click listener for the popup window close button
                    closeButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Dismiss the popup window
                            mPopupWindow.dismiss();
                        }
                    });

                /*
                    public void showAtLocation (View parent, int gravity, int x, int y)
                        Display the content view in a popup window at the specified location. If the
                        popup window cannot fit on screen, it will be clipped.
                        Learn WindowManager.LayoutParams for more information on how gravity and the x
                        and y parameters are related. Specifying a gravity of NO_GRAVITY is similar
                        to specifying Gravity.LEFT | Gravity.TOP.

                    Parameters
                        parent : a parent view to get the getWindowToken() token from
                        gravity : the gravity which controls the placement of the popup window
                        x : the popup's x location offset
                        y : the popup's y location offset
                */
                    // Finally, show the popup window at the center location of root relative layout
                    mPopupWindow.showAtLocation(mLinearLayout, Gravity.CENTER,0,0);

                }
            });
        }

        protected Void getWebServiceResponseData(){
            try{
                Object localidadSeleccionada = localidadesSpinner.getSelectedItem();
                String completePath = path;
                if(localidadSeleccionada != null){
                    String localidad = String.valueOf(localidadSeleccionada);
                    completePath += "?localidad=" + localidad;
                }
                url = new URL(completePath);
                Log.d(TAG, "ServerData: " + path);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");

                int responseCode = conn.getResponseCode();
                Log.d(TAG, "Response code: " + responseCode);

                if(responseCode == HttpsURLConnection.HTTP_OK){
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String output;
                    response = new StringBuffer();
                    while((output = in.readLine()) != null)
                        response.append(output);
                    in.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            responseText = response.toString();
            Log.d(TAG, "data: " + responseText);

            try{
                JSONArray jsonArray = new JSONArray(responseText);
                for(int i=0; i<jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String codigo = jsonObject.getString(CODIGO);
                    String tipo = jsonObject.getString(TIPO);
                    String nombre = jsonObject.getString(NOMBRE);
                    String localidad = jsonObject.getString(LOCALIDAD);
                    String equipamiento = jsonObject.getString(EQUIPAMIENTO);
                    String area = jsonObject.getString(AREA);
                    String dotacion = jsonObject.getString(DOTACION);
                    String material = jsonObject.getString(MATERIAL);
                    String cerramiento = jsonObject.getString(CERRAMIENTO);

                    Parque parque = new Parque(codigo, tipo, nombre, localidad);
                    Dotacion dotacionObject = new Dotacion(dotacion, equipamiento, material, cerramiento, area);

                    if(parques.contains(parque)){
                        int id = parques.indexOf(parque);
                        parques.get(id).addDotacion(dotacionObject);

                    }else{
                        ArrayList<Dotacion> dotaciones = new ArrayList<>();
                        dotaciones.add(dotacionObject);
                        parque.setDotaciones(dotaciones);
                        parques.add(parque);
                    }


                    /*if(!equipamientos.contains(equipamiento))
                        equipamientos.add(equipamiento);*/

                    if(!dotaciones.contains(localidad))
                        dotaciones.add(localidad);

                }
                System.out.println("Localidades: ");
                System.out.println(dotaciones);
                /*
                System.out.println("Equipamientos: ");
                System.out.println(equipamientos);

                System.out.println("Dotaciones: ");
                System.out.println(dotaciones);*/
            }catch (JSONException e){
                e.printStackTrace();
            }
            return null;
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
