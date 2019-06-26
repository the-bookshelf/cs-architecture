using System;
using System.IO;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using RemotingProtocolParser.TCP;

namespace RemotingProtocolParserTest
{
    [TestClass]
    public class BaseProtocolTest
    {
        [TestMethod]
        public void ReadWriteContentLength()
        {
            this.ReadWriteContentLength(10);
            this.ReadWriteContentLength(100);
            this.ReadWriteContentLength(1000);
            this.ReadWriteContentLength(100000);
            this.ReadWriteContentLength(10000000);
        }

        private void ReadWriteContentLength(int length)
        {
            using (Stream stream = new MemoryStream())
            {
                var handle = new TcpProtocolHandle(stream);
                handle.WriteContentLength(length);
                stream.Seek(0, SeekOrigin.Begin);
                var bytes = new byte[4];
                stream.Read(bytes, 0, 4);
                stream.Seek(0, SeekOrigin.Begin);
                foreach (var b in bytes)
                    Console.Write(b + " ");
                Console.WriteLine();
                Assert.AreEqual(length, handle.ReadContentLength());
            }
        }
    }
}