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

// This function will be called every time Light Sensor Widget
// in Blynk app writes values to the Virtual Pin V1
BLYNK_WRITE(V1)
{
//light value
  int lx = param.asInt();

// print
printf("light level: %i lx\n", lx);

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
