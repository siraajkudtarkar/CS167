# Lab 9

## Student information

* Full name: Siraaj Kudtarkar
* E-mail: skudt001@ucr.edu
* UCR NetID: skudt001
* Student ID: 862129207

## Answers

* (Q1) What is the schema of the file after loading it as a Dataframe

    ```text
    root
     |-- Timestamp: string (nullable = true)
     |-- Text: string (nullable = true)
     |-- Latitude: double (nullable = true)
     |-- Longitude: double (nullable = true)
    ```

* (Q2) Why in the second operation, convert, the order of the objects in the  tweetCounty RDD is (tweet, county) while in the first operation, count-by-county, the order of the objects in the spatial join result was (county, tweet)?

    ```text
    The tweetCounty RDD is necessary to be in the order of tweets first for the conversion process whereas count-by-county has to have county first in order for the program to know what needs to be converted. Join is called first for tweet in tweetCounty RDD and it is called first for county in count-by-county.
    ```

* (Q3) What is the schema of the tweetCounty Dataframe?

    ```text
        root
     |-- Timestamp: string (nullable = true)
     |-- Text: string (nullable = true)
     |-- geometry: geometry (nullable = true)
     |-- CountyID: string (nullable = true)
    ```

* (Q4) What is the schema of the convertedDF Dataframe?

    ```text
         root
      |-- CountyID: string (nullable = true)
      |-- keywords: array (nullable = true)
      |    |-- element: string (containsNull = true)
      |-- Timestamp: string (nullable = true)
    ```

* (Q5) For the tweets_10k dataset, what is the size of the decompressed ZIP file as compared to the converted Parquet file?

  | Size of the original decompressed file | Size of the Parquet file |
  | - | - |
  |  791 KB | 549 KB |

* (Q6) (Bonus) Write down the SQL query(ies) that you can use to compute the ratios as described above. Briefly explain how your proposed solution works.

    ```SQL
    SELECT CountyID, count() AS countyCount
                FROM tweets
                GROUP BY CountyID
    SELECT CountyID, count() AS count
                FROM tweets
                WHERE array_contains(keywords, "$keyword")
                GROUP BY CountyID
    SELECT keyword_counts.CountyID, counties.NAME, counties.g, (keyword_counts.count/countyCounts.countyCount) as TweetProp
                FROM keyword_counts, counties, countyCounts
                WHERE keyword_counts.CountyID = counties.GEOID AND keyword_counts.CountyID = countyCounts.CountyID
    ```

    ```text
    The first query is total count with no conditions. The second query is total count with a keyword as the condition. The third query is the division of the second query to the first query.
    ```