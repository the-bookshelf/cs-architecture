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

const int btnPin = 4;

WidgetLED  led1(V1);

BlynkTimer timer;

bool btnState = false;
void buttonLedWidget()
{
        bool isPressed = (digitalRead(btnPin) == LOW);

        if(isPressed != btnState) {
                if(isPressed) {
led1.on();
                } else {
                led1.off();
                }
                btnState = isPressed;
        }
}


void setup()
{
    pinMode(btnPin, INPUT);

        timer.setInterval(500L, buttonLedWidget);
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
