# Lab 1

## Student information
* Full name: Siraaj Kudtarkar
* E-mail: skudt001@ucr.edu
* UCR NetID: skudt001
* Student ID: 862129207

## Answers

* (Q1) What is the name of the created directory?
* skudt001_lab1

* (Q2) What do you see at the console output?
* Hello World!

* (Q3) What do you see at the output?
* log4j:WARN No appenders could be found for logger (org.apache.hadoop.metrics2.lib.MutableMetricsFactory).
*  log4j:WARN Please initialize the log4j system properly.
*  log4j:WARN See http://logging.apache.org/log4j/1.2/faq.html#noconfig for more info.
*  Exception in thread "main" java.lang.ArrayIndexOutOfBoundsException: 0
  at edu.ucr.cs.cs167.skudt001.App.main(App.java:61)
* (Q4) What is the output that you see at the console?
* but	1
*  cannot	3
*  crawl	1
*  do	1
*  fly,	1
*  forward	1
*  have	1
*  if	3
*  keep	1
*  moving	1
*  run	1
*  run,	1
*  then	3
*  to	1
*  walk	1
*  walk,	1
*  whatever	1
*  you	5
* log4j:WARN No appenders could be found for logger (org.apache.hadoop.metrics2.lib.MutableMetricsFactory).
*   log4j:WARN Please initialize the log4j system properly.
*   log4j:WARN See http://logging.apache.org/log4j/1.2/faq.html#noconfig for more info.
* (Q5) Does it run? Why or why not?
* No this first method does not run because of a ClassNotFoundException. The second method also didn't work because the output file was already created and threw a FileAlreadyExistsException.
* Exception in thread "main" java.lang.NoClassDefFoundError: org/apache/hadoop/conf/Configuration
*  at edu.ucr.cs.cs167.skudt001.App.main(App.java:53)
*  Caused by: java.lang.ClassNotFoundException: org.apache.hadoop.conf.Configuration
*  at java.net.URLClassLoader.findClass(URLClassLoader.java:387)
*  at java.lang.ClassLoader.loadClass(ClassLoader.java:418)
*  at sun.misc.Launcher$AppClassLoader.loadClass(Launcher.java:355)
*  at java.lang.ClassLoader.loadClass(ClassLoader.java:351)
*  ... 1 more
* THE SECOND METHOD IS BELOW
* 2023-01-12 18:06:58,015 WARN util.NativeCodeLoader: Unable to load native-hadoop library for your platform... using builtin-java classes where applicable
  2023-01-12 18:06:58,316 INFO impl.MetricsConfig: Loaded properties from hadoop-metrics2.properties
  2023-01-12 18:06:58,387 INFO impl.MetricsSystemImpl: Scheduled Metric snapshot period at 10 second(s).
  2023-01-12 18:06:58,387 INFO impl.MetricsSystemImpl: JobTracker metrics system started
  Exception in thread "main" org.apache.hadoop.mapred.FileAlreadyExistsException: Output directory file:/Users/siraaj/cs167/workspace/skudt001_lab1/output.txt already exists
  at org.apache.hadoop.mapreduce.lib.output.FileOutputFormat.checkOutputSpecs(FileOutputFormat.java:164)
  at org.apache.hadoop.mapreduce.JobSubmitter.checkSpecs(JobSubmitter.java:277)
  at org.apache.hadoop.mapreduce.JobSubmitter.submitJobInternal(JobSubmitter.java:143)
  at org.apache.hadoop.mapreduce.Job$11.run(Job.java:1565)
  at org.apache.hadoop.mapreduce.Job$11.run(Job.java:1562)
  at java.security.AccessController.doPrivileged(Native Method)
  at javax.security.auth.Subject.doAs(Subject.java:422)
  at org.apache.hadoop.security.UserGroupInformation.doAs(UserGroupInformation.java:1762)
  at org.apache.hadoop.mapreduce.Job.submit(Job.java:1562)
  at org.apache.hadoop.mapreduce.Job.waitForCompletion(Job.java:1583)
  at edu.ucr.cs.cs167.skudt001.App.main(App.java:63)
  at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
  at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
  at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
  at java.lang.reflect.Method.invoke(Method.java:498)
  at org.apache.hadoop.util.RunJar.run(RunJar.java:323)
  at org.apache.hadoop.util.RunJar.main(RunJar.java:236)
