# Pheme
#### Web based admin interface for jPregel

View Pheme's [project page](http://ninj0x.github.com/Pheme/).

### Usage
Pheme is a [Play Framework](http://www.playframework.org) web app. To run Pheme make sure your `play` command is working. If not, download the Play Framework. Next use ant to run:

	ant runServer

in the Pheme root folder. If you don't have ant you can also just run:

	play -Djava.security.policy=policy.txt -DapplyEvolutions.default=true start

To run the test RMI client run:

	ant runClient

### Screenshots

Logs

![Logs](https://github.com/ninj0x/Pheme/blob/master/pheme_logs.PNG?raw=true)
