#This script is used to run the namefinder mapreduce program, provide its input files and read its output files from hdfs

#Firstly, the DownloadPapers.jar is run, using which PDF and Text files are Papers are downloaded. So, This script needs to be in the same directory as DownloadPapers.jar

#The below command is used to remove the signature of the signed dependency jars that might be contained in the fat jar DownloadPapers.jar. Otherwise, running the below jar causes a signature error.
 
zip -d DownloadPapers.jar META-INF/*.RSA META-INF/*.DSA META-INF/*.SF

#Now we run the above jar to download papers to downloadedPDF and downloadedText directories 
mkdir downloadedText
mkdir downloadedPDF
rm -rf downloadedText/*
rm -rf downloadedPDF/*
java -jar DownloadPapers.jar $1 $2

#where electron is the search term and 2 is the number of papers we want.

#Since the text files are very large,r we can create shorter version of these text files to test the mapreduce program in shorter time. 
#Below command can be used(to keep only first 50 lines of the  file): 
mkdir input
rm -rf input/*
cp downloadedText/* input   #Put files in another directory
sed -i '50,$ d' input/*

#Now we create a directory in hdfs for our project.

hadoop dfs -mkdir /user/root/project
hadoop dfs -mkdir /user/root/project/input/
hadoop dfs -rmr /user/root/project/input/* #Clear the input directory if exists
hadoop dfs -put input/* /user/root/project/input/
#Put all shortened text files into hdfs
hadoop dfs -rmr /user/root/project/output

zip -d nameExtractor.jar META-INF/*.RSA META-INF/*.DSA META-INF/*.SF
#Remove signed dependency jars just in case

hadoop jar nameExtractor.jar /user/root/project/input/ /user/root/project/output/
#Run the map reduce program. Input and output hdfs directories are specified.

echo "Please press a key to see the output file of the mapreduce job: "
read input_variable


hadoop fs -cat /user/root/project/output/part*
#Display output files produced in hdfs in the specidied directory

