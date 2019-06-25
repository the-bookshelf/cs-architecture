#define BLYNK_PRINT stdout

#ifdef RASPBERRY
 #include <BlynkApiWiringPi.h>
#else
 #include <BlynkApiLinux.h>
#endif
#include <BlynkSocket.h>
#include <BlynkOptionsParser.h>

static BlynkTransportSocket _blynkTransport;
BlynkSocket Blynk(_blynkTransport);
#include <BlynkWidgets.h>

#include <wiringPi.h>
#include <softPwm.h>

BLYNK_WRITE(V1)
{
//FOREWORD LEFT SWING TURN
  int pinValue = param.asInt(); // assigning incoming value from pin V1 (left button)to a variable

  // STOP left motor
softPwmWrite (6, 100); //PWMA on GPIO. 6
digitalWrite (4, LOW); //AIN1 on GPIO. 4
digitalWrite (5, LOW); //AIN2 on GPIO. 5

// TURN ON CW right motor
softPwmWrite (29, 100); //PWMB on GPIO. 29
digitalWrite (27, HIGH); //BIN1 on GPIO. 27
digitalWrite (28, LOW); //BIN2 on GPIO. 28

}

BLYNK_WRITE(V2)
{
//FOREWORD RIGHT SWING TURN
  int pinValue = param.asInt(); // assigning incoming value from pin V2 (right button) to a variable

//TURN ON CW left motor
softPwmWrite (6, 100); //PWMA on GPIO. 6
digitalWrite (4, HIGH); //AIN1 on GPIO. 4
digitalWrite (5, LOW); //AIN2 on GPIO. 5

//STOP right motor
softPwmWrite (29, 100); //PWMB on GPIO. 29
digitalWrite (27, LOW); //BIN1 on GPIO. 27
digitalWrite (28, LOW); //BIN2 on GPIO. 28

}

BLYNK_WRITE(V0)
{
//FOREWORD 
  int pinValue = param.asInt(); // assigning incoming value from pin V0 (forward button) to a variable

  // TURN ON CW left motor
softPwmWrite (6, 100); //PWMA on GPIO. 6
digitalWrite (4, HIGH); //AIN1 on GPIO. 4
digitalWrite (5, LOW); //AIN2 on GPIO. 5

// TURN ON CW right motor
softPwmWrite (29, 100); //PWMB on GPIO. 29
digitalWrite (27, HIGH); //BIN1 on GPIO. 27
digitalWrite (28, LOW); //BIN2 on GPIO. 28

}

BLYNK_WRITE(V3)
{
//BACKWARD
  int pinValue = param.asInt(); // assigning incoming value from pin V3 (backward button) to a variable

  // TURN ON CW left motor
softPwmWrite (6, 100); //PWMA on GPIO. 6
digitalWrite (4, LOW); //AIN1 on GPIO. 4
digitalWrite (5, HIGH); //AIN2 on GPIO. 5

// TURN ON CW right motor
softPwmWrite (29, 100); //PWMB on GPIO. 29
digitalWrite (27, LOW); //BIN1 on GPIO. 27
digitalWrite (28, HIGH); //BIN2 on GPIO. 28

}


void setup()
{
softPwmCreate (6, 100, 100);
softPwmCreate (29, 100, 100);

}

void loop()
{
Blynk.run();
}

int main(int argc, char* argv[])
{
  const char *auth, *serv;
  uint16_t port;
  parse_options(argc, argv, auth, serv, port);

  Blynk.begin(auth, serv, port);

setup();
  while(true) {
loop();
  }

  return 0;
}
