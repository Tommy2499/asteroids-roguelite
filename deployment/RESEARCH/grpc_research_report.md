# Research Report
## gRPC Java Server, gRPC Web Javascript Client, Envoy Proxy, and Protocol Buffers

### Summary of Work
<!--One paragraph summary of the research being performed-->
I researched into how gRPC worked in Java and Javascript. From this sprouted additional topics to research into, including how to handle Java package dependencies, how to handle Javascript dependencies, the Envoy Proxy, and how to compile proto files into Java/Javascript classes using the protoc compiler. I read many github pages and documentation, followed youtube tutorials, and read documentation on different APIs. During this process I created a Java gRPC server running in a docker container that listens to incoming requests from another Docker container running the Envoy proxy, which in turn listens to Client requests on the Browser. The client is a web server written in Javascript. My work may be found here: https://git.doit.wisc.edu/cdis/cs/courses/cs506/sp2025/team/T_07/research-grpc/-/tree/master?ref_type=heads.

### Motivation
<!--Explain why you felt the need to perform this research-->
My team needed a way for the various components (Front end, Back end, and Server) to communicate between each other. I was familiar with gRPC (google remote procedure call) and protocol buffers, which is a way for a client computer to remotely call a function from a server computer. Loosely, the client sends function arguments in an efficient way and recieves the result back from the Server. I have written a few servers and clients in python, and figured I could learn how to do it in Java and Javascript. The goal was to learn and create a demo of this to share with my team and decide if it is something we want to incorporate into our tech stack. 

### Time Spent
<!--Explain how your time was spent-->
- 30 minutes writing a demo.proto file, reading Protoc/Java documentation and installing protoc tools for Java
- 30 minutes figuring out dependencies for Java grpc libraries, reading java grpc github pages, downloading required jar files from the Maven repository, and compiling the demo.proto file into separate java classes
- 60 minutes writing the Java service implementation, Java server, a Dockerfile for the server, and following parts of tutorials
- 30 minutes writing a client in Java for testing
- 30 minutes installing protoc tools for Javascript, reading Web grpc documentation and compiling demo.proto into javascript classes
- 30 minutes troubleshooting npm and npx webpack to deal with javascript dependencies
- 60 minutes following github tutorial to create a javascript web client that sends grpc requests
- 120 minutes reading into envoy proxy in a docker container, docker networks, configuring envoy, writing a docker compose to get the proxy container and server container on same network and communicating. 

### Results
<!--Explain what you learned/produced/etc. This section should explain the
important things you learned so that it can serve as an easy reference for yourself
and others who could benefit from reviewing this topic. Include your sources as
footnotes. Make sure you include the footnotes where appropriate e.g [^1]-->
I started by writing a demo.proto file with a simple function (known as a service):
```proto
syntax = "proto3";

option java_multiple_files = true;
option java_outer_classname = "demo";

message AddTwoRequest {
    int32 x = 1;
}

message AddTwoResponse {
    int32 y = 1;
}

service AddTwoService {
    rpc AddTwo(AddTwoRequest) returns (AddTwoResponse);
}
```

The syntax option lets the compiler know what proto language I'm using. A message is defined by using the message keyword followed by the name. In it you can include as many arguments as you want. Note that the equals sign is not an assignment statement. It numbers the arguments sequentially, in case different versions of your message have a different number of arguments. These are language independent; a correspondence between different types can be found here[^1].

The service keyword defines the service, which will take a AddTwoRequest to the server and return an AddTwoResponse to the client. By convention, clients send requests and servers sent reponses. The messages and services define an API between the front end and back end. Messages are stored in a wire format, serialized/encoded efficiently in a binary format which makes it fast.

Next I looked at language specific options here[^2] and found tools to compile demo.proto into Java code here[^3]. Here I found a link to the Maven repository for Jar dependencies and downloaded them. They can be found here: https://git.doit.wisc.edu/cdis/cs/courses/cs506/sp2025/team/T_07/research-grpc/-/tree/master/server_demo/lib?ref_type=heads. 
I then installed the following for compiling:
- protobuf: the protoc gen compiler
- protoc-gen-grpc-java: a protoc java extension

Using a command similar to
```bash
protoc --java_out=. --grpc-java_out=. demo.proto
```
I obtained several Java classes:
- AddTwoRequest.java: A class for turning java types into requests
- AddTwoResponse.java: A class for turning responses into java types
- AddTwoServiceGrpc.java: A class containing the base code for a grpc service
- Two more java interfaces that aren't that important

Referencing here[^4] and a youtube tutorial[^5], I implemented the AddTwoServiceImpl.java and DemoServer.java for my specific services.

