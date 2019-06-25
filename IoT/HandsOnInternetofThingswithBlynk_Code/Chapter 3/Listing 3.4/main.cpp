//#define BLYNK_DEBUG
#define BLYNK_PRINT stdout
#ifdef RASPBERRY
  #include <BlynkApiWiringPi.h>
#else
  #include <BlynkApiLinux.h>
#endif
#include <BlynkSocket.h>
#include <BlynkOptionsParser.h>
#include <wiringPi.h>
#include <softPwm.h>

static BlynkTransportSocket _blynkTransport;
BlynkSocket Blynk(_blynkTransport);

static const char *auth, *serv;
static uint16_t port;

#include <BlynkWidgets.h>

BLYNK_WRITE(V1)
{
printf("x: %s\n", param[0].asStr());
printf("y: %s\n", param[1].asStr());

}

void setup()
{
Blynk.begin(auth, serv, port);
}

void loop()
{
Blynk.run();
}


int main(int argc, char* argv[])
{
parse_options(argc, argv, auth, serv, port);

setup();
while(true) {
loop();
    }

    return 0;
}
