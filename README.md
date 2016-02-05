<snippet>
  <content>
#S.O.F.T - Stable Offline File Transfer

STILL IN PRODUCTION, NOT A FINISHED PRODUCT

The problem:

Transferring files between two devices requires some form of connectivity (WiFi, 3G, Bluetooth, etc.). There are many locations in the world where connections are unavailable or unsecure.

Our solution:

A native Android app that allows for file transfer between devices through QR codes. The goal is to enable file transfer of any type, however our initial aims are based on transferring of one type of file and to make sure that the app works correctly for this file type and is designed in such a way that extending it to facilitate other files types should be relatively straightforward.

How it works:

The app will compress a file using a predefined compression algorithm, generate a QR code(s) based on the compressed file, and present the QR code(s) on the screen of the originator. The person who is going to receive the file will open the app and scan the code(s). As the compression algorithm has already been predefined, the receiver can decompress the file onto their own device and store the file locally.

Target Audience:

People who work in remote locations where connectivity is limited, as well as people who need to securely transfer files. This app is a placeholder for when QR code reading technology improves. Right now there are limitations but the app is still useable. As the technology progresses the app will improve with little work required.

Another market we see this app being useful for is as a last resort utility. You cannot always rely on connectivity and our app does not require any connectivity.

Issues:

QR Codes have a hard limit on the data that they can encode. As such it will be required for our app to be able to buffer QR codes to send larger files. We see this as a temporary problem however given that the limitations are based on a the software to read QR codes. As this technology improves, so will the usability of our app. We see this app as a placeholder. This app can be used now, but will get better over time and eventually be a required app.

## Installation
TODO: Describe the installation process

## Usage
TODO: Write usage instructions

## Contributing
1. Fork it!
2. Create your feature branch: `git checkout -b my-new-feature`
3. Commit your changes: `git commit -am 'Add some feature'`
4. Push to the branch: `git push origin my-new-feature`
5. Submit a pull request :D

## Testing

#JVM Unit Tests
TODO

#Instrumented Unit Tests
TODO

#Automated Testing
TODO

## Credits
Jeanine Burke - https://github.com/jeaninevb
Tomas Barry - https://github.com/TomasBarry
Sean McGroarty - https://github.com/McGizzle
Lingfeng Yue - https://github.com/sorocky
Sam Green - https://github.com/greens1
Cormac Keane - https://github.com/CrusaderCrab

## License
TODO: Add license</content>
</snippet>
