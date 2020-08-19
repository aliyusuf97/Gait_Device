#include "BMX055.h"
float pitch_angle;
float roll_angle;
float yaw_angle;

void i2c_write(uint8_t addr_to_write, uint8_t register_to_write, uint8_t data_to_write) {
  Wire.beginTransmission(addr_to_write);
  Wire.write(register_to_write);
  Wire.write(data_to_write);
  Wire.endTransmission();
}

void i2c_read(uint8_t device_addr, uint8_t register_addr, int number_of_bytes, uint8_t *buf) {
  Wire.beginTransmission(device_addr);
  Wire.write(register_addr);
  Wire.endTransmission();

  //Wire.beginTransmission(DEVICE_ADDR);
  Wire.requestFrom(device_addr, number_of_bytes);

  int i = 0;
  while (Wire.available()) {
    buf[i] = Wire.read();
    i++;
  }
}

void init_imu() {
  Wire.begin(25 , 33);

  // Select PMU_Range register
  // Range = +/- 2g
  i2c_write(BMX055_ACCEL_ADDRESS0, BMX055_ACCEL_PMU_RANGE, 0x03);

  // Select PMU_BW register
  // Bandwidth = 7.81 Hz
  i2c_write(BMX055_ACCEL_ADDRESS0, BMX055_ACCEL_PMU_BW, 0x08);

  // Select PMU_LPW register
  // Normal mode, Sleep duration = 0.5ms
  i2c_write(BMX055_ACCEL_ADDRESS0, BMX055_ACCEL_PMU_LPW, 0x00);

  // Select Range register
  // Full scale = +/- 250 degree/s
  i2c_write(BMX055_GYRO_ADDRESS0, BMX055_GYRO_RANGE, 0x03);

  // Select Bandwidth register
  // ODR = 100 Hz
  i2c_write(BMX055_GYRO_ADDRESS0, BMX055_GYRO_BW, 0x07);

  // Select LPM1 register
  // Normal mode, Sleep duration = 2ms
  i2c_write(BMX055_GYRO_ADDRESS0, BMX055_GYRO_LPM1, 0x00);

  // Select Mag register
  // Soft reset
  i2c_write(BMX055_MAG_ADDRESS0, BMX055_MAG_POWER, 0x83);

  // Select Mag register
  // Normal Mode, ODR = 10 Hz
  i2c_write(BMX055_MAG_ADDRESS0, BMX055_MAG_MODE, 0x00);

  // Select Mag register
  // X, Y, Z-Axis enabled
  i2c_write(BMX055_MAG_ADDRESS0, BMX055_MAG_AXIS_ENABLE, 0x84);

  // Select Mag register
  // No. of Repetitions for X-Y Axis = 9
  i2c_write(BMX055_MAG_ADDRESS0, BMX055_MAG_REPXY, 0x04);

  // Select Mag register
  // No. of Repetitions for Z-Axis = 15
  i2c_write(BMX055_MAG_ADDRESS0, BMX055_MAG_REPZ, 0x0F);
}

void convert_to_vector(uint8_t *raw_data, float *converted_data) {
  converted_data[0] = (float)((int16_t)(((uint16_t)raw_data[1] << 8) | (uint16_t)raw_data[0]));
  converted_data[1] = (float)((int16_t)(((uint16_t)raw_data[3] << 8) | (uint16_t)raw_data[2]));
  converted_data[2] = (float)((int16_t)(((uint16_t)raw_data[5] << 8) | (uint16_t)raw_data[4]));
}

void read_data(uint8_t address_to_read_from, uint8_t register_to_read_from, float *data_to_read, boolean prepare) {
  uint8_t raw_data[6];
  i2c_read(address_to_read_from, register_to_read_from, 6, raw_data);
  if (prepare) {
    raw_data[0] &= 0xf0;
    raw_data[2] &= 0xf0;
    raw_data[4] &= 0xf0;
  }
  convert_to_vector(raw_data, data_to_read);
}

void read_accel(float *accel_data) {
  read_data(BMX055_ACCEL_ADDRESS0, BMX055_ACCEL_X_LSB , accel_data, true);
}

void read_gyro(float *gyro_data) {
  read_data(BMX055_GYRO_ADDRESS0, BMX055_GYRO_X_LSB , gyro_data, false);
}

void read_mag(int16_t *mag_data) {
  uint8_t raw_data[6];
  i2c_read(BMX055_MAG_ADDRESS0, BMX055_MAG_X_LSB, 6, raw_data);
  mag_data[0] = (int16_t) (((int16_t)raw_data[1] << 8) | raw_data[0]) >> 3;
  mag_data[1] = (int16_t) (((int16_t)raw_data[3] << 8) | raw_data[2]) >> 3;
  mag_data[2] = (int16_t) (((int16_t)raw_data[5] << 8) | raw_data[4]) >> 1;
}

void print_values(float *values_to_print, char* title){
  Serial.print(title);
  Serial.print("\t");
  Serial.print(values_to_print[0]);
  Serial.print("\t");
  Serial.print(values_to_print[1]);
  Serial.print("\t");
  Serial.print(values_to_print[2]);
  Serial.print("\t");
}

void print_sensor_values(){
    Serial.print("Angles: ");
  Serial.print("\t");
  Serial.print(pitch_angle);
  Serial.print("\t");
  Serial.print(roll_angle);
  Serial.print("\t");
  Serial.print(yaw_angle);
  Serial.println("\t");
}

void convert_raw_data(float *raw_data, float sensitivity) {
  raw_data[0] = raw_data[0] * sensitivity;
  raw_data[1] = raw_data[1] * sensitivity;
  raw_data[2] = raw_data[2] * sensitivity;
}

void complementary_filter(float *accel, float *gyro){
  float pitch_acc = atan2(accel[1], sqrt( pow(accel[2],2))) * (180/M_PI);
  float roll_acc = atan2(accel[0], sqrt( pow(accel[2],2))) * (180/M_PI); 
  float gx = gyro[0] * (180/PI);
  pitch_angle = 0.98 * (pitch_angle + gyro[0] * (180/PI) * 0.01) + pitch_acc * 0.02; // complementary filter
  roll_angle = 0.98 * (roll_angle + gyro[1] * (180/PI) * 0.01 * -1) + roll_acc * 0.02; // complementary filter
  yaw_angle += gyro[2] * 0.01 * (180/PI);
}

void read_imu() {
  float accel_data[3];
  float gyro_data[3];
  int16_t mag_data[3];

  float accel_data_unconverted[3];
  float gyro_data_unconverted[3];
  
  float accel_resolution = 2/32768.0;
  float gyro_resolution = (PI / 180.0) * 0.0076;
  
  read_accel(accel_data);
  read_gyro(gyro_data); 

  convert_raw_data(accel_data, accel_resolution);
  convert_raw_data(gyro_data, gyro_resolution);

  complementary_filter(accel_data, gyro_data);
}
