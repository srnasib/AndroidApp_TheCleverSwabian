package userlayer.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.thecleverswabian.thecleverswabian.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import userlayer.fragments.FragmentHome;


public class ActivitySendPdf extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_pdf);
    }

    public void createPdf(View view)
    {
        // create a new document
        PdfDocument document = new PdfDocument();
        // crate a page description
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 841, 1).create();
        // start a page
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        //canvas.drawCircle(50, 50, 30, paint);
        paint.setColor(Color.BLACK);
        canvas.drawText("Statement of the Month", 200, 50, paint);
        canvas.drawText("___________________________", 200, 50, paint);
        canvas.drawText("Total Expense of This Month" + " = " + FragmentHome.sumOfExpenses, 80, 70, paint);
        canvas.drawText("Total Income of This month = " + FragmentHome.sumOfIncome, 80, 90, paint);
        //canvas.drawt
        // finish the page
        document.finishPage(page);


        String directory_path = this.getExternalCacheDir().getPath() + "/mypdf/";
        File file = new File(directory_path);
        if (!file.exists())
        {
            file.mkdirs();
        }
        String targetPdf = directory_path + "test.pdf";
        File filePath = new File(targetPdf);
        try
        {
            document.writeTo(new FileOutputStream(filePath));
            Toast.makeText(this, "Done", Toast.LENGTH_LONG).show();
        }
        catch (IOException e)
        {
            Log.e("main", "error " + e.toString());
            Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
        }
        // close the document
        document.close();

        String emailTo = "example@example.com";
        //lets send the email

        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND_MULTIPLE);
        emailIntent.setType("text/plain");
        //emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{emailTo});
        ArrayList<Uri> uris = new ArrayList<Uri>();


        File file1 = new File(getExternalCacheDir() + "test.pdf");

        Log.d("file", file1.getAbsolutePath());

        Uri URI = Uri.fromFile(filePath);
        emailIntent.putExtra(Intent.EXTRA_STREAM, URI);

        // emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        startActivity(Intent.createChooser(emailIntent, "Share The Pdf..."));
    }


    public void createandDisplayPdf(View text)
    {

        Document doc = new Document();

        try
        {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/PDF";

            File dir = new File(path);
            if (!dir.exists())
            {
                dir.mkdirs();
            }

            File file = new File(dir, "mypdffile.pdf");
            FileOutputStream fOut = new FileOutputStream(file);

            PdfWriter.getInstance(doc, fOut);

            //open the document
            doc.open();

            Paragraph p1 = new Paragraph("Amar nam tuntuni");
            Font paraFont = new Font(Font.FontFamily.COURIER);
            p1.setAlignment(Paragraph.ALIGN_CENTER);
            p1.setFont(paraFont);

            //add paragraph to document
            doc.add(p1);

        }
        catch (DocumentException de)
        {
            Log.e("PDFCreator", "DocumentException:" + de);
            Toast.makeText(this, "Not done", Toast.LENGTH_LONG).show();
        }
        catch (IOException e)
        {
            Log.e("PDFCreator", "ioException:" + e);
            Toast.makeText(this, "Not done", Toast.LENGTH_LONG).show();
        } finally
        {
            doc.close();
        }

        viewPdf("mypdffile.pdf", "PDF");
    }

    // Method for opening a pdf file
    private void viewPdf(String file, String directory)
    {

        File pdfFile = new File(Environment.getExternalStorageDirectory() + "/" + directory + "/" + file);
        Uri path = Uri.fromFile(pdfFile);

        // Setting the intent for pdf reader
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(path, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        try
        {
            startActivity(pdfIntent);
        }
        catch (ActivityNotFoundException e)
        {
            Toast.makeText(this, "Can't read pdf file", Toast.LENGTH_SHORT).show();
        }
    }

}