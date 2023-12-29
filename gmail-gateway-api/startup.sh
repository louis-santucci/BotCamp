#!/bin/sh
if [ "$SLEEP_ENABLED" == "true" ]
  then
    echo "SLEEP_ENABLED: true";
    echo "Sleeping 10 seconds...";
    sleep 10;
  else
    echo "SLEEP_ENABLED: false"
    echo "Running application..."
fi

java -Dspring.profiles.active=docker,prod org.springframework.boot.loader.JarLauncher