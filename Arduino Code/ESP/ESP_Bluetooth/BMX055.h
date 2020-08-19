//----------------------------------------------------------
//s
//  BMX055

//  I2C Slave Addresses

#define BMX055_GYRO_ADDRESS0        0x68
#define BMX055_GYRO_ADDRESS1        0x69
#define BMX055_GYRO_ID              0x0f

#define BMX055_ACCEL_ADDRESS0       0x18
#define BMX055_ACCEL_ADDRESS1       0x19
#define BMX055_ACCEL_ID             0xfa

#define BMX055_MAG_ADDRESS0         0x10
#define BMX055_MAG_ADDRESS1         0x11
#define BMX055_MAG_ADDRESS2         0x12
#define BMX055_MAG_ADDRESS3         0x13

#define BMX055_MAG_ID               0x32

//  BMX055 Register map

#define BMX055_GYRO_WHO_AM_I        0x00
#define BMX055_GYRO_X_LSB           0x02
#define BMX055_GYRO_X_MSB           0x03
#define BMX055_GYRO_Y_LSB           0x04
#define BMX055_GYRO_Y_MSB           0x05
#define BMX055_GYRO_Z_LSB           0x06
#define BMX055_GYRO_Z_MSB           0x07
#define BMX055_GYRO_INT_STATUS_0    0x09
#define BMX055_GYRO_INT_STATUS_1    0x0a
#define BMX055_GYRO_INT_STATUS_2    0x0b
#define BMX055_GYRO_INT_STATUS_3    0x0c
#define BMX055_GYRO_FIFO_STATUS     0x0e
#define BMX055_GYRO_RANGE           0x0f
#define BMX055_GYRO_BW              0x10
#define BMX055_GYRO_LPM1            0x11
#define BMX055_GYRO_LPM2            0x12
#define BMX055_GYRO_RATE_HBW        0x13
#define BMX055_GYRO_SOFT_RESET      0x14
#define BMX055_GYRO_INT_EN_0        0x15
#define BMX055_GYRO_1A              0x1a
#define BMX055_GYRO_1B              0x1b
#define BMX055_GYRO_SOC             0x31
#define BMX055_GYRO_FOC             0x32
#define BMX055_GYRO_FIFO_CONFIG_0   0x3d
#define BMX055_GYRO_FIFO_CONFIG_1   0x3e
#define BMX055_GYRO_FIFO_DATA       0x3f

#define BMX055_ACCEL_WHO_AM_I       0x00
#define BMX055_ACCEL_X_LSB          0x02
#define BMX055_ACCEL_X_MSB          0x03
#define BMX055_ACCEL_Y_LSB          0x04
#define BMX055_ACCEL_Y_MSB          0x05
#define BMX055_ACCEL_Z_LSB          0x06
#define BMX055_ACCEL_Z_MSB          0x07
#define BMX055_ACCEL_TEMP           0x08
#define BMX055_ACCEL_INT_STATUS_0   0x09
#define BMX055_ACCEL_INT_STATUS_1   0x0a
#define BMX055_ACCEL_INT_STATUS_2   0x0b
#define BMX055_ACCEL_INT_STATUS_3   0x0c
#define BMX055_ACCEL_FIFO_STATUS    0x0e
#define BMX055_ACCEL_PMU_RANGE      0x0f
#define BMX055_ACCEL_PMU_BW         0x10
#define BMX055_ACCEL_PMU_LPW        0x11
#define BMX055_ACCEL_PMU_LOW_POWER  0x12
#define BMX055_ACCEL_HBW            0x13
#define BMX055_ACCEL_SOFT_RESET     0x14
#define BMX055_ACCEL_FIFO_CONFIG_0  0x30
#define BMX055_ACCEL_OFC_CTRL       0x36
#define BMX055_ACCEL_OFC_SETTING    0x37
#define BMX055_ACCEL_FIFO_CONFIG_1  0x3e
#define BMX055_ACCEL_FIFO_DATA      0x3f

