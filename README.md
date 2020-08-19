# Gait
To download the android app go to https://github.com/aliyusuf97/Gait/tree/master/Android%20Code and download the apk onto your phone. 
Make sure you have Android 9+ (Android Pie or Android Oreo)

To use the app you need two devices to connect to and you cannot pick a random bluetooth device it wont let you. So you need to program two
Esp32s with the following files. The ESP32 with the BNO080 and force sensor should use this. https://github.com/aliyusuf97/Gait/tree/master/Arduino%20Code/ESP/ESP_Bluetooth
and the BNO080 to load fake values should use this https://github.com/aliyusuf97/Gait/tree/master/Arduino%20Code/ESP/Simulated_Data.
The ESP with fake values doesn't need to have any connectsions to anything. And if you want to use the fake data for both devices you must
modify the code and change the 'SerialBT.begin("Right_Device");' from "Right_Device" to "Left_Device"