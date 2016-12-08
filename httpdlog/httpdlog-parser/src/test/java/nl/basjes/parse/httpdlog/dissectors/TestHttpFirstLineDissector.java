/*
 * Apache HTTPD logparsing made easy
 * Copyright (C) 2011-2016 Niels Basjes
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nl.basjes.parse.httpdlog.dissectors;

import nl.basjes.parse.core.nl.basjes.parse.core.test.DissectorTester;
import org.junit.Test;

public class TestHttpFirstLineDissector {
    @Test
    public void testNormal() throws Exception {
        DissectorTester.create()
            .withDissector(new HttpFirstLineDissector())
            .withInput("GET /index.html HTTP/1.1")
            .expect("HTTP.METHOD:method",                     "GET")
            .expect("HTTP.URI:uri",                           "/index.html")
            .expect("HTTP.PROTOCOL:protocol",                 "HTTP")
            .expect("HTTP.PROTOCOL.VERSION:protocol.version", "1.1")
            .check();
    }

    @Test
    public void testChoppedFirstLine() throws Exception {
        DissectorTester.create()
            .withDissector(new HttpFirstLineDissector())
            .withInput("GET /index.html HTT")
            .expect("HTTP.METHOD:method",                     "GET")
            .expect("HTTP.URI:uri",                           "/index.html HTT")
            .expect("HTTP.PROTOCOL:protocol",                 (String)null)
            .expect("HTTP.PROTOCOL.VERSION:protocol.version", (String)null)
            .check();
    }

    @Test
    public void testStrangeCommandVersionControl() throws Exception {
        DissectorTester.create()
            .withDissector(new HttpFirstLineDissector())
            .withInput("VERSION-CONTROL /index.html HTTP/1.1")
            .expect("HTTP.METHOD:method",                     "VERSION-CONTROL")
            .expect("HTTP.URI:uri",                           "/index.html")
            .expect("HTTP.PROTOCOL:protocol",                 "HTTP")
            .expect("HTTP.PROTOCOL.VERSION:protocol.version", "1.1")
            .check();
    }
}