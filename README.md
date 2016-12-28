# CYOP - Create Your Own API
[![Build Status](https://travis-ci.org/matheusmr13/cyop.svg?branch=master)](https://travis-ci.org/matheusmr13/cyop)
[![Coverage Status](https://coveralls.io/repos/github/matheusmr13/cyop/badge.svg?branch=master)](https://coveralls.io/github/matheusmr13/cyop?branch=master)
[![Code Climate](https://codeclimate.com/github/matheusmr13/cyop/badges/gpa.svg)](https://codeclimate.com/github/matheusmr13/cyop)

**CYOP** is a tool to **create your own api** without **ANY** code using a simple interface.

## Requirements

- Maven ^3.1.1
- Java 1.7

## Steps to Use

1. Create a project on Google App Engine on [Google Console](https://console.developers.google.com/)
1. Clone this repo

       $ git clone git@github.com:matheusmr13/cyop.git

1. Execute:

        $ ./deploy <YOUR_PROJECT_ID>

1. Access \<YOUR_PROJECT_ID\>.appspot.com/import
1. Edit your configurations and click "Create new version!"
1. You are ready to go!

## When should I use CYOP?

1. When you have to test a proof of concept (aka POC) 
  * You can focus your time on the real challenge: test if people likes your solution/idea
1. When you have to do some fast project (maybe a freelance job, or a project without complex backend)
  * In most cases, when your client needs a freelance software (web or mobile), there is no need to complex server side logic developing
1. When you don't know any server side programming language and need to save your data on some place
  * You just need to know JSON and read some CYOP documentation to have your API (in future you will not even need to know JSON to configure your API)

Ps: In any of this ideas, Google App Engine will help you reducing your server costs with [the daily free quotas](https://cloud.google.com/appengine/docs/quotas) and [the super duper cheap environment](https://cloud.google.com/appengine/pricing)

## When shouldn't I use CYOP?

1. When your client needs very specific software and you need complex server side logic developing
  * Maybe you will need to craft some backend that fits what he really needs
1. When you need some feature that CYOP doesn't has 
  * **Or you can contribute and help CYOP grow**
  
## Running local

> git clone git@github.com:matheusmr13/cyop.git

> cd cyop

> mvn clean install && mvn appengine:devserver

[http://localhost:3000/import](http://localhost:3000/import)

## Demo

[http://saasapi-1469987800705.appspot.com/](http://saasapi-1469987800705.appspot.com/)

## Future

