//Analog Input
#define FSR_1 36

enum step_states {
  stepping,
  not_stepping
};

uint16_t step_counter = 0;
float contact_time = 0;
float contact_force = 0;

void print_contact_details() {
  Serial.print("contact time: ");
  Serial.println(contact_time);
  Serial.print("step counter: ");
  Serial.println(step_counter);
  Serial.print("contact force: ");
  Serial.println(contact_force);
  Serial.println("------------------");
}

float calculate_average_fsr(uint16_t * list_of_fsr_readings, uint16_t number_of_readings){
  float average_reading = 0;
  for(int i=0; i<number_of_readings; i++){
    average_reading += list_of_fsr_readings[i];
  }
  average_reading /= number_of_readings;
  return average_reading;
}

//https://learn.adafruit.com/force-sensitive-resistor-fsr?view=all
void fsr_force_calculation(float fsr_reading) {

  uint16_t fsr_voltage;     // the analog reading converted to voltage
  unsigned long fsr_resistance;  // The voltage converted to resistance, can be very big so make "long"
  unsigned long fsr_conductance;
  long fsr_force;       // Finally, the resistance converted to force
  
  fsr_voltage = map(fsr_reading, 0, 4095, 0, 3300);
  if (fsr_voltage == 0) {
    // No pressure
  } else {
    // The voltage = Vcc * R / (R + FSR) where R = 3.3K and Vcc = 3.3V
    // so FSR = ((Vcc - V) * R) / V        yay math!
    fsr_resistance = 3300 - fsr_voltage;     // fsrVoltage is in millivolts so 5V = 5000mV
    fsr_resistance *= 3300;                // 3.3K resistor
    fsr_resistance /= fsr_voltage;

    fsr_conductance = 1000000;           // we measure in micromhos so
    fsr_conductance /= fsr_resistance;

    // Use the two FSR guide graphs to approximate the force
    if (fsr_conductance <= 1000) {
      fsr_force = fsr_conductance / 80;
      contact_force = fsr_force;
    } else {
      fsr_force = fsr_conductance - 1000;
      fsr_force /= 30;
      contact_force = fsr_force;
    }
  }
}

void force_sensor_logic() {
  uint16_t fsr_reading;
  static uint8_t step_state = not_stepping;
  static uint16_t list_of_fsr[5000];
  static uint16_t number_of_fsr_readings = 0;
  static float initial_time;
  fsr_reading = analogRead(FSR_1);
  
  switch (step_state)
  {
    case not_stepping:
      fsr_reading = analogRead(FSR_1);
      
      //When the FSR reading is larger than 1000 a step has been detected
      if (fsr_reading > 2000 ) {
        //For contact time measurement 
        initial_time = millis(); 
        step_state = stepping;
        step_counter++;
      }
      break;
      
    case stepping:
      fsr_reading = analogRead(FSR_1);
      
      //Gets a list of FSR readings in order to calculate the average fsr reading for force calculation
      list_of_fsr[number_of_fsr_readings] = fsr_reading;
      number_of_fsr_readings++;
      
      //When the FSR reading is less than 1000 user is no longer stepping
      if (fsr_reading < 2000) {
        contact_time = (millis() - initial_time) / 1000;
        step_state = not_stepping;
        //Calculate the average FSR readings then pass it in to the calculation function to get the average force for this step
        fsr_force_calculation(calculate_average_fsr(list_of_fsr,number_of_fsr_readings));
        number_of_fsr_readings = 0;
        print_contact_details();
      }
      break;
  }
}
