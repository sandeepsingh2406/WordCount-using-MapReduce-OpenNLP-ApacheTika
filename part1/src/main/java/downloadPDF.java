/**
 * Created by Sandeep Singh on 10/6/2016.
 */

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;


public class downloadPDF {

    void method(String searchquery, String numPDF ) throws Exception {

        System.out.println("\n\nDownloaded pdfs are stored in downloadedPDF folder.\n\n");
        String USER_AGENT = "Mozilla/5.0";
        String url = "http://export.arxiv.org/api/query?search_query="+searchquery+"&start=0&max_results="+numPDF;
        //Using the above url, we can obtain links to pdf files containing the specified keyword

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();

        //Reader to get the xml response after submitting the HTTP request as done above
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        //This StringBuffer object will contain the entire xml response

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);

        }
        in.close();




        int i=0;
        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(response.toString()));
        //Storing the xml response in a InputSource Object

        Document doc = db.parse(is);
        doc.getDocumentElement().normalize();
        //Normalizing the xml reponse

        NodeList nList = doc.getElementsByTagName("link");
        //Get all nodes with tagname "link" as one of them will contain the link to the pdf


        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);

            Element eElement = (Element) nNode;

            //Matching the node(element) which contains the pdf link
            if(eElement.getAttribute("title").equals("pdf")) {

                NodeList eList = doc.getElementsByTagName("entry");
                Node eNode = eList.item(i);
                Element element = (Element) eNode;
                String papername=element
                        .getElementsByTagName("title")
                        .item(0)
                        .getTextContent();
                //Name of each pdf is also obtained from the xml response

                i++;

                //Name of the paper is simplified
                papername=papername.replaceAll("[^A-Za-z(\\s)]+", "").replace(" ","_");

                //Downloading the pdfs below
                URL pdf_url = new URL(eElement.getAttribute("href").replace("http:","https:"));
                HttpURLConnection pdf_con = (HttpURLConnection) pdf_url.openConnection();

                // optional default is GET
                pdf_con.setRequestMethod("GET");

                //add request header
                pdf_con.setRequestProperty("User-Agent", USER_AGENT);




                byte[] ba1 = new byte[1024];
                InputStream pdf_in=pdf_con.getInputStream();

                //New directory "downloadedPDF"  is created where all pdf files are stored

                System.out.println("Downloading file: "+papername+".pdf");
                File file = new File("downloadedPDF/"+papername+".pdf");
                file.getParentFile().mkdirs();
                FileOutputStream fos1 = new FileOutputStream(file);

                BufferedReader br= new BufferedReader(new InputStreamReader(pdf_in));


                //File is written below
                int baLength;
                while (( baLength = pdf_in.read(ba1)) != -1) {
                    fos1.write(ba1, 0, baLength);

                }
                fos1.flush();
                fos1.close();
                pdf_in.close();

            }





        }


    }
}


