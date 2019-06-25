define BLYNK_PRINT stdout

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
#include <stdlib.h>

unsigned int uptime;
SimpleTimer timer;

void tweetMe()
{
uptime = millis()/1000;
char msg[] = "My Raspberry Pi is tweeting using @blynk_app and it’s awesome!\nTweeting time since startup: ";
char tm[100];
sprintf(tm,"%d",uptime);
strcat(msg,tm);
strcat(msg," seconds.");
Blynk.tweet(msg);

}

void setup()
{
timer.setInterval((30*1000), tweetMe);
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
