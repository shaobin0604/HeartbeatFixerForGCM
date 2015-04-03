
[![Build Status](https://travis-ci.org/shaobin0604/HeartbeatFixerForGCM.svg)](https://travis-ci.org/shaobin0604/HeartbeatFixerForGCM)

## Heartbeat Fixer for GCM

Have you ever experience **Push Notification Delay**, missing something important. Here is the tool for fixing GCM heartbeat interval issue.

### The Root Cause

The **root** cause of this issue can be found at [Push notifications delayed, Heartbeat Interval not reliable](https://productforums.google.com/forum/#!msg/nexus/fslYqYrULto/lU2D3Qe1mugJ)

### How Heartbeat Fixer for GCM resolve this issue

It sends an heartbeat every x minutes, where you can choose the interval. Setting it to 5 minutes will keep alive the GCM connection used for push notifications.

### Build 

  ./gradlew assembleDebug

### Screenshots

<img src="snapshot/device-2015-03-24-231244.png" width="300px"/>
<img src="snapshot/device-2015-03-24-231306.png" width="300px"/>

