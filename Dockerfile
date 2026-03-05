####
# Multi-stage Dockerfile (native):
# - Build native runner with Maven in a Mandrel builder image
# - Run using Red Hat Quarkus UBI9 micro image
#
# Build:
# docker build -f Dockerfile -t daes-quarkus:native .
#
# Run:
# docker run -i --rm -p 8080:8080 daes-quarkus:native
###
ARG BUILDER_IMAGE=quay.io/quarkus/ubi9-quarkus-mandrel-builder-image:jdk-21
ARG RUNTIME_IMAGE=quay.io/quarkus/ubi9-quarkus-micro-image:2.0

FROM ${BUILDER_IMAGE} AS build
WORKDIR /code

# Mandrel builder image does not ship Maven by default.
RUN microdnf install -y maven && microdnf clean all

# Copy sources and build native executable.
COPY pom.xml ./
COPY src ./src
RUN mvn -B -ntp package -Pnative -DskipTests

FROM ${RUNTIME_IMAGE}
WORKDIR /work/
RUN chown 1001 /work && chmod "g+rwX" /work && chown 1001:root /work
COPY --from=build --chown=1001:root --chmod=0755 /code/target/*-runner /work/application
EXPOSE 8080
USER 1001
ENTRYPOINT ["./application", "-Dquarkus.http.host=0.0.0.0"]
