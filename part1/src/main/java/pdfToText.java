import org.apache.tika.Tika;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.sax.BodyContentHandler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;


@SuppressWarnings("unchecked")


public class pdfToText {
    public static void pdfconverter() throws Exception {

        //handler to store the pdf text
        BodyContentHandler handler = new BodyContentHandler(Integer.MAX_VALUE);
        Metadata metadata = new Metadata();
        Tika tika = new Tika();

        //metadata for pdf file

        System.out.println("\n\nConverted text files are stored in downloadedText folder.\n\n");

        //Read pdf files from below directory
        File folder = new File("downloadedPDF/");
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles) {

            if (file.isFile()) {

//                FileInputStream inputstream;


                try {


//                    inputstream = new FileInputStream(file);


                    //Obtaining the filename to provide the text filename
                    String filename = file.getName().split("\\.pdf")[0];



                    File ofile = new File("downloadedText/" + filename + ".txt");
                    ofile.getParentFile().mkdirs();

                    System.out.println("Converted text file name: "+filename+".txt");
                    try (PrintStream out = new PrintStream(new FileOutputStream("downloadedText/" + filename + ".txt"))) {


                        PrintStream oldErr = System.err;
                        PrintStream newErr = new PrintStream(new ByteArrayOutputStream());
                        System.setErr(newErr);
                        //Above code is used to avoid printing warning that occur while parsing the pdf file due to Font issues

                        out.print(tika.parseToString(file));
                        //Writing the text file obtained by parsing through Tika


                        System.setErr(oldErr);



                    }



                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }
    }
}



