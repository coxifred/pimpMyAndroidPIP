![PimpMyAndroidPIP](https://github.com/coxifred/pimpMyAndroidPIP/blob/master/docs/logo.png?raw=true&s=100)


# Welcome to PimpMyAndroidPIP

> Purpose of this project was to allow PIP on my AndroidTV (announcing Domotic messages, pictures, camera captures, put in sleep mode ...)

> Inspired of https://github.com/rogro82/PiPup, (original one was unable to display non https pictures/streams)

> This is finally my first Android app built under Android Studio Koala

> So totally rewrite in java (not swichted yet to Kotlin)

# Installation

Install the APK file and accept overlay permission.

# Launch the app:

This will automatically try to start the service and listening on local ip.

 ![PimpMyAndroidPIP](https://github.com/coxifred/pimpMyAndroidPIP/blob/master/docs/capture1.png?raw=true&s=100)

# Trigger message for sleep mode:

> Make a simple *post* curl request on `8080` port with this payload, it will mute the sound & display a black screen (convenient for scheduling sleeping from domotic external systems):

curl -X POST -d @/data.json 'http://<your_android_device_ip>:8080/'

```json
{
 "messageType"   : "sleep",
 "timeToSleep" : 15000
}
```

# Trigger message for overlay popup:

> Make a simple *post* curl request on `8080` port with this payload, it will display a popup in your screen (overlay mode):

curl -X POST -d @/data.json 'http://<your_android_device_ip>:8080/'

```json
{
 "messageType"   : "popup",
 "popupType"     : "SimpleMessage",
 "cardWidth"     : 400,
 "message"       : "This is a simple message",
 "timeToDisplay" : 15000
}
```

> Image

```json
{
 "messageType"   : "popup",
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
 "messageType"   : "popup",
 "popupType"     : "SimpleMessage",
 "cardWidth"     : 400,
 "message"       : "A car enter in the alley",
 "rtcUrl"        : "http://192.168.2.17:1984/stream.html?src=mezz&mode=webrtc",
 "timeToDisplay" : 15000
}
```

![PimpMyAndroidPIP](https://github.com/coxifred/pimpMyAndroidPIP/blob/master/docs/capture2.png?raw=true&s=100)


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

