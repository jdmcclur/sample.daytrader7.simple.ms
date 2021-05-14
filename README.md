# sample.daytrader7.simple.ms
Attempt at MicroServices version of daytrader7

# Prereqs
    git
    Maven
    JDK
    Docker/docker-compose
    
## Download
```
git clone https://github.com/jdmcclur/sample.daytrader7.simple.ms
cd sample.daytrader7.simple.ms
```
    
## Build    
```
./buildAll.sh
```

## Run
Derby DB (pre-built and included in services)
```
docker-compose up --build
```

DB2 - you will need to create and populate the db tables after startup
```
docker-compose -f docker-compose-db2.yaml up --build
```




# Differences from sample.daytrader7
1. Based on sample.daytrader7.simple (https://github.com/jdmcclur/sample.daytrader7.simple)
    - Only has EJB mode, no Direct (JDBC) mode.
    - Only has JSP/Servlet pages, no JSFs.
3. No JMS 
4. Moved to EE8 (instead of EE7) features.
5. Applications are wars instead of an ear.
6. Uses MicroProfile 4.0 to talk to other MicroServices
7. Most importantly, App is broken up into 5 services,
    - Front End
    - Accounts
    - Holdings
    - Orders
    - Quotes
