package br.com.narcielitonlopes.cadastropessoas;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.orm.SugarContext;
import com.orm.dsl.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.narcielitonlopes.cadastropessoas.service.PessoaService;
import br.com.narcielitonlopes.cadastropessoas.util.FormatarValor;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RelatorioGraficoActivity extends AppCompatActivity implements View.OnClickListener{

    @BindView(R.id.chart) PieChart chart;
    @BindView(R.id.button_gerar_pdf) Button gerarPDF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorio_grafico);
        ButterKnife.bind(this);
        SugarContext.init( this );
        gerarPDF.setOnClickListener(this);

        Intent intent = getIntent();
        if(intent != null){
            mostrarGrafico();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],@NonNull int[] grantResults) {
        switch (requestCode) {
            case 1024: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    gerarPdf();
                } else {
                    String texto = "É preciso permitir salvar";
                    Context contexto = getApplicationContext();
                    int duracao = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(contexto, texto, duracao);
                    toast.show();
                }
            }

        }
    }

    @Override
    public void onClick(View v) {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1024);
        } else {
            gerarPdf();
        }
    }

    private void mostrarGrafico() {
        PessoaService service = new PessoaService();
        Long quantidadeFeminino = service.countSexo("F");
        Long quantidadeMasculino = service.countSexo("M");

        List<PieEntry> entries = new ArrayList<>();
            entries.add(new PieEntry(quantidadeFeminino, "Feminino"));
            entries.add(new PieEntry(quantidadeMasculino, "Masculino"));
        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(Color.RED, Color.BLUE);
        //QUANTIDADE
        dataSet.setValueTextSize(12);
        dataSet.setAutomaticallyDisableSliceSpacing(false);
        dataSet.setSliceSpace(3);
        dataSet.setValueFormatter(new FormatarValor());

        //INICIO legenda parte de baixo
        Legend legend = chart.getLegend();
        legend.setTextSize(14);
        legend.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);
        //forma de bola, quadrado....
        legend.setFormSize(13);
        //FIM legenda parte de baixo

        PieData data = new PieData(dataSet);

        chart.setUsePercentValues(false);
        chart.animateY(2000);
        chart.getDescription().setEnabled(false);
        chart.setData(data);
        chart.invalidate(); //refresh
    }

    private void gerarPdf() {
        File path = new File(Environment.getExternalStorageDirectory(), "PDFGerados");
        String filename = "relatorioPdf.pdf";
        File pdfFile = new File(path, filename);
        try {
            path.mkdirs();
            chart.setDrawingCacheEnabled(true);
            chart.buildDrawingCache();
            Bitmap bm = chart.getDrawingCache();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            //passo 1
            Document document = new Document(PageSize.A4);
            //passo 2
            FileOutputStream fileOutputStream = new FileOutputStream(pdfFile);
            PdfWriter.getInstance(document, fileOutputStream);
            //passo 3
            document.open();
            Paragraph texto_paragrafo = new Paragraph("Relatórios de Cadastrados por Sexo", FontFactory.getFont(FontFactory.HELVETICA, 20, Font.BOLD));
            document.add(texto_paragrafo);
            document.addAuthor("Narcieliton Lopes");
            Image img = Image.getInstance(byteArray);
            img.scalePercent(50, 50);
            //passo 4
            document.add(img);
            //passo 5
            document.close();
            abrirPDF(filename);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    private void abrirPDF(String filename){
        File file = new File(Environment.getExternalStorageDirectory().getPath()+"/PDFGerados"+"/"+filename);

        //criar o intent para visualização do documento
        Uri caminho = FileProvider.getUriForFile(RelatorioGraficoActivity.this,
                BuildConfig.APPLICATION_ID + ".provider",
                file);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(caminho, "application/pdf");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);
    }

}
