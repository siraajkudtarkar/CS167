# Lab 5

## Student information

* Full name: Siraaj Kudtarkar
* E-mail: skudt001@ucr.edu
* UCR NetID: skudt001
* Student ID: 862129207

## Answers

* (Q1) Do you think it will use your local cluster? Why or why not?

Yes because there were no changes made to the Spark web interface.

* (Q2) Does the application use the cluster that you started? How did you find out?

Yes because it shows up under the completed application section of the Spark web interface.

* (Q3) What is the Spark master printed on the standard output on IntelliJ IDEA?

Using Spark master 'local[*]'
Number of lines in the log file 30970

* (Q4) What is the Spark master printed on the standard output on the terminal?

Using Spark master 'spark://127.0.0.1:7077'
Number of lines in the log file 30970

* (Q5) For the previous command that prints the number of matching lines, list all the processed input splits.

23/02/09 18:00:45 INFO HadoopRDD: Input split: file:/Users/siraaj/cs167/workspace/skudt001_lab5/nasa_19950801.tsv:0+1138640
23/02/09 18:00:45 INFO HadoopRDD: Input split: file:/Users/siraaj/cs167/workspace/skudt001_lab5/nasa_19950801.tsv:1138640+1138641

* (Q6) For the previous command that counts the lines and prints the output, how many splits were generated?

4

* (Q7) Compare this number to the one you got earlier.

This time there are 2 splits compared to 4 from earlier.

* (Q8) Explain why we get these numbers.

The input file is read twice so there are 2 times more splits.

* (Q9) What can you do to the current code to ensure that the file is read only once?

To ensure the file is read only once, add

        .cache( ) to the end of   JavaRDD<String> logFile = spark.textFile(inputPath)