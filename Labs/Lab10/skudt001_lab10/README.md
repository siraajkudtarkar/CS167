# Lab 10

## Student information

* Full name: Siraaj Kudtarkar
* E-mail: skudt001@ucr.edu
* UCR NetID: skudt001
* Student ID: 862129207

## Answers

* (Q1) What is the result?

  ```text
    [
        3
    ]
  ```

  * (Q2) Which query did you use and what is the answer?

    ```sql
    USE chicago_crimes_sample; 
    SELECT DISTINCT(primary_type) AS gas
    FROM ChicagoCrimes
    WHERE location_description ='GAS STATION';
    ```

    ```text
    [
      {
      "gas": "BATTERY"
      },
      {
      "gas": "MOTOR VEHICLE THEFT"
      },
      {
      "gas": "DECEPTIVE PRACTICE"
      },
      {
      "gas": "ROBBERY"
      },
      {
      "gas": "THEFT"
      }
    ]
        ```

* (Q3) Include the query in your README file

  ```sql
  USE chicago_crimes_sample; 
  SELECT year, count(*)
  FROM ChicagoCrimes
  GROUP BY year
  ORDER BY count(*) desc
  ```

* (Q4) Which `district` has the most number of crimes? Include the query and the answer in the README file.

  ```sql
  USE chicago_crimes_sample; 
  SELECT district, count(*)
  FROM ChicagoCrimes
  WHERE district IS NOT NULL
  GROUP BY district
  ORDER BY count(*) desc
  LIMIT 1;
  ```

  ```text
  [
    {
    "district": 18,
    "$1": 108587
    }
  ]
  ```

* (Q5) Include the query in your submission.

  ```sql
  USE chicago_crimes_sample;
  SELECT year_month, count(year_month) AS count FROM (
  SELECT print_datetime(parse_datetime(date_value, "MM/DD/YYYY hh:mm:ss a"), "YYYY/MM") AS year_month FROM ChicagoCrimes) AS tmp GROUP BY year_month ORDER BY year_month;
  ```

* (Q6) What is the total number of results produced by this query (not only the shown ones)?
* 232 total results