```java
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

public class AddTwoServiceImpl extends AddTwoServiceGrpc.AddTwoServiceImplBase {

    @Override
    public void addTwo(AddTwoRequest request, StreamObserver<AddTwoResponse> responseObserver) {
        int x = request.getX();
        AddTwoResponse response = AddTwoResponse.newBuilder().setY(x + 2).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
```
This first file is the implementation of my service I defined in the demo.proto file. I extend the ImplBase class and implement the addTwo service function. It takes a request and gets the x value I defined in the demo.proto file. Then it creates a response object, setting the y value to y = x + 2. The StreamObserver class can be found here[^7] for the remaining method calls. The response value is then sent back to the client.

```java
import java.io.IOException;
import io.grpc.Server;
import io.grpc.ServerBuilder;

public class DemoServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(5000).addService(new AddTwoServiceImpl()).build();
        server.start();
        System.out.println("Server started on port 5000");
        server.awaitTermination();
    }    
}
```
Next I implemented the server that handles requests on port 5000 and add the new service I implemented above. I also used[^7] as a reference for creating a Java client, which can be found in the repository. Once the server is coded, many services and messages may be added in the same manner.

Next I wrote a docker file to host my server in Ubuntu with portforwarding on port 5000. I also made a hacky start script since I can't run java code in the container that I compiled locally (newer open-jdk). 

I next learned about grpc web[^8] for Javascript web clients. I installed 
- protoc-gen-js: protoc javascript extension
- protoc-gen-grpc-web: protoc plugin

and using a command
```bash
protoc -I=. demo.proto --js_out=import_style=commonjs:. --grpc-web_out=import_style=commonjs,mode=grpcwebtext:.
```
I compiled the demo.proto (without the java related options) to obtain
- demo_pb.js: class for working with AddTwoRequest and AddTwoResponse objects
- demo_grpc_web_pb.js: class for making service calls

Using this tutorial[^9] (be warned this is outdated) I created the code for a javascript client.
```javascript
const { AddTwoRequest, AddTwoResponse } = require("./demo_pb.js");
const { AddTwoServiceClient } = require("./demo_grpc_web_pb.js");

var client = new AddTwoServiceClient("http://localhost:5050");
var request = new AddTwoRequest();
request.setX(5);

client.addTwo(request, {}, (err, response) => {
  if (err) {
    console.error(err);
  } else {
    console.log(response.getY());
  }
});
```
This client program creates a request object, setting the x value to 5 and sends it to the Java gRPC server. The response is then sent to the console log in the browser. Notice that the Java Server is listening on port 5000, while the Client is set up on localhost:5050. This is because we cannot directly send the request to the server. We must send it through Envoy proxy at port 5050, which will then forward it to the server at port 5000.

I used the commands detailed in[^9] to handle my dependencies. I first created a list of needed packages in package.json:
```json
{
  "devDependencies": {
    "@grpc/grpc-js": "~1.8.21",
    "@grpc/proto-loader": "~0.5.4",
    "async": "~1.5.2",
    "google-protobuf": "~3.21.4",
    "grpc-web": "~1.5.0",
    "lodash": "~4.17.0",
    "webpack": "~5.98.0",
    "webpack-cli": "~5.1.1"
  }
}
```
Then I used these commands to compile it in the same directory. Note that the above packages differ from the tutorial.
```bash
npm install
npx webpack ./client.js
```
This created a nodes directory with the needed dependencies, and a dist/main.js program which is what the web server will run. To set up the web server I used the same index.html as in the tutorial:
```html
<!doctype html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <title>gRPC-Web Example</title>
    <script src="./dist/main.js"></script>
  </head>
  <body>
    <p>Open up the developer console and see the logs for the output.</p>
  </body>
</html>
```
Again, it runs the generated ./dist/main.js file.

