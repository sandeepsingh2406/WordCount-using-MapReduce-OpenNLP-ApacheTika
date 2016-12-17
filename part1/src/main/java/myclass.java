import org.apache.commons.io.FileUtils;

import java.io.File;

/**
 * Created by Sandeep Singh on 10/8/2016.
 */
public class myclass {
    public static void main(String arg[]) throws Exception {

        try {
            FileUtils.cleanDirectory(new File("downloadedPDF/"));
            FileUtils.cleanDirectory(new File("downloadedText/"));
            //Cleaning the pdf and text file directories
        }
        catch(Exception e)
        {}


        downloadPDF obj= new downloadPDF();
        if(arg.length != 0)
        {
            System.out.println("Downloading PDFs for search keyword(default 5 PDFs downloaded if number not provided): '"+arg[0]+"'");
            if(arg.length<2)
                obj.method(arg[0],"5");
            else
                obj.method(arg[0],arg[1]);
            //Object created for downloadPDF class and method is called with search term as provided by user
        }
        else
        {
            System.out.println("Search keyword not provided. Downloading PDF for search keyword (default 5 PDFs downloaded if number     not provided) : 'proton'");

            if(arg.length<2)
                obj.method("proton","5");
            else
                obj.method("proton",arg[1]);
            //If user does not provide search term, default search term is "proton"
        }

        System.out.println("\nCalling PDF tp text converter class");

        //Calling method of pdfToText class to convert download pdf files to text files
        pdfToText obj1=new pdfToText();
        obj1.pdfconverter();




    }
}
