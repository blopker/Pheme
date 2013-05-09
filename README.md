![Pheme](https://raw.github.com/blopker/Pheme/master/logo.png)

#### Web based admin interface for distributed systems.

View Pheme's [project page](http://blopker.github.com/Pheme/).

This project requires *Play 2.1*.

## Abstract

The big data era is upon us. To manage this data we increasingly rely on distributed application frameworks like Hadoop, Storm, and jPregel. While these frameworks are powerful they often use esoteric report and control schemes. Unfortunately, these frameworks are far to busy pushing the  performance envelope to design something better. A simple, drop-in control interface is needed. That's where Pheme comes in. Users need easy access to distributed applications through a simple interface. Pheme is a real-time standalone web application that is easy to use for both application developers and end users. By making big data more accessible to fields out side of computer science Pheme hands the power these frameworks over to the people who need it most.

Pheme uses Java, the Play Framework, Websockets, Require.js with other technologies. This paper explores the design and performance challenges involved with mixing these pieces together to make Pheme a reality.

## Usage
Pheme is a [Play Framework](http://www.playframework.org) web app. To run Pheme make sure your `play` command is working. If not, download the Play Framework. Next run:

	play start

in the Pheme root folder. You can also start a development server with:

	play run

To run the test RMI client you'll need Ant. Then run:

	ant runClient

Finally you can go through all the unittests by running:

	play test

## Screenshots

Job Page

![Logs](https://github.com/blopker/Pheme/blob/master/pheme_logs.PNG?raw=true)

## License

(The MIT License)

Copyright (c) 2013 Bo Lopker;

Permission is hereby granted, free of charge, to any person obtaining
a copy of this software and associated documentation files (the
'Software'), to deal in the Software without restriction, including
without limitation the rights to use, copy, modify, merge, publish,
distribute, sublicense, and/or sell copies of the Software, and to
permit persons to whom the Software is furnished to do so, subject to
the following conditions:

The above copyright notice and this permission notice shall be
included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED 'AS IS', WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
