using System;
using System.IO;
using System.Linq;
using System.Reflection;
using System.Runtime.Remoting.Messaging;
using System.Text;
using NUnit.Framework;
using Client;
using MbDotNet;
using MbDotNet.Enums;
using MbDotNet.Models.Predicates;
using MbDotNet.Models.Predicates.Fields;
using RemotingProtocolParser;
using RemotingProtocolParser.TCP;
using TownCrier;

namespace ClientTest
{
    [TestFixture]
    public class TownCrierGatewayTest
    {
        private readonly MountebankClient mb = new MountebankClient();

        [TearDown]
        public void TearDown()
        {
            mb.DeleteAllImposters();
        }

        [Test]
        public void ClientShouldAddSuccessMessage()
        {
            var stubResult = new AnnouncementLog("TEST");
            CreateImposter(3000, "Announce", stubResult);
            var gateway = new TownCrierGateway(3000);

            var result = gateway.AnnounceToServer("ignore", "ignore");

            Assert.That(result, Is.EqualTo($"Call Success!\n{stubResult}"));
        }

        //[Test]
        public void SpitOutLargeRequest()
        {
            File.WriteAllText("response.txt", RandomString(100000));
        }

        private static readonly Random random = new Random();
        public static string RandomString(int length)
        {
            const string chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
            return new string(Enumerable.Repeat(chars, length)
                .Select(s => s[random.Next(s.Length)]).ToArray());
        }

        private void CreateImposter(int port, string methodName, AnnouncementLog result)
        {
            var imposter = mb.CreateTcpImposter(port, "", TcpMode.Binary);
            imposter.AddStub()
                .On(ContainsMethodName(methodName))
                .ReturnsData(Serialize(result));
            mb.Submit(imposter);
        }

        private ContainsPredicate<TcpPredicateFields> ContainsMethodName(string methodName)
        {
            var predicateFields = new TcpPredicateFields { Data = ToBase64(methodName) };
            return new ContainsPredicate<TcpPredicateFields>(predicateFields);
        }

        private string ToBase64(string plaintext)
        {
            return Convert.ToBase64String(Encoding.UTF8.GetBytes(plaintext));
        }

        public string Serialize(Object obj)
        {
            var messageRequest = new MethodCall(new[] {
                new Header(MessageHeader.Uri, "tcp://localhost:3000/TownCrier"),
                new Header(MessageHeader.MethodName, "Announce"),
                new Header(MessageHeader.MethodSignature, SignatureFor("Announce")),
                new Header(MessageHeader.TypeName, typeof(Crier).AssemblyQualifiedName),
                new Header(MessageHeader.Args, ArgsFor("Announce"))
            });
            var responseMessage = new MethodResponse(new[]
            {
                new Header(MessageHeader.Return, obj)
            }, messageRequest);

            var responseStream = BinaryFormatterHelper.SerializeObject(responseMessage);
            using (var stream = new MemoryStream())
            {
                var handle = new TcpProtocolHandle(stream);
                handle.WritePreamble();
                handle.WriteMajorVersion();
                handle.WriteMinorVersion();
                handle.WriteOperation(TcpOperations.Reply);
                handle.WriteContentDelimiter(TcpContentDelimiter.ContentLength);
                handle.WriteContentLength(responseStream.Length);
                handle.WriteTransportHeaders(null);
                handle.WriteContent(responseStream);
                return Convert.ToBase64String(stream.ToArray());
            }
        }

        private Type[] SignatureFor(string methodName)
        {
            return typeof(Crier)
                .GetMethod(methodName)
                .GetParameters()
                .Select(p => p.ParameterType)
                .ToArray();
        }

        private Object[] ArgsFor(string methodName)
        {
            var length = SignatureFor(methodName).Length;
            return Enumerable.Repeat(new Object(), length).ToArray();
        }
    }
}

