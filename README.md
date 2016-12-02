# SaaS API!
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

## Frameworks

 * Backend:
  * Google App Engine ([reason](https://cloud.google.com/why-google/));
  * Yawp! ([reason](http://yawp.io/) on "feature" part);
  * RESTeasy (Because yawp does not support {PathParams}, used to create a custom api).

 * Frontend:
  * Yawp! (To comunicate with yawp at backend);
  * Materialize (To beautify things a little);
  * JQuery (To make requests easier, but it's easy to replace).

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
