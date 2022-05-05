# Lab 6

## Objectives

* Getting started with using Scala to access Spark RDD and Spark SQL.
* Use the Dataframe (SparkSQL) API to process semi-structured data.
* Use SQL queries to process CSV files.

## Prerequisites
* Setup the development environment as explained in [Lab 1](../Lab1/CS167-Lab1.md).
* (Similar to Lab 5) Download [Apache Spark 3.2.1](https://spark.apache.org/downloads.html). Choose the package type *Pre-built for Apache Hadoop 3.3 and later*.
* Download these two sample files [sample file 1](../Lab4/nasa_19950801.tsv), [sample file 2](https://drive.google.com/open?id=1pDNwfsx5jrAqaSy8AKEZyfubCE358L2p). Decompress the second file after download. These are the same files we used in [Lab 4](../Lab3/CS167_Lab3.md).
* Depending on how you extract the second file, it could be named either `nasa_19950630.22-19950728.12.tsv` or `19950630.23-19950801.00.tsv`. In this lab, we will use these two names interchangeably.
* For Windows users, install the Ubuntu app from Microsoft Store and set it up or work with the virtual machine.
* (Optional) To add Scala language support to IntelliJ, you can install the [Scala plugin](https://plugins.jetbrains.com/plugin/1347-scala). Please check the [plugin management page](https://www.jetbrains.com/help/idea/managing-plugins.html) to see the details about installing and managing plugins in Intellij.
* If you are not yet familiar with Scala, please check [this tutorial](https://docs.scala-lang.org/tutorials/scala-for-java-programmers.html) to help with the transition from Java to Scala.

## Overview
In this lab, we will be using mainly Scala code. While pure Scala projects are usually set up using [SBT](https://www.scala-sbt.org), we will use Maven for this project to reuse your existing development environment and avoid the complications of setting up a new development tool. According to the [official Scala documentation](https://www.scala-lang.org) 'Scala combines object-oriented and functional programming in one concise, high-level language.' Since big-data systems rely heavily on functional programming, Scala is an excellent match for big-data. This is why Spark is natively written in Scala. If you excel in Scala, you can write more concise and readable code and become more productive.

The lab has two parts. The first part implements some operations using the Scala RDD API. The second part repeats the same operations using SparkSQL. This allows you to contrast and understand the difference between them.

## Lab Work


## Part A. Spark RDD

### I. Project Setup (10 minutes)
We will follow a slightly modified version of the instructions on the [official Scala website](https://docs.scala-lang.org/tutorials/scala-with-maven.html). Mainly, we will avoid the interactive steps and combine all our choices in one command line.
1. To generate a new Maven project that works with Scala, use the following command:
```console
mvn archetype:generate -DarchetypeGroupId=net.alchim31.maven -DarchetypeArtifactId=scala-archetype-simple -DgroupId=edu.ucr.cs.cs167.<UCRNetID> -DartifactId=<UCRNetID>-lab6 -B
```
Note: Do not forget to replace `<UCRNetID>` with your UCR Net ID.

2. Change into the project directory and type `mvn package` once to make sure that it compiles.
3. To configure your project with Spark, merge the following configuration in `pom.xml`.
```xml
<properties>
  <spark.version>3.2.1</spark.version>
  <scala.compat.version>2.12</scala.compat.version>
  <maven.compiler.source>1.8</maven.compiler.source>
  <maven.compiler.target>1.8</maven.compiler.target>
</properties>

<dependencies>
  <dependency>
    <groupId>org.apache.spark</groupId>
    <artifactId>spark-core_${scala.compat.version}</artifactId>
    <version>${spark.version}</version>
    <scope>compile</scope>
  </dependency>
</dependencies>
```
4. Import your project into IntelliJ IDEA in the same way you did in previous labs and make sure it compiles. Run the main function in `App` class to make sure it works.
5. To make it easier to run your code from the JAR file, add the following part to the plugins section in your `pom.xml` file.
```xml
<plugin>
  <groupId>org.apache.maven.plugins</groupId>
  <artifactId>maven-jar-plugin</artifactId>
  <configuration>
    <archive>
      <manifest>
        <mainClass>edu.ucr.cs.cs167.[UCRNetID].App</mainClass>
      </manifest>
    </archive>
  </configuration>
</plugin>
```

Note: Do not forget to replace `[UCRNetID]` with your net ID.

### II. Initialize with Spark RDD (5 minutes)
In this part, you will initialize your project with Spark.
1. In `App` class, add the following stub code. (Do not remove the package line in your code)
```scala
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object App {

  def main(args : Array[String]) {
    val command: String = args(0)
    val inputfile: String = args(1)

    val conf = new SparkConf
    if (!conf.contains("spark.master"))
      conf.setMaster("local[*]")
    println(s"Using Spark master '${conf.get("spark.master")}'")
    conf.setAppName("lab6")
    val sparkContext = new SparkContext(conf)
    try {
      val inputRDD: RDD[String] = sparkContext.textFile(inputfile)
      // TODO Parse the input file using the tab separator and skip the first line
      val t1 = System.nanoTime
      command match {
      case "count-all" =>
        // TODO count total number of records in the file
      case "code-filter" =>
        // TODO Filter the file by response code, args(2), and print the total number of matching lines
      case "time-filter" =>
        // TODO Filter by time range [from = args(2), to = args(3)], and print the total number of matching lines
      case "count-by-code" =>
        // TODO Group the lines by response code and count the number of records per group
      case "sum-bytes-by-code" =>
        // TODO Group the lines by response code and sum the total bytes per group
      case "avg-bytes-by-code" =>
        // TODO Group the liens by response code and calculate the average bytes per group
      case "top-host" =>
        // TODO print the host the largest number of lines and print the number of lines
      }
      val t2 = System.nanoTime
      println(s"Command '${command}' on file '${inputfile}' finished in ${(t2-t1)*1E-9} seconds")
    } finally {
      sparkContext.stop
    }
  }
}
```

Note: Unlike the switch statement in C and Java, the [match statement](https://docs.scala-lang.org/tour/pattern-matching.html) in Scala does not require a break at the end of each case.

2. Take a few minutes to check the stub code and understand what it does. It has two required command-line arguments. (Q1) What are these two arguments?

### III. Read and parse the input file (10 minutes)
1. Since most of the commands will need to split the input line and skip the first line, let us do this first.
2. Use a filter transformation to skip the first line. For simplicity, we will detect the first line as the line that starts with `"host\tlogname"`
3. Use a map transformation to split each line using the tab character `"\t"` as a separator.
4. Note that since the filter and map operations are transformations, not actions, none of them will be executed until you use them.
5. After parsing, you should have an RDD declared as follows:
```scala
val parsedLines: RDD[Array[String]] = ...
```
6. A few commands in the next sections may require more than 2 arguments.

### IV. `count-all` and `code-filter` (10 minutes)
1. The `count-all` command should use the method [`RDD#count`](https://spark.apache.org/docs/latest/api/scala/org/apache/spark/rdd/RDD.html#count():Long) which is an action to count the total number of records in the input file. Below is the expected output for the two sample files.
```text
Total count for file 'nasa_19950801.tsv' is 30969
Total count for file '19950630.23-19950801.00.tsv' is 1891709
```
Hint: Use the following statement to print the output as shown above.
```scala
println(s"Total count for file '${inputfile}' is ${count}")
```

2. The `code-filter` command should count the lines that match a desired response code. The desired code is provided as a third command line argument. This method should use the [`filter`](https://spark.apache.org/docs/latest/api/scala/org/apache/spark/rdd/RDD.html#filter(f:T=>Boolean):org.apache.spark.rdd.RDD[T]) transformation followed by the `count` action. Below is the expected output for the two sample files.
```text
Total count for file 'nasa_19950801.tsv' with response code 200 is 27972
Total count for file '19950630.23-19950801.00.tsv' with response code 302 is 46573
```
* Note: For all commands in this lab, make sure that you write the output to the standard output using the `println` command and that the output looks *exactly* the same to the expected output. We will use a script to automatically check your answer and it will use regular expressions to match the answer. Any minor change in this expected output might reduce your grade for this lab. For the command above, use the following print statement:
```scala
println(s"Total count for file '${inputfile}' with response code ${responseCode} is ${count}")
```
* Hint: To make your code more readable, you can add constants for each attribute to access them by name instead of number. See the following code snippet for an example.
```scala
val ResponseCode: Int = 5
val code: String = line.split("\\t")(ResponseCode)
```
* Note: In Scala, the keyword `val` declares a constant while the keyword `var` declares a variable.

### V. `time-filter` (10 minutes)
1. In this part, we need to count the number of lines that have a timestmap in a given range `[start, end]`.
2. The interval is given as two additional arguments as integers.
3. Do not forget to use the method [`String#toLong`](https://www.scala-lang.org/api/2.13.6/scala/collection/StringOps.html#toLong:Long) in Scala to convert the String argument to a long integer to enable numeric comparison.
4. Similar to `code-filter`, you will need a filter followed by count to complete this part. The filter will be:
```scala
line(2).toLong >= from && line(2).toLong <= to
```
5. Two sample outputs are given below.
```text
Total count for file 'nasa_19950801.tsv' in time range [807274014, 807283738] is 6389
Total count for file '19950630.23-19950801.00.tsv' in time range [804955673, 805590159] is 554919
```
Hint: You can use the following print command:
```scala
println(s"Total count for file '$inputfile' in time range [$from, $to] is $count")
```

### VI. `count-by-code` (10 minutess)
1. This part requires grouping the records by response code first. In Scala, this is done using a map operation that returns a tuple `(key,value)`.
2. You can directly count each group using the function [`countByKey`](https://spark.apache.org/docs/latest/api/scala/org/apache/spark/rdd/PairRDDFunctions.html#countByKey():scala.collection.Map[K,Long]).
3. To print the output on the resulting map, you can use the method `foreach` on that map. A sample output is given below.
```text
Number of lines per code for the file 'nasa_19950801.tsv'
Code,Count
404,221
200,27972
302,355
304,2421



Number of lines per code for the file 19950630.23-19950801.00.tsv
Code,Count
302,46573
501,14
404,10845
500,62
403,54
304,132627
200,1701534
```
* Hint: Use the following set of commands to print the output shown above:
```scala
println(s"Number of lines per code for the file '$inputfile'")
println("Code,Count")
counts.foreach(pair => println(s"${pair._1},${pair._2}"))
```
* Note: In Scala, the expression `(x,y)` creates a tuple with the two given values. You can similarly create tuples with more values, e.g., `(x,y,z)` for a triplet. Tuples in Scala are immutable, i.e., once created, you cannot modify them.
* In the Scala API, there is no explicit defintion for a PairRDD. Any RDD that has a value of type Tuple2, i.e., a tuple with two values, will be automatically treated as a pair RDD.

### VII. `sum-bytes-by-code` and `avg-bytes-by-code` (15 minutes)
1. This method is similar to the previous one except that it will calculate the summation of bytes for each code.
2. To do that, you can first use the [`map`](https://spark.apache.org/docs/latest/api/scala/org/apache/spark/rdd/RDD.html#map[U](f:T=>U)(implicitevidence$3:scala.reflect.ClassTag[U]):org.apache.spark.rdd.RDD[U]) function to produce only the `code` and the `bytes`. Then, you can use the mehod [`reducyByKey`](https://spark.apache.org/docs/latest/api/scala/org/apache/spark/rdd/PairRDDFunctions.html#reduceByKey(func:(V,V)=>V):org.apache.spark.rdd.RDD[(K,V)]) to compute the summation.
3. The reduce method is Spark is different that the reduce method in Hadoop. Instead of taking all the values, it only takes two values at a time. To compute the summation, your reduce function should return the sum of the two values given to it.
4. Since reduceByKey is a transformation, you will need to use the [`collect`](https://spark.apache.org/docs/latest/api/scala/org/apache/spark/rdd/RDD.html#collect():Array[T]) action to get the results back.
5. A sample output is given below.
```text
Total bytes per code for the file 'nasa_19950801.tsv'
Code,Sum(bytes)
404,0
200,481974462
302,26005
304,0




Total bytes per code for the file 19950630.23-19950801.00.tsv
Code,Sum(bytes)
501,0
403,0
304,0
200,38692291442
404,0
302,3682049
500,0
```
Note: The order of the output might be different than the one shown above.
6. For `avg-bytes-by-code` you need to compute the average, rather than the summation. A simple reduce function cannot be used to compute the average since the average function is not associative. However, it can be computed using a combination of sum and count.
7. The easiest way to compute the average is to combine the output of the two commands `count-by-code` and `sum-bytes-by-code`. The average is simply the sum divided by count.
8. Bonus (+3 points): The drawback of the above method is that it will need to scan the input twice to count each function, sum and count. It is possible to compute both functions in one scan over the input and without caching any intermediate RDDs. Complete this part to get three bonus points on this lab. Explain your method in the README file and add the code snippet that performs this task. Mark your answer with (B). Hint, check the [aggregateByKey](https://spark.apache.org/docs/latest/api/scala/org/apache/spark/rdd/PairRDDFunctions.html#aggregateByKey[U](zeroValue:U)(seqOp:(U,V)=>U,combOp:(U,U)=>U)(implicitevidence$3:scala.reflect.ClassTag[U]):org.apache.spark.rdd.RDD[(K,U)]) function.
9. A sample output is given below.
```text
Average bytes per code for the file 'nasa_19950801.tsv'
Code,Avg(bytes)
404,0.0
200,17230.604247104246
302,73.25352112676056
304,0.0



Average bytes per code for the file '19950630.23-19950801.00.tsv'
Code,Avg(bytes)
501,0.0
403,0.0
304,0.0
200,22739.652244386536
404,0.0
302,79.0597341807485
500,0.0
```

### VIII. `top-host` (10 minutes)
1. In this part we want to count the number of entries per host and output the one with the highest number of entries.
2. While we could use the function `countByKey` it could be inefficient since it returns all the values to the driver node. Unlike the response codes, there could be too many distinct values of `host` and we do not want to return all of them.
3. Instead of `countByKey` we will use the method `reduceByKey` which runs as a transformation and keeps the result in an RDD.
4. After that, we will use the transformation [`sortBy`](https://spark.apache.org/docs/latest/api/scala/org/apache/spark/rdd/RDD.html#sortBy[K](f:T=%3EK,ascending:Boolean,numPartitions:Int)(implicitord:Ordering[K],implicitctag:scala.reflect.ClassTag[K]):org.apache.spark.rdd.RDD[T]) to sort the results in *descending* order.
5. Finally, we will use the action [`first`](https://spark.apache.org/docs/latest/api/scala/org/apache/spark/rdd/RDD.html#first():T) to return only the first value.
6. Sample output
```text
Top host in the file 'nasa_19950801.tsv' by number of entries
Host: edams.ksc.nasa.gov
Number of entries: 364



Top host in the file 19950630.23-19950801.00.tsv by number of entries
Host: piweba3y.prodigy.com
Number of entries: 17572
```

## Part B. Spark SQL

In this part, we will repeat the same work done above using SparkSQL to see the difference. If you are not familiar with SQL, check this [SQL Tutorial](https://www.w3schools.com/sql/).

### I. Project Setup (1 minute)

1. In the `pom.xml` file, add the following dependency.
```xml
<dependency>
  <groupId>org.apache.spark</groupId>
  <artifactId>spark-sql_${scala.compat.version}</artifactId>
  <version>${spark.version}</version>
</dependency>
```

### II. Initialize a SparkSession (5 minutes)
Create a new Scala class of type Object named `AppSQL`. Add the following stub code to it.

```scala
import org.apache.spark.sql.SparkSession
import org.apache.spark.SparkConf

object AppSQL {

  def main(args : Array[String]) {
    val conf = new SparkConf
    if (!conf.contains("spark.master"))
      conf.setMaster("local[*]")
    println(s"Using Spark master '${conf.get("spark.master")}'")

    val spark = SparkSession
      .builder()
      .appName("CS167 Lab6")
      .config(conf)
      .getOrCreate()

    try {
      // Your code will go here
    } finally {
      spark.stop
    }
  }
}
```

* Note: To create a new Scala object, check the following instructions.

![Create scala class](images/new-scala-class.png)

![Create scala object](images/new-scala-object.png)

* Note: A Scala object is a Singleton class with one object instantiated automatically. All methods inside the object are treated as static methods.

### III. Read and parse the input file (5 minutes)
Spark SQL is equipped with a CSV parser that can read semi-structured CSV files.
1. Use the following code to open the sample file and print the first few lines.
```scala
val input = spark.read.format("csv")
  .option("sep", "\t")
  .option("inferSchema", "true")
  .option("header", "true")
  .load("nasa_19950801.tsv")

import spark.implicits._

input.show()
```
The output should look similar to the following:
```text
+--------------------+-------+---------+------+--------------------+--------+------+-------+---------+
|                host|logname|     time|method|                 url|response| bytes|referer|useragent|
+--------------------+-------+---------+------+--------------------+--------+------+-------+---------+
|pppa006.compuserv...|      -|807256800|   GET|/images/launch-lo...|     200|  1713|   null|     null|
|  vcc7.langara.bc.ca|      -|807256804|   GET|/shuttle/missions...|     200|  8677|   null|     null|
|pppa006.compuserv...|      -|807256806|   GET|/history/apollo/i...|     200|  1173|   null|     null|
|thing1.cchem.berk...|      -|807256870|   GET|/shuttle/missions...|     200|  4705|   null|     null|
|       202.236.34.35|      -|807256881|   GET|     /whats-new.html|     200| 18936|   null|     null|
|bettong.client.uq...|      -|807256884|   GET|/history/skylab/s...|     200|  1687|   null|     null|
|       202.236.34.35|      -|807256884|   GET|/images/whatsnew.gif|     200|   651|   null|     null|
|       202.236.34.35|      -|807256885|   GET|/images/KSC-logos...|     200|  1204|   null|     null|
|bettong.client.uq...|      -|807256900|   GET|/history/skylab/s...|     304|     0|   null|     null|
|bettong.client.uq...|      -|807256913|   GET|/images/ksclogosm...|     304|     0|   null|     null|
|bettong.client.uq...|      -|807256913|   GET|/history/apollo/i...|     200|  3047|   null|     null|
|        hella.stm.it|      -|807256914|   GET|/shuttle/missions...|     200|513911|   null|     null|
|mtv-pm0-ip4.halcy...|      -|807256916|   GET| /shuttle/countdown/|     200|  4324|   null|     null|
|   ednet1.osl.or.gov|      -|807256924|   GET|                   /|     200|  7280|   null|     null|
|mtv-pm0-ip4.halcy...|      -|807256942|   GET|/shuttle/countdow...|     200| 46573|   null|     null|
|dd10-046.compuser...|      -|807256943|   GET|/shuttle/missions...|     200| 10566|   null|     null|
|ad11-013.compuser...|      -|807256944|   GET|/history/history....|     200|  1602|   null|     null|
|dd10-046.compuser...|      -|807256946|   GET|/shuttle/missions...|     200|  8083|   null|     null|
|dd10-046.compuser...|      -|807256954|   GET|/images/KSC-logos...|     200|  1204|   null|     null|
|dd10-046.compuser...|      -|807256954|   GET|/history/apollo/i...|     200|  1173|   null|     null|
+--------------------+-------+---------+------+--------------------+--------+------+-------+---------+
only showing top 20 rows
```
2. The `option("sep", "\t")` configures the reader with the tab separator so there is no need to manually split each line.
3. The `option("header", "true")` will do two things. First, it will skip the first line in the file so that you do not have to manually remove it. Second, it will use the column names in that line to access the attribute so you do not need to access them by number.
4. The `option("inferSchema", "true")` tells Spark to infer the schema based on the values in the file. For example, the time will be parsed as an integer number which allows the use of range comparison.
5. To check the inferred schema, add the following statement.
```scala
input.printSchema()
```
The output should look similar to the following:
```text
root
 |-- host: string (nullable = true)
 |-- logname: string (nullable = true)
 |-- time: integer (nullable = true)
 |-- method: string (nullable = true)
 |-- url: string (nullable = true)
 |-- response: integer (nullable = true)
 |-- bytes: integer (nullable = true)
 |-- referer: string (nullable = true)
 |-- useragent: string (nullable = true)
```
6. Comment the line `option("inferSchema", "true")` and run your program again. (Q1) What is the type of the attributes `time` and `bytes` this time? Why?
7. To use SQL queries, you should add the following line to create a view named `log_lines` that points to your input.
```scala
input.createOrReplaceTempView("log_lines")
```

### IV. Query the Dataframe using Dataframe Operators (45 minutes)
In this part, we will run some relational operators through the Dataframe/SparkSQL API. The logic of these queries is similar to what we did in part A. This will allow you to compare and contrast the two APIs.

* Note: For each of the following, you are free to use SQL queries directly or build the query using the Dataframe API. Instructions for both are given for each command.

1. Add the following code (similar to part A) to run a user-provided operation.
```scala
val command: String = args(0)
val inputfile: String = args(1)

...

val t1 = System.nanoTime
command match {
  case "count-all" =>
    // TODO count total number of records in the file
  case "code-filter" =>
    // TODO Filter the file by response code, args(2), and print the total number of matching lines
  case "time-filter" =>
    // TODO Filter by time range [from = args(2), to = args(3)], and print the total number of matching lines
  case "count-by-code" =>
    // TODO Group the lines by response code and count the number of records per group
  case "sum-bytes-by-code" =>
    // TODO Group the lines by response code and sum the total bytes per group
  case "avg-bytes-by-code" =>
    // TODO Group the liens by response code and calculate the average bytes per group
  case "top-host" =>
    // TODO print the host the largest number of lines and print the number of lines
}
val t2 = System.nanoTime
println(s"Command '${command}' on file '${inputfile}' finished in ${(t2-t1)*1E-9} seconds")
```
2. The command `count-all` is implemented using the `count` function. The output should look similar to the following.
```text
Total count for file 'nasa_19950801.tsv' is 30969
Total count for file '19950630.23-19950801.00.tsv' is 1891709
```
You can also run this logic using the following SQL function:
```SQL
SELECT count(*)
FROM log_lines
```
The following code snippet shows how to run this SQL query in your code.
```scala
val count = spark.sql(
  """SELECT count(*)
    FROM log_lines""")
  .first()
  .getAs[Long](0)
```
Notice that the return value of any SQL query is always a Dataframe even if it contains a single row or a single value.

Note: An alternative way to call functions in Scala is using spaces instead of dot and parantheses. The following syntax is valid.
```scala
val queryResult = spark sql "SELECT count(*) FROM log_lines" first
val count = queryResult.getAs[Long](0)
```
You can use this alternative syntax wisely to make your code more readable without being too complex to follow.

3. The command `code-filter` should count the records with a give response code. To do that, you will use the `filter` method. The easiest way is to provide the test as a string, e.g., `"response=200"`. Alternatively, you can use the expression `$"response" === 200`. For the latter, make use that you ipmort the implicit coversion using the statement `import spark.implicits._` in your program. The output should look similar to the following.
```text
Total count for file 'nasa_19950801.tsv' with response code 200 is 27972
Total count for file '19950630.23-19950801.00.tsv' with response code 302 is 46573
```

The following SQL command will also return the same result.
```SQL
SELECT count(*)
FROM log_lines
WHERE response=200
```
4. The command `time-filter` should count all the records that happened in a time interval `[from, to]`. The two parameters are provided as the third and forth command line arguments. You will use the `filter` function but this time with the `between` expression. Again, you can just provide the filter predicate as a string, i.e., `"time BETWEEN 807274014 AND 807283738"`, or as a Scala expression, i.e., `$"time".between(807274014, 807283738)`. This will be followed by `count` to count the records. A sample output is given below.
```text
Total count for file 'nasa_19950801.tsv' in time range [807274014, 807283738] is 6389
Total count for file '19950630.23-19950801.00.tsv' in time range [804955673, 805590159] is 554919
```

You can also use the following SQL query.
```SQL
SELECT count(*)
FROM log_lines
WHERE time BETWEEN [from] AND [to]
```
You should replace `[from]` and `[to]` with the correct parameters passed through command line. You can use the [string interpolation](https://docs.scala-lang.org/overviews/scala-book/two-notes-about-strings.html) feature in Scala to keep your code readable.
5. The commands `count-by-code`, `sum-bytes-by-code`, and `avg-bytes-by-code` will all look very similar. You first need to group records by response code using the `groupBy` function, i.e., `groupBy("response")` or `groupBy($"response")`. On the result, you should call the correct aggregate function, i.e., `count`, `sum`, or `avg`. The last two functions take a parameter which is the column name to aggregate, e.g., `sum("bytes")`. You can finally print the result using the `show()` command. The output should look like the following.
```text
Number of lines per code for the file 'nasa_19950801.tsv'
+--------+-----+
|response|count|
+--------+-----+
|     404|  221|
|     200|27972|
|     304| 2421|
|     302|  355|
+--------+-----+



Number of lines per code for the file '19950630.23-19950801.00.tsv'
+--------+-------+
|response|  count|
+--------+-------+
|     501|     14|
|     500|     62|
|     403|     54|
|     404|  10845|
|     200|1701534|
|     304| 132627|
|     302|  46573|
+--------+-------+



Total bytes per code for the file nasa_19950801.tsv
+--------+----------+
|response|sum(bytes)|
+--------+----------+
|     404|         0|
|     200| 481974462|
|     304|         0|
|     302|     26005|
+--------+----------+


Total bytes per code for the file 19950630.23-19950801.00.tsv
+--------+-----------+
|response| sum(bytes)|
+--------+-----------+
|     501|          0|
|     500|          0|
|     403|          0|
|     404|          0|
|     200|38692291442|
|     304|          0|
|     302|    3682049|
+--------+-----------+

Average bytes per code for the file nasa_19950801.tsv
+--------+------------------+
|response|        avg(bytes)|
+--------+------------------+
|     404|               0.0|
|     200|17230.604247104246|
|     304|               0.0|
|     302| 73.25352112676056|
+--------+------------------+



Average bytes per code for the file 19950630.23-19950801.00.tsv
+--------+------------------+
|response|        avg(bytes)|
+--------+------------------+
|     501|               0.0|
|     500|               0.0|
|     403|               0.0|
|     404|               0.0|
|     200|22739.652244386536|
|     304|               0.0|
|     302|  79.0597341807485|
+--------+------------------+
```

Here is one SQL query that you can further customize for the three commands.
```SQL
SELECT response, SUM(bytes)
FROM log_lines
GROUP BY response
```
6. The command `top-host` should group records by host, `groupBy("host")`, then count records in each group `count()`. After that, you should sort the results in descending order by count, `orderBy($"count".desc)`. Finally, return the top result using the method `first()`. The final result will be of type `Row`. To access the host and number of records for the top result, you can use one of the methods `Row#getAs(String)` and `Row#getAs(Int)` which retrieve an attribute by its name and index, respectively. The final output should look similar to the following.
```text
Top host in the file 'nasa_19950801.tsv' by number of entries
Host: edams.ksc.nasa.gov
Number of entries: 364



Top host in the file '19950630.23-19950801.00.tsv' by number of entries
Host: piweba3y.p`rodigy.com
Number of entries: 17572
```
The following SQL query will help you with this part.
```SQL
SELECT host, COUNT(*) AS cnt
FROM log_lines
GROUP BY host
ORDER BY cnt DESC
LIMIT 1
```

7. (Bonus +3 points) Add a new command, `comparison` that counts records by response code before and after a specific timestamp. The timstamp is given as a command-line argument. You can do that by first creating two Dataframes by filtering the input twice. For each Dataframe, you can count the records by response code as done in the operation `count-by-code`. Finally, you can join the results of the two Dataframes by code to place them side-by-side in one Dataset. The join method may look like the following line:
```scala
countsBefore.join(countsAfter, "response")
```
which joins two dataframes, namely, `countsBefore` and `countsAfter`, using the common key `response`. You can then print out the final result using the `show` command as follows.
```text
Comparison of the number of lines per code before and after 807295758 on file 'nasa_19950801.tsv'
+--------+------------+-----------+
|response|count_before|count_after|
+--------+------------+-----------+
|     404|         199|         22|
|     200|       22248|       5724|
|     304|        1925|        496|
|     302|         272|         83|
+--------+------------+-----------+


Comparison of the number of lines per code before and after 805383872 on file '19950630.23-19950801.00.tsv'
+--------+------------+-----------+
|response|count_before|count_after|
+--------+------------+-----------+
|     501|           2|         12|
|     500|          53|          9|
|     403|          19|         35|
|     404|        3864|       6981|
|     200|      594412|    1107122|
|     304|       38000|      94627|
|     302|       21057|      25516|
+--------+------------+-----------+
```
Hint: By default, the name of the column that results from the `count` function is named `count`. You can rename this column in each Datafame separately using the method `withColumnRenamed`, for example, for the count-before dataframe, the stament will be `withColumnRenamed("count", "count_before")`.

## Submission (15 minutes)
1. Add a `README` file with all your answers.
2. If you implemented the bonus tasks, add your explanation and code snippet to the `README` file.
3. Add a `run` script that compiles your code and then runs the following commands with the given parameters on the file `nasa_19950630.22-19950728.12.tsv`. Use only the file name with the assumption that the file is available in the working directory. The script should run all these command twice, once with `App` and once with `AppSQL`.

| Command           | Parameters           |
| ----------------- | -------------------- |
| count-all         |                      |
| code-filter       | 302                  |
| time-filter       | 804955673  805590159 |
| count-by-code     |                      |
| sum-bytes-by-code |                      |
| avg-bytes-by-code |                      |
| top-host          |                      |
| comparison (if implemented for AppSQL)        | 805383872            |

4. As a test, run your script using the following command to redirect the standard output to the file `output.txt` and double check that the answers in your file are the same to the ones listed earlier in this lab for the file `nasa_19950630.22-19950728.12.tsv`.
```shell
./run.sh > output.txt
```
5. Similar to all labs, do not include any additional files such as the compiled code, input, or output files.

## Further Readings
The following reading material could help you with your lab.
* [RDD Programming Guide](http://spark.apache.org/docs/latest/rdd-programming-guide.html)
* [RDD API Docs](http://spark.apache.org/docs/latest/api/scala/index.html#org.apache.spark.rdd.RDD)
* [Spark SQL Programming Guide](http://spark.apache.org/docs/latest/sql-getting-started.html)
* [Dataset API Docs](hhttp://spark.apache.org/docs/latest/api/scala/index.html#org.apache.spark.sql.Dataset)
* [SQL Tutorial](https://www.w3schools.com/sql/)

## FAQ
* Q: My code does not compile using `mvn package`.
* A: Check your `pom.xml` file and make sure that the following sections are there in your file.
```xml
  <properties>
    <maven.compiler.source>1.8</maven.compiler.source>
    <maven.compiler.target>1.8</maven.compiler.target>
    <encoding>UTF-8</encoding>
    <scala.version>2.12.6</scala.version>
    <scala.compat.version>2.12</scala.compat.version>
    <spec2.version>4.2.0</spec2.version>
    <spark.version>3.2.1</spark.version>
  </properties>


  <dependencies>
    <dependency>
      <groupId>org.scala-lang</groupId>
      <artifactId>scala-library</artifactId>
      <version>${scala.version}</version>
    </dependency>
    <dependency>
      <groupId>org.apache.spark</groupId>
      <artifactId>spark-core_${scala.compat.version}</artifactId>
      <version>${spark.version}</version>
      <scope>compile</scope>
    </dependency>

    <!-- Test -->
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>4.12</version>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>org.scalatest</groupId>
      <artifactId>scalatest_${scala.compat.version}</artifactId>
      <version>3.0.5</version>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>org.specs2</groupId>
      <artifactId>specs2-core_${scala.compat.version}</artifactId>
      <version>${spec2.version}</version>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>org.specs2</groupId>
      <artifactId>specs2-junit_${scala.compat.version}</artifactId>
      <version>${spec2.version}</version>
      <scope>test</scope>
    </dependency>
  </dependencies>

  <build>
    <sourceDirectory>src/main/scala</sourceDirectory>
    <testSourceDirectory>src/test/scala</testSourceDirectory>
    <plugins>
      <plugin>
        <!-- see http://davidb.github.com/scala-maven-plugin -->
        <groupId>net.alchim31.maven</groupId>
        <artifactId>scala-maven-plugin</artifactId>
        <version>3.3.2</version>
        <executions>
          <execution>
            <goals>
              <goal>compile</goal>
              <goal>testCompile</goal>
            </goals>
            <configuration>
              <args>
                <arg>-dependencyfile</arg>
                <arg>${project.build.directory}/.scala_dependencies</arg>
              </args>
            </configuration>
          </execution>
        </executions>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-surefire-plugin</artifactId>
        <version>2.21.0</version>
        <configuration>
          <!-- Tests will be run with scalatest-maven-plugin instead -->
          <skipTests>true</skipTests>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.scalatest</groupId>
        <artifactId>scalatest-maven-plugin</artifactId>
        <version>2.0.0</version>
        <configuration>
          <reportsDirectory>${project.build.directory}/surefire-reports</reportsDirectory>
          <junitxml>.</junitxml>
          <filereports>TestSuiteReport.txt</filereports>
          <!-- Comma separated list of JUnit test class names to execute -->
          <jUnitClasses>samples.AppTest</jUnitClasses>
        </configuration>
        <executions>
          <execution>
            <id>test</id>
            <goals>
              <goal>test</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
    </plugins>
  </build>
```

* Q: IntelliJ IDEA does not show the green run arrow next to the `App` class.
* A: Check if there is a message at the top asking you to set up Scala SDK. Click on that button and follow the instructions to install the default SDK version 2.12.

* Q: IntelliJ IDEA does not recognize Scala code.
* A: Make sure that the Scala plugin is installed on IntelliJ IDEA.

* Q: I could not create the initial Maven project from command-line.
* A: As an alternative solution, you can create the project directly from IntelliJIDEA as a new Maven project with the correct archetype information.

![New Maven Project](images/1.new-maven-project.png)

![Select archetype if listed](images/2.archetype-selected.png)

If you do not find the archetype listed, click `Add Archetype` and enter the information below.

![Add archetype](images/3.scala-archetype.png)

Finally, set the correct information for your groupId and artifactId as shown below.
![Set groupId](images/4.set-groupId.png)
![Set artifactId](images/5.set-artifactId.png)