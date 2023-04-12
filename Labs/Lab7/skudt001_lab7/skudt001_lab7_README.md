# Lab 7

## Student information

* Full name: Siraaj Kudtarkar
* E-mail: skudt001@ucr.edu
* UCR NetID: skudt001
* Student ID: 862129207

## Answers

* (Q1) What is your command to import the `contact.json` file?

    ```shell
    mongoimport --collection=contacts --jsonArray /Users/siraaj/cs167/contacts.json
    ```

* (Q2) What is the output of the import command?

    ```text
    2023-02-23T14:01:36.211-0800	connected to: mongodb://localhost/
  2023-02-23T14:01:36.392-0800	1000 document(s) imported successfully. 0 document(s) failed to import.
    ```

* (Q3) What is your command to retrieve all users sorted by Name in ascending order?

    ```javascript
    db.contacts.find().sort({Name:1}).pretty()
    ```

* (Q4) What is your command to retrieve only the `_id` and `Name` sorted in reverse order by `Name`?

    ```javascript
    db.contacts.find({}, {Name:1}).sort({Name:-1})
    ```

* (Q5) Is the comparison of the attribute `Name` case-sensitive?

  Yes, the comparison is case-sensitive.

* (Q6) Explain how you answered (Q5). Show the commands that you ran and how would the output tell you if MongoDB applied case-sensitive or case-insensitive.
  I tested with "apple" and "Berry" as names. I used the command 
  *     db.contacts.insertMany({Name: "apple"}, {Name: "Berry"})
  * Once I sorted it in descending order, I got apple listed below Zoey Stevens so it bases sorting off the ASCII value. The sorting command I used is below for descending order and I changed the sorting to 1 for ascending order to find Berry while also using the command "it" to show more results.
    *     db.contacts.find({},{Name:1}).sort({Name:-1})

* (Q7) What is the command that retrieves the results in sorted order but without the `_id` field?

    ```javascript
  db.contacts.find({},{_id:0,Name:1}).sort({Name:-1})
    ```


* (Q8) What is the command to insert the sample document? What is the result of running the command?

  Command:
    ```javascript
    db.contacts.insertOne({Name: {First: "Yuan", Last: "Zhang"}})
    ```

  Result:
    ```json
    {
  acknowledged: true,
  insertedId: ObjectId("63f8006fd55e71ebcec48067")
  }
      ```

* (Q9) Does MongoDB accept this document while the `Name` field has a different type than other records?
  Yes, the document is accepted.


* (Q10) What is your command to insert the record `{Name: ["Yuan", "Zhang"]}`?

    ```javascript
    db.contacts.insertOne({Name: ["Yuan", "Zhang"]})
    ```


* (Q11) Where did the two new records appear in the sort order?
  At the bottom of the list for { First: Yuan, Last: Zhang } in descending order. Yuan, Zhang wasn't far below it in between Yuna Williamson and Zoey Freeman in descending order.


* (Q12) Why did they appear at these specific locations?
  the { character is greater than the ' character. Yuan, Zhang wasn't far below it in between Yuna Williamson and Zoey Freeman because it is in descending order and the [ character is less than the { character. 

* (Q13) Where did the two records appear in the ascending sort order? Explain your observation.
  Because both of these were at the top in descending order, they are now at the bottom. The ascending and descending sorted lists are essentially just reversed or inverted of each other.


* (Q14) Is MongoDB able to build the index on that field with the different value types stored in the `Name` field?
  Yes, it is able to build the index on that field.

* (Q15) What is your command for building the index?

    ```javascript
    db.contacts.createIndex({Name:1})
    ```


* (Q16) What is the output of the create index command?

    ```text
  Name_1
    ```