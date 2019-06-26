using System;

namespace TownCrier
{
    public class Crier : MarshalByRefObject
    {
        public delegate void AnnounceHandler(string topic);
        public event AnnounceHandler AnnounceTopic;

        public string Announce2(string topic)
        {
            AnnounceTopic?.Invoke(topic);
            return $"Hear ye! Hear ye! On this datetime {DateTime.Now} I hearby announce that {topic}";
        }

        public AnnouncementLog Announce(AnnouncementTemplate template)
        {
            AnnounceTopic?.Invoke(template.Topic);
            return new AnnouncementLog($"{template.Greeting}! I hearby announce that {template.Topic}");
        }
    }
}
