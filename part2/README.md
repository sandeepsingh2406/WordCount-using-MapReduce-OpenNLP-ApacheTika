# README #

(NOTE: There are 2 repositories for HW2. One is this and other is hw2_part1, As I was only able to put one project in one repo )

This repo contains the project for second part of the HW2 i.e. **Name Extraction**.

In this Java project, I implement the Hadoop MapReduce framework along with OpenNLP to extract names from various research papers.

--------------------------------------------------------------------------------------------------------------
**The project contains only the class nameFinder_MapReduce. 
**

This class implements MapReduce to  process the text files. 

It produces output of the following form: Name, Total Count: 10, References: filename1, filename2

* The Mapper takes as input the text file which we will be parsing. It gets the file name and file text, runs the opennlp namefinder tool to get the names from the file text. The mapper outputs the following key, value pairs: <Name>, <1, filename>
The 2 files from opennlp contains the tokenizer and namefinder, are places in the src/main/resources/ folder. They are alrady included in the runnable JAR for the project.

* The Reducer gets all the values from one key (Name) and combines them to produce the following output: <Name>, <TotalCount, filenames_comma_separated>

---------------------------------------------------------------------------------------------------------------
**Creating and running the jar file
**

The runnable jar exists in out/artifacts/nameExtractor.jar

This jar can be run using the following linux command:

      java -jar nameExtractor.jar input_files_hdfs_path output_files_hdfs_path 

The input_files_hdfs_path should already contain the text files that map reduce will take as input.

----------------------------------------------------------------------------------------------------------------
**Script to run the entire project:
**
 out/artifacts/Script_to_run_jar/ contains a script run_namefinder.sh

* This script can run the whole project from downloading pdf files, converting them to text files, transferring the files to hdfs for input to mapreduce, executing the mapreduce program, reading the final output file from HDFS.

          The script can be run as: sh  run_namefinder.sh <SearchKeyword> <Count>
          If the searchkeyword and count are not provided, default values already defined the paperDownloader program are considered.

* The script first calls the DownloadPapers.jar, providing it the searchterm and count entered by user. The jar downloads the pdf files and converts them into text files and outputs to a directory. For more info on this jar, see hw2_part1 repo.

* Next, it copies these text files to a hdfs directory. It then runs nameExtractor.jar which has the mapreduce program. It provides the above input hdfs directory and output hdfs directory. Finally, after mapreduce is done, it reads and displays the mapreduce output file created in hdfs.

---------------------------------------------------------------------------------------------------------------
**Testing for the nameExtractor program**

 out/artifacts/Script_for_testcase contains a test script testcase.sh. 
 This script does an integration test for the entire project. 

       It can be run as : sh testcase.sh

* **Note:** There is another file in out/artifacts/Script_for_testcase/ which is testcase_output.txt. This text file contains the expected output produced by running this test case, and is used by the testcase script to check the outpu produced by mapreduce in hdfs. So, this testcase_output.txt should be kept with testcase.sh before runnning the script

* testcase.sh first creates 2 input text files in the local directory. These files are then copies into a input path in hdfs. 
  It then runs nameExtractor.jar which starts the mapreduce program. After the mapreduce program is over, the script reads the file produced in hdfs output directory, and compares it to the expected output in testcase_output.txt.
If the output matches, test case is passed.

------------------------------------------------------------------------------------------------------------

* **Note:** Both the above scripts, attempt to create and clear input and output directories in hdfs, as well as locally create and clear directories for pdf and text files. In case these directories exist already, the script might produce errors like:

       mkdir: cannot create directory `input': File exists

       mkdir: `/user/root/project/input': File exists

These errors do not affect the functionality of the scripts.

* **Note**: For some search keywords, some papers are downloaded which have issues being processed by Apache Tika. So they might throw the exception:

      org.apache.tika.exception.TikaException: Unable to extract all PDF content

* **Note:** These scripts also run the following command: 

      zip -d SomeJarName.jar META-INF/*.RSA META-INF/*.DSA META-INF/*.SF

This is because some of the dependency JARs in the fat JAR we created for the project are signed JARs(like Apache Tika jar), so when we combine them all in one JAR and run that JAR then signature of the signed JAR doesn't match up and hence we get the security exception about signature mis-match. So by running the above command, it removes the signature of the signed jars.


----------------------------------------------------------------------------------------------------------

References: 
https://opennlp.apache.org/
 https://github.com/hortonworks/hadoop-tutorials/blob/master/Community/T09_Write_And_Run_Your_Own_MapReduce_Java_Program_Poll_Result_Analysis.md 
http://hortonworks.com/hadoop-tutorial/introducing-apache-hadoop-developers/
http://blog.puneethabm.in/inverted-index-mapreduce-program/
 http://stackoverflow.com/