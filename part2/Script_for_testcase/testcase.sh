#This script is used to create a test for the name finder mapreduce program

#First we create a pair of small input files.
echo "Starting test for name finder mapreduce program"
echo "  Press any key to continue."
read input_variable

printf " Creating a directory to create 2 text files that will e provided as input for name finder\n\n"
mkdir testcase_input
rm -rf testcase_input/*
printf "MapReduce: Simplified Data Processing on Large Clusters. \n Jeffrey Dean. " > testcase_input/testcase_inputfile1.txt

printf "This is a paper on Hadoop \n \nThe Hadoop Distributed File System (HDFS). \n Jeffrey Dean. Paper on hadoop" > testcase_input/testcase_inputfile2.txt

echo " Copying files to hdfs"

#Now we create a directory in hdfs for our project.

hadoop dfs -mkdir /user/root/project
hadoop dfs -mkdir /user/root/project/input/
hadoop dfs -rmr /user/root/project/input/* #Clear the input directory if exists
hadoop dfs -put testcase_input/* /user/root/project/input/
#Put all text files into hdfs

hadoop dfs -rmr /user/root/project/output

printf " Remove signed dependency jars in our bundled jar, just in case"
zip -d nameExtractor.jar META-INF/*.RSA META-INF/*.DSA META-INF/*.SF
#Remove signed dependency jars just in case

echo "Files copied to HDFS. Press any key to start Map reduce program for name finder"
read input_variable

hadoop jar nameExtractor.jar /user/root/project/input/ /user/root/project/output/
#Run the map reduce program. Input and output hdfs directories are specified.

echo "Mapreduce completed.  Press any key to check output file created."
read input_variable
echo "Checking...."
rm -rf part*
rm -rf check*
hadoop fs -get /user/root/project/output/part*
cp -r part* check.txt


s3=`cat check.txt`
s2=`cat testcase_outputcheck.txt`

if [ "$s3" == "$s2" ]
then
  printf "\n Output file in hdfs matches testcase_outputcheck.txt. Test case passed\n"
else
 printf "\n test case not passed\n"
fi

printf "\n You can see below."
printf "\nhadoop fs -get /user/root/project/output/part*\n"
hadoop fs -cat /user/root/project/output/part*
printf "\ncat testcase_outputcheck.txt\n"
cat testcase_outputcheck.txt
