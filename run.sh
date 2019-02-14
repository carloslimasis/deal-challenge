#!/usr/bin/env bash

mvn clean install && docker-compose build && docker-compose up