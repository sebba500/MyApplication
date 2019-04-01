package com.example.myapplication;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class TemplatePDF {

    private Context context;
    public static File archivoPDF;
    private Document documento;
    private PdfWriter pdfWriter;
    private Paragraph paragraph;
    private Font fTitle = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD);
    private Font fSubTitle = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    private Font fText = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
    private Font fHighText = new Font(Font.FontFamily.TIMES_ROMAN, 15, Font.BOLD, BaseColor.RED);


    public TemplatePDF(Context context) {

        this.context = context;
    }

    //METODO PARA ABRIR EL DOCUMENTO
    public void abrirDocumento() {

        crearPDF();
        try {

            documento = new Document(PageSize.A4);
            pdfWriter = PdfWriter.getInstance(documento, new FileOutputStream(archivoPDF));
            documento.open();

        } catch (Exception e) {
            Log.e("openDocument", e.toString());
        }
    }

    //METODO PARA CREAR EL DOCUMENTO
    private void crearPDF() {

        File folder = new File(Environment.getExternalStorageDirectory().toString(), "PDF");

        if (!folder.exists())
            folder.mkdir();
        archivoPDF = new File(folder, "OrdenDeTrabajo"+".pdf");
    }

    public void closeDocument() {
        documento.close();
    }


    public void addTitles(String title, String subTitle) {

        try {


            paragraph = new Paragraph();
            addChildP(new Paragraph(title, fTitle));
            addChildP(new Paragraph(subTitle, fSubTitle));
            paragraph.setSpacingAfter(30);
            documento.add(paragraph);
        } catch (Exception e) {
            Log.e("addTitles", e.toString());
        }
    }



    private void addChildP(Paragraph childParagraph) {
        childParagraph.setAlignment(Element.ALIGN_RIGHT);
        paragraph.add(childParagraph);
    }

    public void addParagraph(String text) {
        try {
            paragraph = new Paragraph(text, fText);
            paragraph.setSpacingAfter(5);
            paragraph.setSpacingBefore(5);
            documento.add(paragraph);
        } catch (Exception e) {
            Log.e("addParagraph", e.toString());
        }
    }

    public void createTable(String[] header, ArrayList<String[]> clients) {
        paragraph = new Paragraph();
        paragraph.setFont(fText);
        PdfPTable pdfPTable = new PdfPTable(header.length);
        pdfPTable.setWidthPercentage(100);
        pdfPTable.setSpacingBefore(20);
        PdfPCell pdfPCell;
        int indexC = 0;
        while (indexC < header.length) {
            pdfPCell = new PdfPCell(new Phrase(header[indexC++], fText));
            pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            pdfPCell.setBackgroundColor(BaseColor.GREEN);
            pdfPTable.addCell(pdfPCell);
        }

        for (int indexR = 0; indexR < clients.size(); indexR++) {
            String[] row = clients.get(indexR);
            for (indexC = 0; indexC < clients.size(); indexC++) {
                pdfPCell = new PdfPCell(new Phrase(row[indexC]));
                pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfPCell.setFixedHeight(40);

                pdfPTable.addCell(pdfPCell);

            }
        }

        paragraph.add(pdfPTable);

        try {

            documento.add(paragraph);
        } catch (Exception e) {
            Log.e("createTable", e.toString());
        }

    }





}
