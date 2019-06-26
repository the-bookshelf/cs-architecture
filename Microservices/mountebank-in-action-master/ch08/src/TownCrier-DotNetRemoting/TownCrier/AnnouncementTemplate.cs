using System;

namespace TownCrier
{
    [Serializable]
    public class AnnouncementTemplate
    {
        public AnnouncementTemplate(string greeting, string topic)
        {
            Greeting = greeting;
            Topic = topic;
        }

        public string Greeting { get; }
        public string Topic { get; }
    }
}
