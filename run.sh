#!/bin/bash
clear
echo "Building the game ...."
./mvnw &> /dev/null
echo "Starting the game ...."
java -jar target/game.kaiser-0.0.1-SNAPSHOT.jar
