#!/bin/sh

ab -p input.json -c 10000 -t 75 -T "application/json" http://localhost:8080/payment-attempts/
