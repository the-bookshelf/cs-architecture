/**
 * @file       main.cpp
 * @author     VolodymyrShymanskyy
 * @modified by	Pradeeka Seneviratne
 * @license    This project is released under the MIT License (MIT)
 * @copyright  Copyright (c) 2015 VolodymyrShymanskyy
 * @date       Mar 2015
 * @brief
 */

//#define BLYNK_DEBUG
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

static const char *auth, *serv;
static uint16_t port;

#include <BlynkWidgets.h>
#include <wiringPi.h>

BLYNK_WRITE(V1)
{
    printf("Got a value %s\n", param[0].asStr());

	if(param.asInt() == 1)
		digitalWrite(1, HIGH);
	else
		digitalWrite(1, LOW);

}

void setup()
{
     Blynk.begin(auth, serv, port);
     pinMode(1, OUTPUT);
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
