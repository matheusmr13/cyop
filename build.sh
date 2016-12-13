#!/bin/sh
cd $TRAVIS_BUILD_DIR/backend
sbt ++$TRAVIS_SCALA_VERSION package
