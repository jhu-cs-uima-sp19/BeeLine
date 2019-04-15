# BeeLine
Made by: Isaiah, Nick, Nancy, Bronte (Team B)

Beeline is a service that seeks to create ridesharing groups to lower the cost of going from point A to point B(ee). The most recent iteration of our Android application enables users to view their user profile and planned trips, in addition to finding and creating new trips.

## First Sprint Objectives:
- [x] Create **Account Activity** Sprint 1 version. Accounts should have: Name and Bio

- [x] Create **Create Beeline Activity** that allows a user to enter trip details to create a trip. Fields for input include:
  - Trip destination
  - Trip time, date, and meet-up location
  - Save trip details permanently after user creates a trip and presses the “SAVE” button

- [x] Create **Account class** to store user data. Must be serializable into something that can be stored into our database.

- [x] Set up **Firebase database.**
  - Ensure that users can authenticate using email and password
  - Ensure we can retrieve user account based on login credentials

- [x] Create **Title Screen Activity** (Sprint 1 version)
  - Just show image, automatically switch to Dashboard w/ hardcoded user
  
- [x] Create **Nav Drawer**:
  - My Beelines
  - Find Beelines
 
 Our current version is able to create new Beeline from a Create Beelines option in the Find Beelines. This action adds the event to the Firebase Database and displays on the Find Beelines screen, connecting our Back End with our Front End.

You must also include a README file that details what features have been completed so far and the general state of your app to make it easier for your mentor to test and grade. 
