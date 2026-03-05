package com.example.previdencia.resource;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.time.Instant;
import java.util.Map;

@Path("/benchmark")
@Produces(MediaType.APPLICATION_JSON)
public class BenchmarkHealthResource {

    @GET
    @Path("/health")
    public Map<String, Object> health() {
        return Map.of(
                "status", "UP",
                "service", "demo-crud",
                "timestamp", Instant.now().toString());
    }

    @GET
    @Path("/perf")
    public Map<String, Object> perf() {
        // Small deterministic CPU work to avoid a zero-cost endpoint during throughput tests.
        long checksum = 0;
        for (int i = 1; i <= 1000; i++) {
            checksum += (long) i * i;
        }
        return Map.of(
                "status", "OK",
                "checksum", checksum);
    }
}
