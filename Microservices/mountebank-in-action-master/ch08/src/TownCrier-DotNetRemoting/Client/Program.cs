using System;
using System.IO;

namespace Client
{
    public class Program
    {
        public static void Main(string[] args)
        {
            var port = int.Parse(args[0]);
            var gateway = new TownCrierGateway(port);

            // Use a large amount of text by default to force more than one packet
            var defaultText = File.ReadAllText("..\\response.txt");
            var greeting = defaultText;
            if (args.Length > 1)
            {
                greeting = args[1];
            }
            var topic = defaultText;
            if (args.Length > 2)
            {
                topic = args[2];
            }

            Console.WriteLine(gateway.AnnounceToServer(greeting, topic));
        }
    }
}
