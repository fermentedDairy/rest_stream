package com.exampleorg.fermented.dairy.rest.test.rest_stream;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.StreamingOutput;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import java.io.OutputStream;
import java.io.Writer;
import java.io.InputStream;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;


/**
 *
 */
@Path("/stream")
@ApplicationScoped
@OpenAPIDefinition(info = @Info(title = "Stream endpoint", version = "1.0"))
@Log
public class StreamController {

    @APIResponses(value = {
            @APIResponse(
                    responseCode = "200",
                    description = "Stream File text using Response output stream",
                    content = @Content(
                            mediaType = MediaType.TEXT_PLAIN)
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "No stream found.")
    })
    @GET
    @Produces({MediaType.TEXT_PLAIN})
    @Path("/stream/{flushSize}")
    public Response readFileStream(@PathParam("flushSize") int flushSize) {
        log.info("START::readFileStream");

        StreamingOutput stream = new StreamingOutput() {

            @Override
            public void write(OutputStream out) throws IOException {
                log.info("Starting Stream");

                try (Writer writer = new BufferedWriter(new OutputStreamWriter(out));
                     InputStream loremIS = StreamController.class.getClassLoader().getResourceAsStream("lorem.txt")) {
                    int c = -1;
                    int i = 0;
                    while ((c = loremIS.read()) != -1) {
                        i++;
                        writer.write(c);
                        if (i % flushSize == 0) {
                            writer.flush();
                        }
                    }
                    writer.flush();
                }
            }
        };
        return Response.ok(stream).build();
    }

    @APIResponses(value = {
            @APIResponse(
                    responseCode = "200",
                    description = "Stream numbers using Response output stream",
                    content = @Content(
                            mediaType = MediaType.TEXT_PLAIN)
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "No stream found.")
    })
    @GET
    @Produces({MediaType.TEXT_PLAIN})
    @Path("/stream/numbers/{flushSize}/{max}")
    public Response readNumberStream(@PathParam("flushSize") int flushSize, @PathParam("max") int max) {
        log.info("START::readNumberStream");

        StreamingOutput stream = new StreamingOutput() {

            @Override
            public void write(OutputStream out) throws IOException {
                log.info("Starting Stream");

                try (Writer writer = new BufferedWriter(new OutputStreamWriter(out))) {

                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i <= max; i++) {
                        sb.append(i).append(" ");
                        if (sb.length() % flushSize == 0) {
                            writer.write(sb.toString());
                            writer.flush();
                            sb = new StringBuilder();
                        }
                    }
                    writer.flush();
                }
            }
        };
        return Response.ok(stream).build();
    }

    @SneakyThrows({IOException.class})
    @APIResponses(value = {
            @APIResponse(
                    responseCode = "200",
                    description = "Stream File text returning stream",
                    content = @Content(
                            mediaType = MediaType.TEXT_PLAIN)
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "No string found.")
    })
    @GET
    @Produces({MediaType.TEXT_PLAIN})
    @Path("/string")
    public String readFileString() {
        log.info("Rest Call");
        try (InputStream loremIS = StreamController.class.getClassLoader().getResourceAsStream("lorem.txt")) {
            int c = -1;
            StringBuilder sb = new StringBuilder();
            while ((c = loremIS.read()) != -1) {
                sb.append((char) c);
            }

            return sb.toString();
        }
    }
}
