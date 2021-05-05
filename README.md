STEPS TO RUN PROJECT :

 1. Clone the project and import the project as maven project in eclipse IDE
 2. open eclipse ide > Click on FILE > Import > Maven > Existing Maven Projects > Now Browse the location where u have cloned project in local > Finish
 3. Once project is loaded : Right click on Project > Maven > Update Project > Check on "Force update of snapshot/releases" > OK
 4. Make sure Maven is installed in your computer and Maven Path is added in System environment variable (check using "mvn --version" command in cmd)
 6. To receive email notification:
     > U need to have a mail from which mails will be sent automatically 
     > open the gmail from which you want to send the email
     > On top right corner click on your google account icon > Manage your Gooogle account > Security > (ensure 2 steps verification is ON ...else enable the 2 steps verification)
     > Click on "App Passwords" > Authenticate > Under select App select "Other (Custom Name)" > Type any name > click on generate 
     > it will generate a app password marked in yellow color > copy it
7. Now go to project > open application.properties file under source/main/resources folder
     > under spring.mail.username add the email id from which u want to send email (ie email id for which we have generated App password)
     > under spring.mail.password add the generated app password
     > under receiverMailId add the mail id on which you want to get notification
     > under districtId  you need to add your district id (find district id from cowin website ..select your state and district > open chrome developer tools > network tab
     > now hit enter > you will find district id in api call url ...ex: calendarByDistrict?district_id=686&date=05-05-2021
 8. Now to run from eclipse :
    > open ApointmentNotifier.java file under src/main/java folder > right click > run as 1 java application 
    > (BOOM !!! Now project is up and running and it will send email notification as soon as vaccine is available for 18+ in your district)
    > if currently no slots are available then it will wait for 5 mins and again check.
 9   To run project from CMD :
    >  open eclipse > right click on project > Properties > copy project location under resource
    >  open CMD > cd "paste path here" > enter
    >  Type command "mvn install" > after running it will create a jar file with name "VaccineAppointmentNotifier-0.0.1-SNAPSHOT.jar" in "\VaccineAppointmentNotifier\VaccineAppointmentNotifier\target" location
    >  now open cmd under "\VaccineAppointmentNotifier\VaccineAppointmentNotifier\target" location
    >  run java - jar VaccineAppointmentNotifier-0.0.1-SNAPSHOT.jar --server.port="port no on which we want to run our app" --districtId="district id for whih we need to find appointment" --receiverMailId="receiver email id"
    >  eg: java - jar VaccineAppointmentNotifier-0.0.1-SNAPSHOT.jar --server.port=8888 --districtId=265 --receiverMailId=SomeValidMailId@gmail.com
    >  note --configurations specified above are optional and if not specified then values from application.properties will be considered else new provided values will be considered.
  
  
  NOTE : This service will search for appointment availability for given district every 5 mins until an appointment for 18+ is found and it will mail data for next 7 days starting from today
  
