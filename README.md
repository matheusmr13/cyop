# CYOP - Create Your Own API
[![Build Status](https://travis-ci.org/matheusmr13/cyop.svg?branch=master)](https://travis-ci.org/matheusmr13/cyop)
[![Coverage Status](https://coveralls.io/repos/github/matheusmr13/cyop/badge.svg?branch=master)](https://coveralls.io/github/matheusmr13/cyop?branch=master)

## Requirements

- Maven ^3.1.1
- Java 1.7

## Running local

> git clone git@github.com:matheusmr13/cyop.git

> cd cyop

> mvn clean install && mvn appengine:devserver

[http://localhost:8080/](http://localhost:8080/)

## Demo

[http://saasapi-1469987800705.appspot.com/](http://saasapi-1469987800705.appspot.com/)

## Using

1. Add your endpoint (eg: users);
  * Now you have your api url:
    * GET http://saasapi-1469987800705.appspot.com/api/users
    * GET http://saasapi-1469987800705.appspot.com/api/users/{id}
    * POST http://saasapi-1469987800705.appspot.com/api/users
    * PUT http://saasapi-1469987800705.appspot.com/api/users/{id}
    * DELETE http://saasapi-1469987800705.appspot.com/api/users/{id}
2. Add columns to your endpoint (eg: name, username, email);
3. Choose your columns type (eg: Integer, String, BigDecimal);
4. Add data to your endpoint (eg: {name: 'Matheus', username: 'matheusmr13'});
  * You can use the created api links or insert it at the platform.
5. That's all folks (:

## Future
