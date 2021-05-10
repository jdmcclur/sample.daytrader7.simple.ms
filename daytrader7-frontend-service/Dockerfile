FROM open-liberty:full

# Add build files
COPY --chown=1001:0 daytrader-ee7/target/daytrader-ee7.ear /config/apps/daytrader-ee7.ear
COPY --chown=1001:0 daytrader-ee7/src/main/liberty/config/ /config/
COPY --chown=1001:0 daytrader-ee7/src/main/liberty/resources/db2jcc4.jar /liberty/usr/shared/resources/db2jars/
