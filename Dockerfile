FROM alpine:latest

# Install required packages
RUN apk add --no-cache curl unzip openjdk11

# Download the interactsh-client ZIP file, extract it, and make it executable
WORKDIR /usr/local/bin
RUN curl -LO https://github.com/projectdiscovery/interactsh/releases/download/v1.1.9/interactsh-client_1.1.9_linux_386.zip && \
    unzip interactsh-client_1.1.9_linux_386.zip && \
    rm interactsh-client_1.1.9_linux_386.zip && \
    chmod +x interactsh-client

# Add your Java project files to the image
WORKDIR /usr/src/app
COPY . .

# Define the default command to run your Java application
CMD ["java", "-jar", "target/demo-1.0-SNAPSHOT.jar"]

# Optionally, set the interactsh-client binary as an entry point
# ENTRYPOINT ["/usr/local/bin/interactsh"]
