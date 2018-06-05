# GpsTrackerExample

This sample project allows for reproduction of the issue that I have been having:

**With foreground service and battery optimizations disabled, listener to GPS position stops receiving regular data after one hour (after screen goes to sleep).**

You can start and stop the service. After it has been runing for a few hours, you can export the log and analyse it to see the issue. To save you on time, I have added example log from my Pixel 2 phone(good data in the first hour is shortened for readability). See file [issue-example.txt](issue-example.txt)
