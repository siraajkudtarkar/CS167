#!/usr/bin/env sh
mvn clean package
hadoop jar target/skudt001_lab3-1.0-SNAPSHOT.jar 3 20 5
hadoop jar target/skudt001_lab3-1.0-SNAPSHOT.jar 3 20 3,5
hadoop jar target/skudt001_lab3-1.0-SNAPSHOT.jar 3 20 3v5