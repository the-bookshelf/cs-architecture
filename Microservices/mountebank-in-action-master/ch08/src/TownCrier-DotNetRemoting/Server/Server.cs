using System;
using System.Runtime.Remoting;
using System.Runtime.Remoting.Channels;
using System.Runtime.Remoting.Channels.Tcp;
using TownCrier;

namespace Server
{
    public class Server
    {
        public static void Main(string[] args)
        {
            var crier = new Crier();
            crier.AnnounceTopic += (topic) =>
            {
                Console.WriteLine("Client announcing: " + topic);
            };

            var port = int.Parse(args[0]);
            var channel = new TcpChannel(port);
            ChannelServices.RegisterChannel(channel);
            RemotingServices.Marshal(crier, "TownCrierService");

            Console.WriteLine("Listening on port " + port);
            Console.WriteLine("Press any key to end...");
            Console.ReadLine();
        }
    }
}
