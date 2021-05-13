FROM open-liberty:full

# Add build files
COPY --chown=1001:0 target/*.war /config/apps/
COPY --chown=1001:0 src/main/liberty/config/ /config/
COPY --chown=1001:0 target/liberty/wlp/usr/shared/resources/DerbyLibs/derby-10.14.2.0.jar /opt/ol/wlp/usr/shared/resources/DerbyLibs/derby-10.14.2.0.jar
COPY --chown=1001:0 resources/data /opt/ol/wlp/usr/shared/resources/data
