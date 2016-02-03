QR File Transfer

STILL IN PRODUCTION, NOT A FINISHED PRODUCT

The problem:

Transferring files between two devices requires some form of connectivity (WiFi, 3G, Bluetooth, etc.). There are many locations in the world where connections are unavailable or unsecure.

The solution:

Create a native Android app that allows for file transfer between devices through QR codes and scanners. The hope would be to enable file transfer of any type, however our initial aims will be based on transferring of one type of file and to make sure that the app works correctly for this file type and is designed in such a way that extending it to facilitate other files types should be relatively straightforward.

How it works:

The idea is to create an app that will compress a file using a predefined compression algorithm, generate a QR code based on the compressed file, and present this QR code on the screen of the transferer. The person who is going to receive the file will open the app and scan the code. As the compression algorithm has already been predefined, the receiver can decompress the file onto their own device.

Mock up:

IMAGES TO BE PUT HERE


Target Audience:

People who work in remote locations where connectivity is limited, as well as people who need to securely transfer files. This app is a placeholder for when QR code reading technology improves. Right now there are limitations but the app is still useable. As the technology progresses the app will improve with little work required.

Another market we see this app being useful for is as a last resort utility. You cannot always rely on connectivity and our app does not require any connectivity.

Issues:

QR Codes have a hard limit on the data that they can encode. As such it will be required for our app to be able to buffer QR codes to send larger files. We see this as a temporary problem however given that the limitations are based on a the software to read QR codes. As this technology improves, so will the usability of our app. We see this app as a placeholder. This app can be used now, but will get better over time and eventually be a required app.

Goals:

Publish a working app to the Google Play Store to enable file transfer through QR codes between Android enabled devices and implement support for at least one file type.

Technologies needed:

Everyone will need to familiarise themselves with Android app development (Java, XML, etc.) and understand how the ZXing library for Android works.

Everyone will also need to familiarise themselves with compression algorithms (are different algorithms better for different file types) as well as the limitations of data storage in QR codes and the variety of types of QR codes.

This whole document in a QR Code:

IMAGE TO BE PUT HERE
