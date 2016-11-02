package com.example.userasus.serviciosweb;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener{


    Button btnReg,btnActu,btnCons,btnConById,btnEliminar;
    EditText eId,eNombre,eDireccion;
    TextView resultado;

    ManejarServicios manejar=null;
    Boolean on = false;

    ///urls
    String IP="http://192.168.0.3/WebServicesAlumnos"; //DIRECCION BASE, EN ESTE CASO LOCAMENTE, PODRÌA SER UN SERVER

    String    GET=IP+"/obtener_alumnos.php"; // EN EL DOCUMENTO ROOT DEL HOST LOCAL/ ODRIA SER EL DOCUIMENT ROOT DEL SEVER EN WWW
    String GET_ID=IP+"/obtener_alumno_por_id.php";
    String UPDATE=IP+"/actualizar_alumno.php";
    String DELETE=IP+"/borrar_alumno.php";
    String INSERT=IP+"/insertar_alumno.php";
    ///urls

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //enlace
        btnReg = (Button) findViewById(R.id.btnReg);
        btnActu = (Button) findViewById(R.id.btnActu);
        btnCons = (Button) findViewById(R.id.btnCons);
        btnConById = (Button) findViewById(R.id.btnConById);
        btnEliminar = (Button) findViewById(R.id.btnEliminar);

        eId = (EditText) findViewById(R.id.eId);
        eNombre = (EditText) findViewById(R.id.eNombre);
        eDireccion = (EditText) findViewById(R.id.eDireccion);

        resultado = (TextView)findViewById(R.id.tResultados);
        //enlace

        ///metodo delegate
        btnReg.setOnClickListener(this);
        btnActu.setOnClickListener(this);
        btnCons.setOnClickListener(this);
        btnConById.setOnClickListener(this);
        btnEliminar.setOnClickListener(this);
        ///metodo delegate
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnActu:
                manejar = new ManejarServicios();
                manejar.execute(UPDATE,"1");
                on=true;
                break;
            case R.id.btnCons:
                Toast.makeText(this,"hola Consulta todos",Toast.LENGTH_SHORT).show();
                manejar = new ManejarServicios();
                manejar.execute(GET,"2");
                on=true;
                break;
            case R.id.btnConById:
                manejar = new ManejarServicios();
                manejar.execute(GET_ID,"3");
                on=true;
                break;
            case R.id.btnEliminar:
                manejar = new ManejarServicios();
                manejar.execute(DELETE,"4");
                on=true;
                break;
            case R.id.btnReg:
                manejar = new ManejarServicios();
                manejar.execute(INSERT,"5");
                on=true;
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(on){
            manejar.cancel(true);
        }
    }

    public class ManejarServicios extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            //volley
            URL url = null;
            String devuelve="";
            String cadena = params[0];
            if(params[1]=="1"){//actualizar
                publishProgress(params[1]);
            }else if(params[1]=="2"){//Consulta todos
                publishProgress(params[1]);
                try {
                        url = new URL(cadena);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection(); //Abrir la conexión
                        connection.setRequestProperty("User-Agent", "Mozilla/5.0" +
                                " (Linux; Android 1.5; es-ES) Ejemplo HTTP");
                        //connection.setHeader("content-type", "application/json");

                        int respuesta = connection.getResponseCode();
                        StringBuilder result = new StringBuilder();

                        if (respuesta == HttpURLConnection.HTTP_OK){

                            InputStream in = new BufferedInputStream(connection.getInputStream());  // preparo la cadena de entrada
                            BufferedReader reader = new BufferedReader(new InputStreamReader(in));  // la introduzco en un BufferedReader

                            // El siguiente proceso lo hago porque el JSONOBject necesita un String y tengo
                            // que tranformar el BufferedReader a String. Esto lo hago a traves de un
                            // StringBuilder.

                            String line;
                            while ((line = reader.readLine()) != null) {
                                result.append(line);        // Paso toda la entrada al StringBuilder
                            }

                            //Creamos un objeto JSONObject para poder acceder a los atributos (campos) del objeto.
                            JSONObject respuestaJSON = new JSONObject(result.toString());   //Creo un JSONObject a partir del StringBuilder pasado a cadena
                            //Accedemos al vector de resultados

                            String resultJSON = respuestaJSON.getString("estado");   // estado es el nombre del campo en el JSON



                            if (resultJSON=="1"){      // hay alumnos a mostrar
                                JSONArray alumnosJSON = respuestaJSON.getJSONArray("alumnos");   // estado es el nombre del campo en el JSON
                                for(int i=0;i<alumnosJSON.length();i++){
                                    devuelve = devuelve + alumnosJSON.getJSONObject(i).getString("idAlumno") + " " +
                                            alumnosJSON.getJSONObject(i).getString("nombre") + " " +
                                            alumnosJSON.getJSONObject(i).getString("direccion") + "\n";
                                    publishProgress(devuelve);
                                }
                            }
                            else if (resultJSON=="2"){
                                devuelve = "No hay alumnos";
                            }
                        }else{
                            devuelve = "conexion no exitosa";
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    ///https://www.youtube.com/watch?v=Fs0vKEcWz6c
                    return devuelve;

            }else if(params[1]=="3"){//Consulta por id
                publishProgress(params[1]);
            }else if(params[1]=="4"){//eliminar
                publishProgress(params[1]);
            }else if(params[1]=="5"){//registrar
                publishProgress(params[1]);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            resultado.append("resultado: "+values[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            resultado.append("---<<<"+s);
        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
            manejar=null;
        }
    }
}