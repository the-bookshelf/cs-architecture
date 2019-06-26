/*
    (The MIT License)

    Copyright (C) 2012 wsky (wskyhx at gmail.com) and other contributors

    Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Runtime.Serialization.Formatters;
using System.Runtime.Serialization.Formatters.Soap;
using System.Text;

namespace RemotingProtocolParser
{
    /// <summary>.Net Remoting use SoapFormatter for MessageSink
    /// </summary>
    [Obsolete("Not support SOAP formatter")]
    public class SoapFormatterHelper
    {
        private static readonly SoapFormatter FormatterInstance = new SoapFormatter();

        public static byte[] SerializeObject(object value)
        {
            if (value == null)
                return null;

            using (var stream = new MemoryStream())
            {
                FormatterInstance.Serialize(stream, value);
                return stream.ToArray();
            }
        }

        public static object DeserializeObject(byte[] data)
        {
            if (data == null)
                return null;

            using (var stream = new MemoryStream(data))
                return FormatterInstance.Deserialize(stream);
        }
    }
}
