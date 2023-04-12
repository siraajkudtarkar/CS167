# Lab 2

## Student information

* Full name: Siraaj Kudtarkar
* E-mail: skudt001@ucr.edu
* UCR NetID: skudt001
* Student ID: 862129207

## Answers

* (Q1) Verify the file size and report the running time.

        Copied 2271210910 bytes from 'AREAWATER.csv' to 'output.txt' in 9.448705 seconds

* (Q2) Report the running time of the copy command.

        0.01s user 1.21s system 59% cpu 2.051 total

* (Q3) How do the two numbers compare? (The running times of copying the file through your program and the operating system.) Explain IN YOUR OWN WORDS why you see these results.

* The first number is significantly slower because the second number uses the operating system's commands. The difference in efficiency is because of the use of multiple API's in our App.java file compared to functions that already exist within the operating system.

* (Q4) Does the program run after you change the default file system to HDFS? What is the error message, if any, that you get?

        WARN util.NativeCodeLoader: Unable to load native-hadoop library for your platform... using builtin-java classes where applicable
        Copied 1258 bytes from 'README.md' to 'output.txt' in 0.140212 seconds
* The program runs successfully to copy the demo file to the output file.

* (Q5) Use your program to test the following cases and record the running time for each case.
  Colons can be used to align columns.

| From  |  To   | Running Time (s) |
|-------|:-----:|-----------------:|
 | Local | HDFS  |  5.949727 |
| HDFS  | Local |  9.132355 |
| HDFS  | HDFS  |  5.963511 |

        Copied 2271210910 bytes from 'AREAWATER.csv' to 'AREAWATER_copy.csv' in 5.949727 seconds
        Copied 2271210910 bytes from 'hdfs:/user/siraaj/AREAWATER.csv' to 'file:/Users/siraaj/cs167/workspace/skudt001_lab2/AREAWATER_copy.csv' in 9.132355 seconds
        Copied 2271210910 bytes from 'hdfs:/user/siraaj/AREAWATER.csv' to 'hdfs:/user/siraaj/AREAWATER_copy1.csv' in 5.963511 seconds

* (Q6) Test your program on two files, one file stored on the local file system, and another file stored on HDFS. Compare the running times of both tasks. What do you observe?