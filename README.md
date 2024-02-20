# ETL-Training Project

ETL = Extract -> Transform -> Load

# Prerequisites
- JDK 8 or higher version
- MySQL v8

# Practiced knowledge

- OOP, File I/O, Database, Exception handling, Regex, Unit test, 

# Issue description

There are 2 file types: firstType and secondType in [Input dir](workspace/input)
- firstType are files have format: {reportDate}：{platformCode}-{accountId}：{adAccountId}：{accountName}.csv
- secondType are files have format: {platformCode}-{accountId}：{accountName}.csv

Files are different format but have same output format

Flow: Input files --> ( Extract -> Transform -> Load ) --> (Filtered csv files + Output csv files)

Mission: From input files, we convert them into filtered files and output files

1. Filtered file: have same name with input file, not contain invalid rows (cost=0, cv=0, imp=0, click=0)
2. Output file: have same format for all files, it has format: report_date,platform_code,platform_name,account_id,ad_account_id,account_name,imp,click,cost,cv

```text
Output file's name:
- for firstType, it is: (1) {platformCode} - {accountId}.csv
- for secondType, it is: (2) {platformCode} - {accountId}.csv

Output file's content:
- ReportDate, it is extracted from fileName for firstType. It is extracted from file content for secondType base on media_setting
- Cost = input file's cost * platform_currency_rate
- Other info are from input file and database
```

MUST: output files use `UTF-8 (with BOM)` encoding

Check by command on MacOS: `file -e encoding "{file_name}"`

3. Save actionLog(fileName, result, executionTime) to database
- fileName: input file name
- result: "Success" or reason of error
- executionTime: at the moment of saving log

Error in cases
- Files aren't firstType or secondType
- Files aren't csv
- Files have invalid platformCode
- Files have no row (except header) after filter

# Setup
- MUST: No change input files: name and content
- Create your DB and then run file [init.sql](sql/init.sql) to create the tables and data.
- Put your DB config to file [application.conf](src/main/resources/application.conf)
- Find and implement files has `TODO` comment. You can create new files to fit your implementation
- After complete the implementation, run the application to test. Compare result with files in workspace/result folder

Good luck!

# Evaluation

Fresher/Junior(1):
- Resolve issue to success
- Readable code
- Have comment to explain on function or class
- Have unit test

Middle/Senior 
- Include (1)
- Have code structure clearly with explanation in Readme
- Have `Elapsed time < 3s` with 2001 input files
- Plus point: Use multi-threading, Design Pattern, Functional Programming, ...