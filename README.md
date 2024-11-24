# PimpMyAndroidPIP

> Purpose of this project was to allow PIP on my AndroidTV (announcing Domotic messages, pictures, camera captures, ...)

> Inspired of https://github.com/rogro82/PiPup, (original one was unable to display non https pictures/streams)

> This is finally my first Android app built under Android Studio Koala

> So totally rewrite in java (not swichted yet to Kotlin)

# Installation

Install the APK file and accept overlay permission.

# Launch the app:

> This will automatically try to start the service and listening on local ip.

# Trigger message:

> Make a simple curl command with this payload:

curl -X POST -d @/data.json 'http://<your_android_device_ip>:8080/'

```json
{
 "popupType"     : "SimpleMessage",
 "cardWidth"     : 400,
 "message"       : "This is a simple message",
 "timeToDisplay" : 15000
}
```

> Image

```json
{
 "popupType"     : "SimpleMessage",
 "cardWidth"     : 400,
 "imageUrl"      : "http://192.168.2.17:1000/tmp/macaque-ouanderou.jpg",
 "imageWidth"    : 300,
 "message"       : "This is a simple message",
 "timeToDisplay" : 15000
}
```

> Stream rtc
```json
{
 "popupType"     : "SimpleMessage",
 "cardWidth"     : 400,
 "message"       : "A car enter in the alley",
 "rtcUrl"        : "http://192.168.2.17:1984/stream.html?src=mezz&mode=webrtc",
 "timeToDisplay" : 15000
}
```

You can mix *message*, *image* and *rtcUrl*.

Others optionals sizing parameters:

```json
{
 "rtcHeight"     : 400,
 "rtcScale"      : 50,
 "imageWidth"    : 200,
 "imageHeight"   : 200
}
```

