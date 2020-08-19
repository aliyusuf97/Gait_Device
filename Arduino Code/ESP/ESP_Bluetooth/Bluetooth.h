#include "BluetoothSerial.h"

#if !defined(CONFIG_BT_ENABLED) || !defined(CONFIG_BLUEDROID_ENABLED)
#error Bluetooth is not enabled! Please run `make menuconfig` to and enable it
#endif

BluetoothSerial SerialBT;
void init_bluetooth(){
  SerialBT.begin("Left_Device");
}

void send_data(float yaw, float pitch, float roll, uint16_t step_counter, float contact_time, float contact_force){
  String yaw_string = String(yaw); 
  String pitch_string = String(pitch); 
  String roll_string = String(roll); 
  String step_counter_string = String(step_counter);
  String contact_time_string = String(contact_time);
  String contact_force_string = String(contact_force);
  //Android is struggling with keeping the first and last digit so I made the first and last digit "33" and ignored those 
  //The number 33 holds no significance
  String concatenate_variables = String("33," + yaw_string + "," + pitch_string + "," + roll_string + "," + step_counter_string + "," + contact_time_string + "," + contact_force_string + ",33");
  SerialBT.println(concatenate_variables);
}
