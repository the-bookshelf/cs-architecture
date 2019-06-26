using System;

namespace TownCrier
{
    [Serializable]
    public class AnnouncementLog
    {
        public AnnouncementLog(string annoucement)
        {
            When = DateTime.Now;
            Announcement = annoucement;
        }

        public DateTime When { get; }
        public string Announcement { get; }

        public override string ToString()

        {
            return $"({When}): {Announcement}";
        }
    }
}
