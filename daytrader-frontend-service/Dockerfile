FROM open-liberty:full

# Add build files
COPY --chown=1001:0 target/*.war /config/apps/
COPY --chown=1001:0 src/main/liberty/config/ /config/
