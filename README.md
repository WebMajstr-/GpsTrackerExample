# GpsTrackerExample

This sample project allows for reproduction of the issue that I have been having:

**With foreground service and battery optimizations disabled, listener to GPS position stops receiving regular data after one hour (after screen goes to sleep).**

You can start and stop the service. After it has been runing for a few hours, you can export the log and analyse it to see the issue. To save you on time, I have added example log from my Pixel 2 phone(good data in the first hour is shortened for readability). See file [issue-example.txt](issue-example.txt)

I am able to reproduce the issue on newer versions of android:

- Pixel 2 (Android 8.1.0)
- Nexus 5 (Android 6.0.1)

BUT, I am not able to reproduce on *older shittier* phone:
Mobistel Cynus E5 (Android 4.4.2)

I would guess that it has something to do with Doze mode on newer devices, but can not find a workaround, although there should be one.

When looking to [issue-example.txt](issue-example.txt), the problem is visible in those two lines:
```
10092;2018-06-04T23:00:31.88Z;onGpsStatusChanged;2
10093;2018-06-04T23:53:26.02Z;onGpsStatusChanged;1
```
The `2` and `1` in the end of the lines are `GpsStatus.GPS_EVENT_STOPPED` and `GpsStatus.GPS_EVENT_STARTED`, respectively. It's between those two lines that phone goes to sleep.
