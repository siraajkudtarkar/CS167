This describes the potential projects for CS167. More details will be added soon for each of them.

# Project A: Chicago Crimes
In this project, you are asked to analyze Chicago Crime dataset by location. You will use Parquet file format to prepare the data for analysis. You will use a mix of SparkSQL and Beast to perform the analysis. The results will be visualized on a map and using a bar chart.

## Datasets
This project will work with [Chicago Crime](https://star.cs.ucr.edu/?Chicago%20Crimes#center=41.8756,-87.6227&zoom=11) dataset by Chicago City and the [ZIP Codes](https://star.cs.ucr.edu/?TIGER2018/ZCTA5#center=41.8756,-87.6227&zoom=11) dataset by the US Census Bureau.

## Task A: Data preparation
This task prepares the data for analysis by doing the following:

- Parse the dataset from its CSV format.
- Rename some of the columns that contain white space to make it easier to process in SQL.
- Introduce a ZIP Code column that associates each crime with a ZIP code based on its location.
- Write te output in Parquet format to make it more efficient for analysis.

## Task B: Spatial analysis
In this task, you need to count the total number of crimes for each ZIP code and plot the results as a choropleth map (see below).

![Choropleth Map](images/CrimesChoroplethMap.png)

## Task C: Temporal analysis
Given start and end dates, count the number of crimes for each crime type and plot as a bar chart (see below).

![Chicago Crime Bar Chart](images/ChicagoCrimesBarChart.png)

## Project B: Bird Analysis
In this project, you will perform some analytical tasks on a dataset that represents bird observations.

### Task 1: Data preparation
To prepare the data for analysis, we will do the following.

- Parse the dataset in its CSV format.
- Rename the columns that contain white space or other special characters in their name to simplify the next steps.
- Introduce the ZIP Code for each bird observation by joining in the ZIPCode dataset.
- Save the converted file in Parquet file format to speed up the next tasks

### Task 2: Spatial analysis
Given a specific bird species, count the percentage of observations for this species among
all observations per ZIP Code and visualize the result as a choropleth map.

## Task 3: Temporal analysis
Given a date range `[start, end]`, find the number of observations of each species and plot the result as a pie chart.

## Project C: Wildfire analysis
In this project, we will perform some analytic tasks on the wildfire dataset in California.

### Task 1: Preparation
To prepare the dataset for analysis, we will do the following.

- Parse the data in its CSV format.
- Drop all the columns that store information about neighbors. We will not need this data for the next step.
- Join with the county dataset and add a new column that gives the county name for each fire observation.
- Write the output in Parquet format.

### Task 2: Spatio-temporal analysis
Given a date range, start and end, compute the total fire intensity for each county over that time. Draw the result as a choropleth map.

### Task 3: Temporal analysis
Given a specific county, compute the total fire intensity each month over all time and plot the result as a line chart.

## Project D: Twitter data analysis
In this project, we will analyze twitter data. Our goal is to build a machine-learning classifier that assigns
a topic to each tweet. The topics are automatically extracted from all tweets based on most frequent hashtags.

### Task 1: Data preparation 1
Clean the dataset by selecting only relevant attributes that will be used in next steps.
Store the converted data in a new CSV file.
Extract the top 20 hashtags in all tweets and store them in an array.

### Task 2: Data preparation 2
Add a new column for each tweet that indicates its topic.
The topic is any of the hashtags that appear in the most frequent list.
If a tweet contains more than one top hashtags, any of the can be used.

### Task 3: Topic prediction
Build a machine learning model that assigns a topic for each tweet based on the classified tweets.
The model should learn the relationship between all features and the topic.
Then, it applies this model to all data to predict one topic for each tweet.
