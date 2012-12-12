# Pheme: jPregel's Voice
### Karl Lopker
UCSB CMPSC 290B

*Fall 2012*

## Abstract
[jPregel](http://charlesmunger.github.com/jpregel-aws/)[1] is a Java based implementation of [Google's Pregel](http://doi.acm.org/10.1145/1807167.1807184)[2] system for distributed graph computation. The Pregel paper also describes a web front end for it. Currently jPregel does not come with such a front end. The goal of this project is to create an extend-able, easy to use front end for jPregel.

## Presentation
<iframe style="margin:0 80px;" src="http://prezi.com/embed/bjw269ahjmh0/?bgcolor=ffffff&amp;lock_to_path=1&amp;autoplay=no&amp;autohide_ctrls=0" width="550" height="400" frameBorder="0"></iframe>

## Introduction
Pheme is a web-based (HTTP) front end interface for jPregel. Pheme, who's name comes from the Greek goddess of gossip, is designed to facilitate bidirectional communication between jPregel and its users. jPregel will communicate with Pheme through RMI-based logging mechanisms. The user will communicate to Pheme through system control widgets vis Websockets. Pheme is designed to be expandable, each jPregel component will register with the Pheme API and the component's controls will be rendered to the user dynamically. Adding another control page will not require a change in Pheme's source.

Currently only the logging page is implemented.

## Functional Requirements
- API for logging to Pheme
	- Logs stored in a database
	- Handle logs from many sources
- Display logs to user in browser
	- All back logs sent on first connect
	- Logs then sent in real time over Websockets
	- Log information: level (INFO, DEBUG), source, time, and message

## Performance Requirements
- User actions must either be relatively instantaneous (<200ms) or asynchronous as to not freeze Pheme's interface
- jPregle to Pheme communication should only add negligible overhead to the system in both processing time and memory usage

## Key Technical Issues
- Designing Pheme's jPregel side API. Essentially a callback-based remote logging system
	- API should be as non-blocking as possible as to not slow down jPregel
- Creating a extend-able architecture
	-	Pheme should be communication agnostic, using adapters to talk to log sources
- Giving the user the impression of real-time communication through WebSockets
- Configuring deployment in conjunction with jPregel
	- Pheme should be easy to integrate with jPregel's deployment scripts


## Architecture
The Pheme architecture comprises of three components; front end, back end and the API.

### Front end
The front end is what a Pheme user sees in their browser. All front end code is run in the browser. This code gets it's data from the back end through either standard HTTP or via Websockets. Besides Websockets, the front end uses several other technologies; jQuery, Twitter Bootstrap, and HTML5 Boilerplate.

Websockets provide a bidirectional communication layer between the browser and the server. When a user visits the log page the browser initiates a persistent Websocket connection with the server. This connection allows the sever to send users logs in real time. Logs are sent as JSON objects.

![Front end](https://www.lucidchart.com/publicSegments/view/50c060a6-4b78-47f5-b15a-080d0a58a127/image.png)

jQuery is used throughout the front end to simplify JavaScript. Although it has some negative performance implications, jQuery excels at hiding browser quirks inherit in web programming. This library also allows code to be more concise by providing convince functions for common patterns. jQuery also eases the creation and management of Websockets.

Twitter Bootstrap is a CSS framework. Bootstrap provides a CSS grid system based on columns. This gives Pheme a solid and structured feel. It also comes with an impressive JavaScript library for creating flexible usability paradigms, like modal pop-ups or buttons. Bootstrap also helps with browsers' visual quirks by providing a normalizing CSS file.

Lastly, HTML5 Boilerplate is used to give a starting template for HTML. Boilerplate irons out browser quarks while also providing best practices for HTML. Boilerplate is especially useful for mobile devices by providing correct viewport tags.

### Back end
The Back end is the most complicated part of Pheme. It follows the classic MVC separation of responsibilities. The model, view and controller are each comprised of several components.

The view part of the back end comprises of the static files and templates. When a client first connects to Pheme they are sent a compiled HTML page from the template engine. A basic HTML page will include the main and a page specific template. The main template has HTML common to all Pheme views. Changing this file will affect the entire site. The page specific template is unique to the URL a user requests. A template can access the database to get information, like configuration values, and parse it into HTML. For instance the log view template gets the log Websocket server URL dynamically so the user can later connect to it.

![Backend](https://www.lucidchart.com/publicSegments/view/50c0719a-2778-4b8d-a598-5fca0a7a0a70/image.png)

The model component consists of a collection of Java Ebeans and the actual database. Currently the only model is the Log class. When a model's save function is called (either from a change or creation) it will convert the model into a SQL directive and send it to the database. Then the model will alert the controller with the new data.

The controller portion of Pheme contains the HTTP endpoint, Websocket server, adapters and the event bus. The HTTP endpoint controls which views are sent to a client's browser. URLs are parsed and routed there. The Websocket server communicates any real-time (AJAX) data with a client. When a new log comes in the Websocket server runs through its list of connected clients and sends the new data.

Adapters are what jPregel uses to communicate with Pheme. An adapter's job is to listen on whatever communication layer it knows for data. When new data arrives the adapter must convert that data into Pheme models. Currently, the only adapter uses RMI for communication. When an adapter creates a new model an alert is sent to the event bus.

The event bus is the backbone of the controller layer. Any part of Pheme can post objects to it and any other part of Pheme can register for object alerts. For instance the log Websocket server is interested when new logs are made. It registers with the event bus for the Log class. When a log model is saved the event bus is notified and sends that data to all log listeners. This decouples the system so that the models do not have to know who is listening.

### API
Each adapter in the back end will have a API associated with it. The RMI adapter is the only one for now so we will focus on its API.

![API](https://www.lucidchart.com/publicSegments/view/50c814ef-6968-4bb9-8a04-01e80a7c4e7c/image.png)

The RMI API is designed to be non-blocking. It does this by backing any method invocation with a blocking queue. The API spawns its own thread to consume this queue and send the data to Pheme. This way a component can send a log to the API, but not have to wait for the log to actually be sent. If the API loses connection with Pheme it will buffer the logs and try to reconnect every couple of seconds.

The API is also simple to package with an existing application. It is designed to be compiled into a jar file so it can easily be imported into a classpath. Also, it has no dependencies outside of Java. This makes it a small jar file. Furthermore, since the API uses RMI for communication the client application can be on any Internet connected device and still send logs to Pheme.

## Experiments
An experiment was set up to stress test the system and see how many messages per second Pheme could handle. The test was run in a virtual machine on a laptop. The host operating system was running Windows 7 and has the browser on it. The guest system was running Xubuntu 12.10 and was running the Pheme server and test client.

### Methodology
1. Adjust the test client's wait time
1. Start Pheme
2. Connect the browser to Pheme
3. Run the test client for ten seconds
4. Shut down the test client, then the browser, then Pheme

### Results and Analysis
At first the test client was instructed to send as many logs as possible. This was done by calling the API's log method in an infinite while loop. Logs made it to the browser, but they did so in short bursts separated by a second. Pheme could not handle this volume.

The next test was to wait 1 second after each log method invocation. Pheme was able to successfully receive and send these messages at a constant rate without delay.

The wait time was then decreased to 10 milliseconds. Over 10 seconds the browser received 1042 messages which is about 104 log/s. With a 10 ms wait time the rate should be 100 log/s. This indicates that Pheme keep up with load, but due to network lag some packets arrived closer together than at 10 ms intervals.

For the last test a 1 ms wait time was used. Over 14 seconds 3190 logs were received. This is a rate of 227 log/s which, for an expected rate of 1000 log/s, is 22.7% what it should be. This amount of logging clearly over whelmed the system. The bottleneck seems to be in the RMI API communication. The test client's log queue was unable to process quickly enough and its size kept growing. Further investigation into RMI performance is needed.

## Future Work
- More robust connection and error handling
- Performance enhancements
- Control panels with widgets
- Graph visualizations
- Log in system with HTTPS
- Generalize to other distributed systems

## Conclusion
So far Pheme has been a success. In terms of logging it has achieved all the functional requirements while maintaining decent performance. Although Pheme could be faster, it is unlikely a system will output much more than 100 logs per second for sustained periods of time. However, more tests should be done.

Pheme's architecture will allow it to grow. Future enhancements will communicate through the event bus. The adapter system allows for flexibility with different APIs. Also, the front end has room to evolve into its own miniature application.

## References
[1] http://charlesmunger.github.com/jpregel-aws/

[2] Grzegorz Malewicz, Matthew H. Austern, Aart J.C Bik, James C. Dehnert, Ilan Horn, Naty Leiser, and Grzegorz Czajkowski. 2010. Pregel: a system for large-scale graph processing. *In Proceedings of the 2010 ACM SIGMOD International Conference on Management of data* (SIGMOD '10). ACM, New York, NY, USA, 135-146. DOI=10.1145/1807167.1807184 http://doi.acm.org/10.1145/1807167.1807184
