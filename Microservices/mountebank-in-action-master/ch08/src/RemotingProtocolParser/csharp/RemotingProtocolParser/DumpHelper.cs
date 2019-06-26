/*
    (The MIT License)

    Copyright (C) 2012 wsky (wskyhx at gmail.com) and other contributors

    Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Runtime.Remoting.Messaging;
using System.Text;

namespace RemotingProtocolParser
{
    public class DumpHelper
    {
        public static void DumpMessage(IMessage msg)
        {
            WriteLine("");
            WriteLine("==== Message Dump ====");
            WriteLine("Type: {0}", msg);
            if (msg is MethodCall)
            {
                var call = msg as MethodCall;
                WriteLine("Uri: {0}", call.Uri);
                WriteLine("---- MethodCall.Args ----");
                DumpArray(call.Args);
            }
            WriteLine("---- Properties ----");
            var enm = msg.Properties.GetEnumerator();
            while (enm.MoveNext())
            {
                WriteLine("{0}: {1}", enm.Key, enm.Value);
                var data = enm.Value as object[];
                if (data != null)
                    DumpArray(data);
            }

            WriteLine("\n\n");
        }
        public static void DumpArray(object[] data)
        {
            WriteLine("\t---- Array ----");
            for (var i = 0; i < data.Length; i++)
                WriteLine("\t{0}: {1}", i, data[i]);
        }
        public static void DumpDictionary(IDictionary<string, object> dict)
        {
            WriteLine("\t---- Dictionary ----");
            foreach (var i in dict)
                WriteLine("\t{0}: {1}", i.Key, i.Value);
        }

        private static void WriteLine(string value)
        {
            Trace.WriteLine(value);
        }
        private static void WriteLine(string format, params object[] args)
        {
            Trace.WriteLine(string.Format(format, args));
        }
    }
}