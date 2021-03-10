![maven workflow](https://github.com/northernjay885/reporting-system-aws/actions/workflows/maven.yml/badge.svg)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/0cc5df1a96534fd6ab10d1a192ff8974)](https://www.codacy.com/gh/northernjay885/reporting-system-aws/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=northernjay885/reporting-system-aws&amp;utm_campaign=Badge_Grade)

# Report Generating System on AWS
 This project is used mainly for demonstrating purpose. It can generate both excel and pdf format reports from a json file through a website. In the beginning, the user would be asked to authenticate himself. After logging in, the user can perform operations like creating the reports, downloading from AWS S3 bucket and deleting previous records through the website.
## Getting Started
 The project can be reached at an [EC2 instance](http://ec2-52-90-176-41.compute-1.amazonaws.com:8080).<br>

 The test user can be used to access the administrator console. <br>
 
 Account Id:
 ```
 JoeSnow582@gmail.com
 ```
 Password:
 ```
 78MmaoY6g5N#
 ```
 

## 2. Understand the structure and details
Look at the [ReportingSystemArchitecture.pdf](./ReportingSystemArchitecture.pdf)

## 3. Make improvement in the code/system level.
Suggestions:
0. Add new features like update/delete/edit report.
1. Improve sync API performance by using multithreading and sending request concurrently to both services.
2. Use a database instead of hashmap in the ExcelRepositoryImpl.
3. Improve code coverage by adding more tests.
4. Convert sync API into microservices by adding Eureka/Ribbon support.
5. Add pressure tests to benchmark the system.
6. Change MongoDB to DynamoDB.
7. Fix bugs.
8. Make the system more robust by adding fault tolerance such like : DeadLetter Queue, retry, cache, fallback etc.
9. Add security and jwt support.
10. Add more fancy UI using angular/react.
11. Setup your CI/CD pipeline.
12. Add new Services like PNGService, JPEGService etc
13. ...

## 4. Send your code to [Dawei Zhuang(dawei.zhuang@antra.com)](dawei.zhuang@antra.com) using Github/Gitlab. 
Make sure there is README.MD to indicate what did you change/add to the project.

