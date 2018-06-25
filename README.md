# play-binding-petstore
[![Build Status](https://travis-ci.org/pme123/play-binding-petstore.svg?branch=master)](https://travis-ci.org/pme123/play-binding-petstore)
[![Coverage Status](https://coveralls.io/repos/github/pme123/play-binding-petstore/badge.svg?branch=master)](https://coveralls.io/github/pme123/play-binding-petstore?branch=master)
[![](https://jitpack.io/v/pme123/play-binding-petstore.svg)](https://jitpack.io/#pme123/play-binding-petstore)
[![Heroku](http://heroku-badge.herokuapp.com/?app=tranquil-reef-73468)](https://tranquil-reef-73468.herokuapp.com)

An implementation of a web fullstack with Scala/Play on the server and ScalaJS/Binding.scala on the client.

Check the documentation: **[pme123.github.io/play-binding-petstore/](https://pme123.github.io/play-binding-petstore/)**

## Documentation

Run Demo app locally:
`sbt run -Dconfig.resource=demo.conf`

Run Jekyll locally for working on the docs:

`cd PROJECT_DIR/docs` 
`bundle exec jekyll serve --config _config.yml,_config.dev.yml`

Create CHANGELOG:

Adjust version: `.github_changelog_generator`

`github_changelog_generator -u pme123 -p play-binding-petstore -t TOKEN`

Check here for the TOKEN: https://github.com/settings/tokens
