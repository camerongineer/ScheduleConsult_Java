# ScheduleConsult

ScheduleConsult is a desktop application developed in JavaFX that allows users to manage appointments for a global consulting organization. The application pulls data from a MySQL database provided by the organization, which cannot be modified. The application was developed in IntelliJ Idea 2022.3.2 Ultimate Edition using Java 17.0.6, JavaFX SDK 17.0.1, and MySQL Connector/J 8.0.25. ScheduleConsult v1.0 released March 6, 2023.

## Requirements

The following business requirements were provided by the developer:

#### - Users must be able to log in to the application with their credentials.
![login](https://github.com/camerongineer/ScheduleConsult_Java/assets/93474097/635b5f69-f06b-4c7e-a6e7-af4d6abe7bf8)


#### - Users must be able to view, add, modify, and delete appointments.

 ![appt](https://github.com/camerongineer/ScheduleConsult_Java/assets/93474097/823c3ccc-2608-482e-96db-4425aaa5c774)
 
- Appointments must include an ID, title, description, location, contact, start time, end time, customer ID, and user ID.
- Appointments of a customer must not overlap with each other.
- Appointments must be displayed in the user's local time zone.
- Customers must include an ID, name, address, postal code, phone number, and division ID.
- Users must be able to view appointments by month or week.
- Users must be able to generate a report of the number of appointments by type and month.
- Users must be able to generate a report of the schedule for each consultant.
#### - The application supports French & English language on the log-in screen.
<img width="48%" alt="french_login" src="https://github.com/camerongineer/ScheduleConsult_Java/assets/93474097/3e1aee08-1f37-43ff-bdc0-c074a3fb8f64">
<img width="48%" alt="english_login" src="https://github.com/camerongineer/ScheduleConsult_Java/assets/93474097/fd70dccf-3c39-441f-8604-59e345b8ece5">

- The application must display the date and time in the appropriate format for the user's locale.


## Setup

To run ScheduleConsult, you will need to have Java 17.0.6 or later installed on your machine. You will also need to have JavaFX SDK 17.0.1 and MySQL Connector/J 8.0.25 installed and linked in your project.

To run the application from IntelliJ IDEA, open the Main.java file located in the `com.cameronm.scheduleconsult` package. Right-click anywhere in the file and select "Run Main.main()". This will launch the application.

Before running the application, make sure that you have a MySQL server running on localhost. The configuration settings for the database connection can be found in the `com.cameronm.scheduleconsult.settings` package.

## Reports

In addition to the business requirements listed above, ScheduleConsult includes an additional report called "Modified Appointments". This report displays the date and time of when a user modified an appointment from its original state.

## Developer Information

ScheduleConsult was developed by Cameron M, February - March 2023.
