import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Created by singsand on 10/9/2016.
 */
public class pdfToTextTest {
    @Test
    public void method() throws Exception {
        try {
            FileUtils.cleanDirectory(new File("downloadedPDF/"));
            FileUtils.cleanDirectory(new File("downloadedText/"));
            //Deleting already existing downloaded pdf and text files
        }
        catch(Exception e)
        {

        }

        System.out.println("Testing the PDFToText class:");
        System.out.println("-------------------------------------------------------------");



        System.out.println("\nProviding input search term as 'proton' for PDF download and number of PDF's to download as 3.");

        //Calling method to download pdf
        downloadPDF obj= new downloadPDF();
        obj.method("proton","3");




        System.out.println("\nNow running PDFToText class");



        //Calling method to convert pdf to text files
        pdfToText obj1=new pdfToText();
        obj1.pdfconverter();


        //Getting file list for pdf and text files from their folders and matching later
        File folder = new File("downloadedPDF/");
        File[] listOfFiles = folder.listFiles();
        String[] fileNames=new String[listOfFiles.length];
        int i=0;
        for (File file : listOfFiles)
        {fileNames[i]=file.getName().split("\\.pdf")[0];
            i++;
        }



        System.out.println("\nChecking if converted text files are present in correct directory, and number and names of pdf downloaded matches the converted text files.");

        try{

            File folder1 = new File("downloadedText/");
            File[] listOfFiles1 = folder1.listFiles();
            String[] fileNames1=new String[listOfFiles1.length];


            i=0;
            for (File file : listOfFiles1)
            {fileNames1[i]=file.getName().split("\\.txt")[0];
                i++;
            }

            //matching if number of files and names are correct
            Assert.assertEquals(fileNames1.length,fileNames.length);
            for (int j=0;j<i;j++)
                Assert.assertEquals(fileNames1[j],fileNames[j]);

        }
        catch(Exception e)
        {
            System.out.println("Test case not passed. Find below stack trace:\n"+e);
        }

        System.out.println("\nTest case passed. File names and number match.");
    }

}