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
printf("Got a value: %s\n", param[0].asStr());
printf("Got a value: %s\n", param[1].asStr());
printf("Got a value: %s\n", param[2].asStr());

softPwmWrite(3, param[0].asInt());
softPwmWrite(21, param[1].asInt());
softPwmWrite(22, param[2].asInt());
}

void setup()
{
Blynk.begin(auth, serv, port);
softPwmCreate(3,0,255);
softPwmCreate(21,0,255);
softPwmCreate(22,0,255);
}

void loop()
{
Blynk.run();
}


intmain(intargc, char* argv[])
{
parse_options(argc, argv, auth, serv, port);

setup();
while(true) {
loop();
    }

    return 0;
}
