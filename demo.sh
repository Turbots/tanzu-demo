#!/usr/bin/env bash

. demo-magic.sh

clear

API_URL=$1
ORG=$2
SPACE=$3

pe "cf login -a $API_URL -o $ORG -s $SPACE --skip-ssl-validation"

pe "cf marketplace"
p "cf create-service p.mysql db-small payments-db"
p "cf create-service p-rabbitmq standard payments-messages"
pe "cf services"