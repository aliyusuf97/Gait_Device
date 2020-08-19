#include<Wire.h>
#include "FSR.h"
#include "IMU.h"
#include "Bluetooth.h"

bool led_state = false;
double elapsed_time;
void setup() {
  // Serial port for debugging purposes
  Serial.begin(9600);
  pinMode(19, OUTPUT);
  init_imu();
  init_bluetooth();
  elapsed_time = millis();
}

void loop() {
  force_sensor_logic();  
  read_imu();
//  print_sensor_values();
  send_data(yaw_angle, pitch_angle, roll_angle, step_counter, contact_time, contact_force);
  delay(10);
  if(millis() - elapsed_time > 1000) {
     toggle_led();
     elapsed_time = millis();
  }
}

void toggle_led(){
  led_state = !led_state;
  digitalWrite(19, led_state);
}
