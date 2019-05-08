# BeeLine

<p align="center">
  <img src="https://user-images.githubusercontent.com/28008631/56102168-1002d700-5ef9-11e9-8831-8c88059d85d4.png" width="256" title="Github Logo">
</p>

Made by: Isaiah, Nick, Nancy, Bronte (Team B)


Beeline is a service that seeks to create ridesharing groups to lower the cost of going from point A to point B(ee). The most recent iteration of our Android application enables users to join trips to do just this, as well as make their own trips to buzz around.

## Log-in Info:
Username: person@place.com
Password: password123

Username: bwen4@gmail.com
Password: hellohello

## First Sprint Objectives:
- [x] Create **Account Activity** (Sprint 1 version) 
  - Accounts should have: Name and Bio

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

## Second Sprint Objectives
- [x] take in current location of user to update beelines shown in Find Beelines [will display only Beelines in same zip code as user]

- [x] Dedicated Log In Screen: authenticates password by exact match

- [x] Dedicated Create User Screen: Creates new user with name, email, password

- [x] Account activity v2: personal User Profile editable

- [x] **Beeline Details v2**: Implemented adaptor to display list of users that share a Beeline; toggle button for user to leave/join a Beeline from Beeline Details activity; anyone can edit "Additional details" text for each trip's Beeline Details activity

- [x] **Flower Button Shortcut**: User can click Recycler item flower button to quickly add or leave a Beeline

- [x] Notifications: based on user preferences noted in Settings, push notifications implemented for every 30 min, 1 hour, and/or 1 day before a given trip
 
 ## General State of the App To-Date
 Our current version is able to log in a user, create a new user, and show a user's personal beelines when they enter our app. Furthermore, if a user is not part of a Beeline, they can navigate to the find beeline page to search through the current offerings. If they don't find one that meets their needs, they can create a brand new beeline from point A to point B(ee). All of these actions are updated via the Firebase Database to keep user actions attached to their profile. In a user's personal profile, a user can update information about themselves and include the payment method they use for ridesharing. Users can also view other users' profiles by clicking on each participant in a trip's Beeline Details page. Some whimsical features we have included include our custom loading screen featuring a loading bee while a user is taken from activity to activity. An extra feature we included was a search feature on My Beelines and Find Beelines that enables users to search for Beelines from and/or to a location. Future improvements to our app could include a more comprehensive onboarding process for first-time users and ability to find beelines from a custom zip code.
 
 ## Meeting Notes:
 3/30 Meeting with Simon. Made sure our t3 assignment was sufficient for submission.
 
 4/2 Scrum Meeting. Started on Different Activities in Beeline and split up activities.
 
 
 4/7 Work day. Group work in Malone UGrad lab.
 
 4/12 Work day. Group work in Malone UGrad lab.
 
 4/14 Meeting with Simon. Discussed accomplished objectives and goals prior to Sprint 1 submission.
 
 4/19 Work day. Group work in Malone UGrad lab.
 
 4/21 Meeting with Simon. Discussed completed goals for Sprint 1 and new objectives for Sprint 2.
 
 4/22 Work day. Group work in Malone UGrad lab. Spoke to Anuraag in office hours about a DB error.
 
 4/26 Group work from 4-7 pm in Brody.
 
 4/28 Meeting with Simon. Established goals to be completed by final project submission and split tasks.

 4/30 Finishing Sprint II before presentation

 5/3 Finishing up Sprint II for submitting code
