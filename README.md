
# Programming task Software Developer - Web Endpoint version (EBI_00971)


You are given a list of comma separated accession numbers as input.

An accession number has the following format: L...LN...N (e.g. AB1234)

where L...L denotes one or more ASCII7 letters and N denotes one or more digits (0,1,2,3,4,5,6,7,8 or 9).

Please return an ordered list of accession numbers where any consecutive digits N...N that share the same prefix L...L are collapsed into an accession number range.
	
An accession number range has the following format : L...LN...N-L...LN...N (e.g. A00001-A00005).

Please note that the N...N digits are left padded using 0s (e.g. 00001) and that the length of the N...N digits must be the same for accession numbers to be part of the same accession number range.

Example input:

(can be found in the following path: EBIProgrammingTaskWebEndpoint/src/main/resources/inputSample.txt) 

A00000, A0001, ERR000111, ERR000112, ERR000113, ERR000115, ERR000116, ERR100114, ERR200000001, ERR200000002, ERR200000003, DRR2110012, SRR211001, ABCDEFG1

Expected output:
A00000, A0001, ABCDEFG1, DRR2110012, ERR000111-ERR000113, ERR000115-ERR000116, ERR100114, ERR200000001-ERR200000003, SRR211001 


## How to build and run the project

This application was created and tested successfully using Java 8, Maven and NetBeans 8.2 IDE on MacOS.

For the implementation of this web endpoint application, the Spring framework was used because it offers
good support for Rest API requirements. Spring Boot was also used because with its embedded server 
technology the deployment and execution of the application is more practical (without having to 
configure and run any stand alone application server such an external Tomcat, also eliminating 
the extra steps of generating/loading the .war file)

The WebServices endpoints were implemented using annotations supported by Spring, which is
an alternate choice over traditional .xml configuration files. 

### To run the Spring Boot application (by default the server port will be set to 8080):

1) Clone the project to your local computer.

2) Run the embedded web Rest endpoint server:

   a. Open the project using NetBeans IDE, then right click on WebStart.java, 
      and press run file. Or...

   b. From the command line, change directory to the cloned folder (e.g. /EBIProgrammingTaskWebEndpoint),
      then type: 

        mvn spring-boot:run


## To run the test file:

1) Make sure the server is up and running (follow the steps described above).    

2) Choose an testing option:
a. Open a new terminal, change directory to the cloned folder (e.g. /EBIProgrammingTaskWebEndpoint), and type:

        mvn test

b. Open the project using NetBeans IDE, then right click on EBIProgrammingTaskWebTest.java,
       and press test file.


## How to send custom messages to the application:

   The application runs as a rest web endpoint server, by default the available URL is:

        http://localhost:8080/EBIProgrammingTaskWebEndpoint

   The endpoint accepts a POST request and with JSON formatted body payload which is a continous
String of comma separated values with format L..LN..N, e.g.:

        {"message": "A00000, A0001, ERR000111, ERR000112, ERR000113, ERR000115, ERR000116, ERR100114, ERR200000001, ERR200000002, ERR200000003, DRR2110012, SRR211001, ABCDEFG1"}

### To see the application in action:

1) Open a new terminal and test with curl, then type:

        curl -X POST http://localhost:8080/EBIProgrammingTaskWebEndpoint -H 'content-type: application/json' -d '{"message": "A00000, A0001, ERR000111, ERR000112, ERR000113, ERR000115, ERR000116, ERR100114, ERR200000001, ERR200000002, ERR200000003, DRR2110012, SRR211001, ABCDEFG1"}' 

2) Use the Postman client (https://www.getpostman.com/)
