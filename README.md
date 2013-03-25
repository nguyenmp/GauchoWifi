GauchoWifi
==========

An Android application that stores credentials and automatically authenticates users into UCSB Wireless Web.

The reason this application was created was because HTTP captive portals are unable to handle HTTPS requests.  When an application or the user tries to access a website that is only available on HTTPS, the request gets dropped and the reason us usally "No Data Connecctivity".  This was very buggy on my device as many applications would simply crash when they tried to sync or download a file.

This application solves this issue by authenticating the user every time it detects the user is behind the UCSB captive portal.

How To Use
==========

Install the application from the Google Play Store.  If the application does not start automatically, launch it.  You will be prompted to enter your UCSBNetID and your password. Enter them and press save.  You can optionally press check to run the authentication checker.  It is sometimes hit-and-miss so some false negatives and false positivies occur, but it is accurate for the most part.  When I tell you you are ready, just close the app and wait.

When I authenticate you into UCSB Wireless Web, you'll get a little notification!  Nothing more.

How To Contribute
=================

There are a few ways to contribute to this project.  If you are interested, please send me bug reports.  You can formally issue them on this website by pressing "Issues" at the top of this page and creating a new one.  You can also report them using the native Android reporting tool.

If you want to contribute to the source code of this project, you'll first need to set up ActionBarSherlock and HoloEverywhere.  I use these two Android libraries because I prefer the asthetics of Holo.  It also simplifies UI bugs and glitches that occur due to different specifications and implementations between skins (TouchWiz, Blur).

Make sure both projects are installed and compiling before you add GauchoWifi.  Additionally, make sure you are not exporting their andorid-support-v4.jar library, as this will cause errors in the build process.  Clone this git repo and add it to your workspace.  Link to the other two libraries and you should be working and operational  now.  Have fun!
