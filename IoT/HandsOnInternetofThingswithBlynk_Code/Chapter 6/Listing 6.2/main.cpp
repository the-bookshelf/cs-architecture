/* Comment this out to disable prints and save space */
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

#include <math.h>

// This function will be called every time Accelerometer Widget
// in Blynk app writes values to the Virtual Pin V1
BLYNK_WRITE(V1)
{
//acceleration force applied to axis x
  int x = param[0].asFloat();
  //acceleration force applied to axis y
  int y = param[1].asFloat();
  //acceleration force applied to axis y
  int z = param[2].asFloat();

double magnitude2D = sqrt((x^2)+(y^2));
double magnitude3D = sqrt((x^2)+(y^2)+(z^2));

// process received valuedouble
printf("2D Magnitude: %f\n", magnitude2D);
printf("3D Magnitude: %f\n", magnitude3D);

}

void setup()
{
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
