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

#include <BlynkWidgets.h>

BlynkTimer timer;
unsigned int potValue;

void repeatMe()
{
        potValue = analogRead(1);
        Blynk.virtualWrite(V1, potValue);
}

void setup()
{
pinMode(1,INPUT);
        softPwmCreate(1,0, 1023);
timer.setInterval(1000L, repeatMe);
}

void loop()
{
    Blynk.run();
timer.run();
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