#define BMX055_MAG_WHO_AM_I         0x40
#define BMX055_MAG_X_LSB            0x42
#define BMX055_MAG_X_MSB            0x43
#define BMX055_MAG_Y_LSB            0x44
#define BMX055_MAG_Y_MSB            0x45
#define BMX055_MAG_Z_LSB            0x46
#define BMX055_MAG_Z_MSB            0x47
#define BMX055_MAG_RHALL_LSB        0x48
#define BMX055_MAG_RHALL_MSB        0x49
#define BMX055_MAG_INT_STAT         0x4a
#define BMX055_MAG_POWER            0x4b
#define BMX055_MAG_MODE             0x4c
#define BMX055_MAG_INT_ENABLE       0x4d
#define BMX055_MAG_AXIS_ENABLE      0x4e
#define BMX055_MAG_REPXY            0x51
#define BMX055_MAG_REPZ             0x52

#define BMX055_MAG_DIG_X1               0x5D
#define BMX055_MAG_DIG_Y1               0x5E
#define BMX055_MAG_DIG_Z4_LSB           0x62
#define BMX055_MAG_DIG_Z4_MSB           0x63
#define BMX055_MAG_DIG_X2               0x64
#define BMX055_MAG_DIG_Y2               0x65
#define BMX055_MAG_DIG_Z2_LSB           0x68
#define BMX055_MAG_DIG_Z2_MSB           0x69
#define BMX055_MAG_DIG_Z1_LSB           0x6A
#define BMX055_MAG_DIG_Z1_MSB           0x6B
#define BMX055_MAG_DIG_XYZ1_LSB         0x6C
#define BMX055_MAG_DIG_XYZ1_MSB         0x6D
#define BMX055_MAG_DIG_Z3_LSB           0x6E
#define BMX055_MAG_DIG_Z3_MSB           0x6F
#define BMX055_MAG_DIG_XY2              0x70
#define BMX055_MAG_DIG_XY1              0x71

//  Gyro sample rate defines

#define BMX055_GYRO_SAMPLERATE_100_32  0x07
#define BMX055_GYRO_SAMPLERATE_200_64  0x06
#define BMX055_GYRO_SAMPLERATE_100_12  0x05
#define BMX055_GYRO_SAMPLERATE_200_23  0x04
#define BMX055_GYRO_SAMPLERATE_400_47  0x03
#define BMX055_GYRO_SAMPLERATE_1000_116  0x02
#define BMX055_GYRO_SAMPLERATE_2000_230  0x01
#define BMX055_GYRO_SAMPLERATE_2000_523  0x00

//  Gyro FSR defines

#define BMX055_GYRO_FSR_2000        0x00
#define BMX055_GYRO_FSR_1000        0x01
#define BMX055_GYRO_FSR_500         0x02
#define BMX055_GYRO_FSR_250         0x03
#define BMX055_GYRO_FSR_125         0x04

//  Accel sample rate defines

#define BMX055_ACCEL_SAMPLERATE_15  0X00
#define BMX055_ACCEL_SAMPLERATE_31  0X01
#define BMX055_ACCEL_SAMPLERATE_62  0X02
#define BMX055_ACCEL_SAMPLERATE_125  0X03
#define BMX055_ACCEL_SAMPLERATE_250  0X04
#define BMX055_ACCEL_SAMPLERATE_500  0X05
#define BMX055_ACCEL_SAMPLERATE_1000  0X06
#define BMX055_ACCEL_SAMPLERATE_2000  0X07

//  Accel FSR defines

#define BMX055_ACCEL_FSR_2          0x00
#define BMX055_ACCEL_FSR_4          0x01
#define BMX055_ACCEL_FSR_8          0x02
#define BMX055_ACCEL_FSR_16         0x03


//  Mag preset defines

#define BMX055_MAG_LOW_POWER        0x00
#define BMX055_MAG_REGULAR          0x01
#define BMX055_MAG_ENHANCED         0x02
#define BMX055_MAG_HIGH_ACCURACY    0x03
