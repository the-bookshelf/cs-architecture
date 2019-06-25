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

intpinData = param.asInt();
	pwmWrite(1, pinData);
}

void setup()
{
Blynk.begin(auth, serv, port);
pinMode(1, PWM_OUTPUT);
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
