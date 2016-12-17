# README #
* (NOTE: There are 2 repositories for HW2. One is this and other is hw2_part2, As I was only able to put one project in one repo)

**This repo contains the project for first part of the HW2 i.e. Downloading Papers.
**

In this Java project, I am downloading pdf files using the arXiv API (https://arxiv.org/help/api/index). The user is asked to specify the search term(for searching papers) and also the number of papers to download. If not specified, a default search query and count has been considered.

* myclass is the main class. It calls a method of downloadPDF class and pdfToText class.
* downloadPDF class takes the search term and number from myclass and downloads the pdfs to the "downloadPDF" folder.
* pdfToText class reads all the pdf files from "downloadPDF" directory and converts these files to text files and writes them to "downloadText" directory". each text file has the same name as the pdf it was converted from.

* myclass take 2 command line arguments, search term and number. 

         It can be run as: java myclass electron 2 

          where electron is the search term and 2 is the number of pdf to be downloaded.


---------------------------------------------------
Test Cases:
In this project, there are 2 test cases created using JUnit.

1. downloadPDFTest: this test case checks the downloadPDF class. It gives a input search query and count to the downloadPDF class and then later checks if the downloaded pdf files are in the correct directory and the correct number of files are downloaded.

2. pdfToTextTest: this test case checks the pdfToText class. It first calls the downloadPDF class providing a search term and count, and then calls the pdfToText class. The test case matches the output of the 2 classes, downloadPDF  and pdfToText. It checks if the files are created in the correct directories and if the file count and file names are the same for the pdf and text files.

------------------------------------------------------
Creating and running the jar file

The runnable jar exists in out/artifacts/DownloadPapers_jar/

* IMPORTANT NOTE: If a new fat jar(containing all the external dependency jars) is created for this project, it is important to run the below command in linux before running the jar. 

        zip -d DownloadPapers.jar META-INF/*.RSA META-INF/*.DSA META-INF/*.SF

This is because some of the dependency JARs are signed JAR(Apache Tika jar), so when we combine them all in one JAR and run that JAR then signature of the signed JAR doesn't match up and hence we get the security exception about signature mis-match.
So by running the above command, it removes the signature of the signed jars.

The other option is to not build a fat jar and keep the dependency jars separate from the project jar.

* After running the above command and removing signatures, the jar can be run using the following linux command, 

        java -jar DownloadPapers.jar electron 2
            where electron is the search term and 2 is the number of pdf to be downloaded.

This will create the "downloadPDF" directory(clean the directory if it already exists) containing all pdfs and  "downloadText" directory containing the text files.


-----------------------------------------------------------------------
* Note: For some search keywords, some papers are downloaded which have issues being processed by Apache Tika. So they might throw the exception:

         org.apache.tika.exception.TikaException: Unable to extract all PDF content
-----------------------------------------------------------------------
References:
https://arxiv.org/help/api/index
http://www.tutorialspoint.com/tika/
http://stackoverflow.com/
https://gist.github.com/hkhamm/88923412992d284580ea