Lastly, to set up the Envoy proxy I edited the tutorial envoy.yaml file, while referencing the Envoy docs[^10]. The details do not matter too much. All it does is listens on port 5050 where the client sends requests, and forwards it to the docker container containing the server on port 5000. Envoy proxy itself will be run in a docker container.
```yaml
static_resources:
  listeners:
    - name: listener_0
      address:
        # We listen for requests on port 5050
        socket_address: { address: 0.0.0.0, port_value: 5050 }
      filter_chains:
        - filters:
          - name: envoy.filters.network.http_connection_manager
            typed_config:
              "@type": type.googleapis.com/envoy.extensions.filters.network.http_connection_manager.v3.HttpConnectionManager
              codec_type: auto
              stat_prefix: ingress_http
              route_config:
                name: local_route
                virtual_hosts:
                  - name: local_service
                    domains: ["*"]
                    routes:
                      - match: { prefix: "/" }
                        route:
                          cluster: demo_service
                          max_stream_duration:
                            grpc_timeout_header_max: 0s
                    cors:
                      allow_origin_string_match:
                        - prefix: "*"
                      allow_methods: GET, PUT, DELETE, POST, OPTIONS
                      allow_headers: keep-alive,user-agent,cache-control,content-type,content-transfer-encoding,custom-header-1,x-accept-content-transfer-encoding,x-accept-response-streaming,x-user-agent,x-grpc-web,grpc-timeout
                      max_age: "1728000"
                      expose_headers: custom-header-1,grpc-status,grpc-message
              http_filters:
                - name: envoy.filters.http.grpc_web
                  typed_config:
                    "@type": type.googleapis.com/envoy.extensions.filters.http.grpc_web.v3.GrpcWeb
                - name: envoy.filters.http.cors
                  typed_config:
                    "@type": type.googleapis.com/envoy.extensions.filters.http.cors.v3.Cors
                - name: envoy.filters.http.router
                  typed_config:
                    "@type": type.googleapis.com/envoy.extensions.filters.http.router.v3.Router
  clusters:
    - name: demo_service
      connect_timeout: 0.25s
      type: logical_dns
      # HTTP/2 support
      typed_extension_protocol_options:
        envoy.extensions.upstreams.http.v3.HttpProtocolOptions:
          "@type": type.googleapis.com/envoy.extensions.upstreams.http.v3.HttpProtocolOptions
          explicit_http_config:
            http2_protocol_options: {}
      lb_policy: round_robin
      load_assignment:
        cluster_name: demo_service 
        endpoints:
          - lb_endpoints:
            - endpoint:
                address:
                  socket_address:
                    # We forward requests to the demo_server on port 5000. 
                    # It's important that the Proxy container and server container are on the same network
                    address: "demo_server"
                    port_value: 5000
```
To set up the server and proxy smoothly, I created a docker-compose.yml file:
```yml
services:
  demo_server:
    build:
      context: .
      dockerfile: Dockerfile
    ports:
      # gRPC server listens on port 5000 
      - "5000:5000"
    networks:
      - demo_network
    
  # Here we start our Envoy Proxy container to listen on port 5050
  envoy_proxy:
    image: envoyproxy/envoy:v1.22.0
    ports:
      - "5050:5050"
    volumes:
      - ./envoy.yaml:/etc/envoy/envoy.yaml
    # Make sure demo_server starts first
    depends_on:
      - demo_server
    networks:
      - demo_network
      
# Having docker containers on the same network means they can communicate by name
networks:
  demo_network:
```
What this does is first builds the demo_server image and runs it in a container on port 5000. Then it uses the envoy proxy image to launch a container that listens to client requests on port 5050. It forwards the requests to the sever. To achieve this, I make sure the sever container and proxy container are on the same docker network called demo_network. This was a fix I found in a forum to help the docker containers communicate. 

Running the setup should only require docker. Navigating to the grpc_demo/server_demo folder we can run
```Bash
docker compose up --build -d
```
which will build the pull/build the images if need be and then run the sever and proxy containers. Navigating to the grpc_demo/client_demo folder and running
```Bash
python3 -m http.server 8000
```
should start the client web server. We can see it by opening a browser and typing in localhost:8000. Recall that the client program has hardcoded sending a request of AddTwo(5). This is sent to port 5050 to the proxy container. The proxy container forwards it to the server container at port 5000. The server sends a response of 7 through the proxy and back to the client. It can be seen by right clicking the browser, inspecting it, and navigating to the console. 

In conclusion, now that the server and proxy are coded, adding new services involves editing the demo.proto file, compiling it to the language of choice, and working with the compiled Java and Javascript classes.
### Sources
<!--list your sources and link them to a footnote with the source url-->
- Language Guide (proto3)[^1]
- Protocol Buffer Basics: Java[^2]
- gRPC-Java[^3]
- gRPC in Java[^4]
- gRPC Service in Java[^5]
- io.grpc.stub Docs[^6]
- gRPC Client in Java[^7]
- gRPC Web[^8]
- gRPC Web Hello World[^9]
- Envoy Proxy Docs[^10]
[^1]: https://protobuf.dev/programming-guides/proto3/
[^2]: https://protobuf.dev/getting-started/javatutorial/
[^3]: https://github.com/grpc/grpc-java
[^4]: https://grpc.io/docs/languages/java/basics/
[^5]: https://www.youtube.com/watch?v=2hjIn3kKXuo
[^6]: https://grpc.github.io/grpc-java/javadoc/io/grpc/stub/StreamObserver.html
[^7]: https://www.youtube.com/watch?v=eUu29SrGYTA
[^8]: https://github.com/grpc/grpc-web
[^9]: https://github.com/grpc/grpc-web/tree/master/net/grpc/gateway/examples/helloworld
[^10]: https://www.envoyproxy.io/docs/envoy/v1.33.0/configuration/configuration
