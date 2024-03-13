#!/bin/bash

mvn package

docker build -t user-service-tomcat:1_0_0 .