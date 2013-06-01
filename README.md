![Pheme](https://raw.github.com/blopker/Pheme/master/logo.png)

#### Web based admin interface for distributed systems.

View Pheme's [project page](http://blopker.github.com/Pheme/).

This project requires *Play 2.1*.

Download the latest server build from: <http://pheme.in/downloads/pheme-latest.zip>

The [Abstract](https://github.com/blopker/Pheme/wiki/Abstract) is now on the wiki.

## Usage
### With binary
To run the Pheme server with the binary build provided above, unzip the archive and run:

    cd pheme-latest
    chmod +x start
    ./start

### From source
Pheme is a [Play Framework](http://www.playframework.org) web app. To run Pheme make sure your `play` command is working. If not, download the Play Framework. Next run:

	play start

in the Pheme root folder. You can also start a development server with:

	play run

To run the test RMI client you'll need Ant. Then run:

	ant runClient

## Screenshots

Main dashboard
![Main](https://github.com/blopker/Pheme/wiki/img/dashboard.png)
Job Page
![Job](https://github.com/blopker/Pheme/wiki/img/job.png)
Computer list
![Computers](https://github.com/blopker/Pheme/wiki/img/computers.png)
Logs page
![logs](https://github.com/blopker/Pheme/wiki/img/logs.png)

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
