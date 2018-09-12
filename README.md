# Live demo for Kotlin in the Backend @ Kotlin Community Summit 2018

This is a demo project with Kotlin, Spring Webflux and Coroutines, made for Kotlin Summit 2018. It contains three implementations of the same hypothetical service:

* A traditional blocking impl (Spring MVC): backend-mvc
* A reactive impl (Spring Webflux + Reactor): backend-reactive
* A Coroutines based impl (Spring Webflux + Coroutines): backend-coroutines

The service is intended to send online payments top a payment Gateway for a given merchant if it's approved by an external Antifraud system.

To run any of the services, firstly run start.sh. It will start a Node.js application to simulate the Gateway and Antifraud and will start a PostgreSQL in a docker container.

The "load-test" folder contains a simple Apache Bench based load test, used fot comparting the implementations' performance.
