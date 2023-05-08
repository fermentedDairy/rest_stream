# rest_stream (POC)

## Introduction

POC project to get data streamed from a JAX-RS REST service.

There are 3 endpoints:
- **/data/stream/stream/numbers/{flushSize}/{max}**: stream a **max** number of integers, flushing every **flushSize** numbers
- **/data/stream/stream/{flushSize}**: stream characters read from the *lorem.txt* resource file, flushing every **flushSize** characters
- **/data/stream/string**: read characters read from the *lorem.txt* resource file into a single string, for comparison and benchmarking purposes

## Result

It works, however, there are probably better ways to do it (that aren't REST services) and I have no idea what the client code would look like if the streamed data was JSON objects or XML documents.

Of course OpenAPI is available:
- /openapi/ui/
- /openapi/

## Build and Run
```
mvn clean liberty:dev
```