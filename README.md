# ScheduleConsult

ScheduleConsult is a desktop application developed in JavaFX that allows users to manage appointments for a global consulting organization. The application pulls data from a MySQL database provided by the organization, which cannot be modified. The application was developed in IntelliJ Idea 2022.3.2 Ultimate Edition using Java 17.0.6, JavaFX SDK 17.0.1, and MySQL Connector/J 8.0.25. ScheduleConsult v1.0 released March 6, 2023.

## Requirements

The following business requirements were provided by the developer:

- Users must be able to log in to the application with their credentials.
- Users must be able to view, add, modify, and delete appointments.
- Appointments must include an ID, title, description, location, contact, start time, end time, customer ID, and user ID.
- Appointments of a customer must not overlap with each other.
- Appointments must be displayed in the user's local time zone.
- Customers must include an ID, name, address, postal code, phone number, and division ID.
- Users must be able to view appointments by month or week.
- Users must be able to generate a report of the number of appointments by type and month.
- Users must be able to generate a report of the schedule for each consultant.
- The application must support French language translation on the log-in screen.
- The application must display the date and time in the appropriate format for the user's locale.

## Setup

To run ScheduleConsult, you will need to have Java 17.0.6 or later installed on your machine. You will also need to have JavaFX SDK 17.0.1 and MySQL Connector/J 8.0.25 installed and linked in your project.

To run the application from IntelliJ IDEA, open the Main.java file located in the `com.cameronm.scheduleconsult` package. Right-click anywhere in the file and select "Run Main.main()". This will launch the application.

Before running the application, make sure that you have a MySQL server running on localhost. The configuration settings for the database connection can be found in the `com.cameronm.scheduleconsult.settings` package.

## Reports

In addition to the business requirements listed above, ScheduleConsult includes an additional report called "Modified Appointments". This report displays the date and time of when a user modified an appointment from its original state.

## Developer Information

ScheduleConsult was developed by Cameron M, February - March 2023.