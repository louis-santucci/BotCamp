#!/bin/sh
echo "-------------------[START DATA CLEAN UP]-------------------"
BOTCAMP_API_FOLDER="botcamp-api"
BOTCAMP_API_DB_DATA="${BOTCAMP_API_FOLDER}/db_data"

if [ -f "$BOTCAMP_API_DB_DATA" ]
then
  echo "$BOTCAMP_API_DB_DATA deleted."
  rm -r "$BOTCAMP_API_DB_DATA"
else
  echo "$BOTCAMP_API_DB_DATA not existing, skipping deletion."
fi

BOTCAMP_API_ENV_DATA="${BOTCAMP_API_FOLDER}/src/main/resources/.env"

if [ -f "$BOTCAMP_API_ENV_DATA" ]
then
  echo "$BOTCAMP_API_ENV_DATA deleted."
  rm -r "$BOTCAMP_API_ENV_DATA"
else
  echo "$BOTCAMP_API_ENV_DATA not existing, skipping deletion."
fi

GMAIL_GATEWAY_API_FOLDER="gmail-gateway-api"
GMAIL_GATEWAY_DB_DATA="${GMAIL_GATEWAY_API_FOLDER}/db_data"

if [ -f "$GMAIL_GATEWAY_DB_DATA" ]
then
  echo "$GMAIL_GATEWAY_DB_DATA deleted."
  rm -r "$GMAIL_GATEWAY_DB_DATA"
else
  echo "$GMAIL_GATEWAY_DB_DATA not existing, skipping deletion."
fi

GMAIL_GATEWAY_ENV_DATA="${GMAIL_GATEWAY_API_FOLDER}/src/main/resources/.env"

if [ -f "$GMAIL_GATEWAY_ENV_DATA" ]
then
  echo "$GMAIL_GATEWAY_ENV_DATA deleted."
  rm -r "$GMAIL_GATEWAY_ENV_DATA"
else
  echo "$GMAIL_GATEWAY_ENV_DATA not existing, skipping deletion."
fi
echo "-------------------[END DATA CLEAN UP]-------------------"