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

#include <stdlib.h>
#include <BlynkWidgets.h>

unsigned int uptime;
SimpleTimer timer;
const int btnPin = 23;


void tweetMe()
{

if(digitalRead(btnPin)== LOW)
{
uptime = millis()/1000;
char msg[] = "Twitter button is pressed!\nTweeting time since startup: ";
char tm[100];
sprintf(tm,"%d",uptime);
strcat(msg,tm);
strcat(msg," seconds.");
Blynk.tweet(msg);

}
}

void setup()
{
wiringPiSetup();
pinMode(btnPin, INPUT);
 pullUpDnControl(btnPin, PUD_UP);
timer.setInterval(1000, tweetMe);
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
