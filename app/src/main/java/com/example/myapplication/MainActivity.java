package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Toast;

import com.itextpdf.text.BadElementException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private String[]header={"Control de Roedores","Control de Insectos","Control de Microorganismos"};
    private String nEmpresa="NOMBRRE EMPRESA: ";
    private String dEmpresa="DIRECCION EMPRESA: ";
    private String fecha="FECHA: ";
    private String rbd="RBD: ";

    private TemplatePDF templatePDF;
    private CheckBox checkBox1;







    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




    }

    public void pdfApp(View view) {



        final TemplatePDF templatePDF = new TemplatePDF(getApplicationContext());


        templatePDF.abrirDocumento();

        templatePDF.addTitles("ORDEN DE TRABAJO","N°000001");

        templatePDF.addParagraph(nEmpresa);
        templatePDF.addParagraph(dEmpresa);
        templatePDF.addParagraph(fecha);
        templatePDF.addParagraph(rbd);
        templatePDF.createTable(header,getClients());
        templatePDF.closeDocument();




        if (TemplatePDF.archivoPDF.exists()) {
            Uri uri = Uri.fromFile(TemplatePDF.archivoPDF);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "application/pdf");
            try {
                startActivity(intent);




            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.adobe.reader")));
                Toast.makeText(getApplicationContext(), "No cuentas con una aplicacion para visualizar pdf", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "No se encontro el archivo", Toast.LENGTH_LONG).show();
        }
    }


    public void sendEmail(View view) {
        String[] correo = {""};
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        Uri uri =Uri.fromFile((new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"/PDF/OrdenDeTrabajo.pdf")));
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, correo);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Orden de Trabajo");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "");
        emailIntent.putExtra(Intent.EXTRA_STREAM,uri);

        try {
            startActivity(Intent.createChooser(emailIntent, "Enviar email..."));
            finish();
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this,
                    "No tienes clientes de email instalados.", Toast.LENGTH_SHORT).show();
        }
    }


    private ArrayList<String[]>getClients(){
        ArrayList<String[]>rows=new ArrayList<>();

        rows.add(new String[]{"Perimetro Ext.","Exterior","Serv. Hig. Adm."});
        rows.add(new String[]{"Perimetro Int.","Interior","Serv. Hig. Camarines."});
        rows.add(new String[]{"Bodegas","Bodegas","Camara de Frio"});
        return rows;


    }
}
