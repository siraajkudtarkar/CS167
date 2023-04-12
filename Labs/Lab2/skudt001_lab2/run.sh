#!/usr/bin/env sh
mvn clean package
hadoop jar target/skudt001_lab2-1.0-SNAPSHOT.jar file://`pwd`/AREAWATER.csv hdfs:///AREAWATER.csv
hadoop jar target/skudt001_lab2-1.0-SNAPSHOT.jar hdfs:///AREAWATER.csv file://`pwd`/AREAWATER_copy.csv
hadoop jar target/skudt001_lab2-1.0-SNAPSHOT.jar hdfs:///AREAWATER.csv hdfs:///AREAWATER_copy.csv