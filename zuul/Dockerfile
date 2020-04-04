FROM openjdk:11-jdk-slim
COPY target/zuulweb.jar /opt/zuulweb.jar
RUN useradd -ms /bin/sh mpaxele2050 \
    && echo "root:Docker!" | chpasswd \
    && chown -R mpaxele2050 /opt/
USER mpaxele2050
ENTRYPOINT ["java",  "-Xms1024m", "-Xmx4096m", "-XX:MetaspaceSize=2048m", \
            "-XX:+UseG1GC", "-XX:ParallelGCThreads=100", "-XX:ConcGCThreads=30", \
            "-XX:InitiatingHeapOccupancyPercent=70", "-XX:MaxGCPauseMillis=10", \
            "-XX:InitiatingHeapOccupancyPercent=35", "-XX:G1HeapRegionSize=16M", \
            "-XX:MinMetaspaceFreeRatio=50", "-XX:MaxMetaspaceFreeRatio=80",  \
            "-XX:+UnlockExperimentalVMOptions","-jar","/opt/zuulweb.jar"]
EXPOSE 8079
