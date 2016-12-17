import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by singsand on 10/8/2016.
 */
public class nameFinder_MapReduce {


    public static class Map extends Mapper<LongWritable, Text, Text, Text> {

        //Some declarations
        private final static IntWritable one = new IntWritable(1);
        private Text word = new Text();
        InputStream tokenizerModel = null;
        InputStream NameFinderModel = null;




        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

            //the input stream is created using the en-token.bin (tokenizer) file provided by opennlp
            tokenizerModel = nameFinder_MapReduce.class.getResourceAsStream("/en-token.bin");

            //the model is created using the input stream that will be used to create a tokenizer
            TokenizerModel modelToken = new TokenizerModel(tokenizerModel);
            Tokenizer tokenizer = new TokenizerME(modelToken);

            String line = value.toString();

            //input line is split into tokens
            String tokens[] = tokenizer.tokenize(line);

            //the name of the input file is obtained
            FileSplit fileSplit = (FileSplit)context.getInputSplit();
            String filename = fileSplit.getPath().getName();

            //the input stream is created using the en-ner-person.bin (namefinder) file provided by opennlp
            NameFinderModel = nameFinder_MapReduce.class.getResourceAsStream("/en-ner-person.bin");

            //the model is built using the file and a namefinder object is created
            TokenNameFinderModel model = new TokenNameFinderModel(NameFinderModel);
            NameFinderME nameFinder = new NameFinderME(model);

            //the namefinder parses the token the gets the names, and store their indices in the below file
            Span nameSpans[] = nameFinder.find(tokens);



            for( int i = 0; i<nameSpans.length; i++) {

                //all the name obtained from namefinder are written as keys with values being 1, filename
                //filename is the name of the input text file cotaining the person's name
                String name="";
                for(int j=nameSpans[i].getStart();j<nameSpans[i].getEnd();j++)
                {
                    //the entire name is obtained
                    name+=tokens[j] + " ";
                }
                word.set(name.trim());
                context.write(word, new Text((one+","+ filename)));
                //key,value pairs are written
            }


        }
    }

    public static class Reduce extends Reducer<Text, Text, Text, Text> {

        public void reduce(Text key, Iterable<Text> values, Context context)
                throws IOException, InterruptedException {
            int sum = 0;
            String combined_string="";
            StringBuilder stringBuilder = new StringBuilder();
            for (Text value : values) {

                //the 1's in the values for each key are added up
                sum += Integer.parseInt(value.toString().split(",",2)[0]);

                if(!stringBuilder.toString().contains(value.toString().split(",", 2)[1])) {
                    stringBuilder.append(value.toString().split(",", 2)[1]);
                    stringBuilder.append(", ");

                    //the filenames in the values for each key are added up
                }

            }

            StringBuilder stringBuilder1 = new StringBuilder();
            String temp="Total count:"+sum+", References: ";
            stringBuilder1.append(temp);
            stringBuilder1.append(stringBuilder.substring(0,stringBuilder.length()-2));

            //Final values will appear in the form: Total count: 10, References: file1.txt, file2.txt
            context.write(new Text("Name: " +key), new Text(stringBuilder1.toString()));
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();

        Job job = new Job(conf, "namefinder");
        job.setJarByClass(nameFinder_MapReduce.class);

        //output key and value classes are specified
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        job.setMapperClass(Map.class);
        job.setReducerClass(Reduce.class);

        //input and output formats are specified
        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);


// the user provides the input hdfs and output hdfs paths when running the jar
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        job.waitForCompletion(true);
    }
}
