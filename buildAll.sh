cd daytrader7-account-service
mvn clean package

cd ../daytrader7-frontend-service
mvn clean package

cd ../daytrader7-holding-service
mvn clean package

cd ../daytrader7-order-service
mvn clean package

cd ../daytrader7-quote-service
mvn clean package

NETWORK_NAME=daytrader7
VOLUME_NAME_ACCOUNTS=daytrader-db2data-accounts
VOLUME_NAME_HOLDINGS=daytrader-db2data-holdings
VOLUME_NAME_ORDERS=daytrader-db2data-orders
VOLUME_NAME_QUOTES=daytrader-db2data-quotes

docker network inspect $NETWORK_NAME
if [ $? == 1 ]; then
    echo "Creating network \"$NETWORK_NAME\""
    docker network create $NETWORK_NAME
fi    

docker volume inspect $VOLUME_NAME_ACCOUNTS
if [ $? == 1 ]; then
    echo "Creating volume \"$VOLUME_NAME_ACCOUNTS\""
    docker volume create $VOLUME_NAME_ACCOUNTS
fi    

docker volume inspect $VOLUME_NAME_HOLDINGS
if [ $? == 1 ]; then
    echo "Creating volume \"$VOLUME_NAME_HOLDINGS\""
    docker volume create $VOLUME_NAME_HOLDINGS
fi

docker volume inspect $VOLUME_NAME_ORDERS
if [ $? == 1 ]; then
    echo "Creating volume \"$VOLUME_NAME_ORDERS\""
    docker volume create $VOLUME_NAME_ORDERS
fi

docker volume inspect $VOLUME_NAME_QUOTES
if [ $? == 1 ]; then
    echo "Creating volume \"$VOLUME_NAME_QUOTES\""
    docker volume create $VOLUME_NAME_QUOTES
fi
