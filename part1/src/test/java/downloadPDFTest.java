import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Created by singsand on 10/9/2016.
 */
public class downloadPDFTest {
    @Test
    public void method() throws Exception {

        try {
            FileUtils.cleanDirectory(new File("downloadedPDF/"));
            //Deleting already existing downloaded pdf files
        }
        catch(Exception e)
        {

        }

        System.out.println("Testing the downloadPDFTest class:");
        System.out.println("-------------------------------------------------------------");

        System.out.println("\nNow running downloadPDFTest class");

        //Calling method to download pdf
        downloadPDF obj= new downloadPDF();
        obj.method("atom","2");


        System.out.println("\nChecking if downloaded pdf files are present in correct directory and number of pdf downloaded matches the specified input.");

        //checking if number of files  are correct
        try{
            Assert.assertEquals(new File("downloadedPDF/").list().length,2);

        }
        catch(Exception e)
        {
            System.out.println("Test case not passed. Find below stack trace:\n"+e);
        }

        System.out.println("\nTest case passed. File number and directory is correct.");
    }




}

