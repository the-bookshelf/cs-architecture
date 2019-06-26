using System;
using TownCrier;

namespace Client
{
    public class TownCrierGateway
    {
        private readonly string url;

        public TownCrierGateway(int port)
        {
            url = $"tcp://localhost:{port}/TownCrierService";
        }

        public string AnnounceToServer(string greeting, string topic)
        {
            var template = new AnnouncementTemplate(greeting, topic);
            var crier = (Crier)Activator.GetObject(typeof(Crier), url);
            var response = crier.Announce(template);
            return $"Call Success!\n{response}";
        }
    }
}
