## iServe Project

This is project introduce to java application with http server framework under ([moleculer-java](https://moleculer-java.github.io/site/introduction.html)) engine within spring boot framework run on application server

## Packaging

With java and maven installed (comes with [maven](https://maven.apache.org/install.html)), run the following commands into a terminal in the root directory of this project:

    mvn compile package

### Dependencies

This module require base package `smartserve`, `beanspi`, `beanserve` and `beancore`.


## How To Run
The application using spring boot framework you can custom change setting by `application.properties`	

The project default will run at `http://localhost:8080/`

## Setup
Since this project required database setup before starting you have to create database schema by run sql file under folder `/database/servedb_mysql.sql` this sql snippet file come with MySQL. Example user access existing in `/database/readme.txt`.

## Configuration
After setup database you may change configuration setting to access your database under `/resources` path by `/META-INF/global_config.json`. 

setup global configuration
- with java option
	
		java -Dglobal.config=c:\global_config.json com.fs.serve.App

- can set/export profile environment with GLOBAL_CONFIG=?

	on windows
	
		set GLOBAL_CONFIG=c:\global_config.json (can set via environment variables setting)
	
	on linux
	
		export GLOBAL_CONFIG=/home/user/global_config.json
	
	on docker
	
		environment:
      	- GLOBAL_CONFIG=/app/conf/global_config.json
 
 	(for center_config.json using CENTER_CONFIG)
 
## Example

This project contains examples API that it can invoke by [curl](https://curl.se/download.html):

By default `web.xml` set servlet path as `/service/*` so you need to reference it with that path.

* curl http://localhost:8080/iserve/service/api/fetch/hello 
* curl http://localhost:8080/iserve/service/api/fetch/hello?name=test  (query parameter)
* curl -X POST http://localhost:8080/iserve/service/api/fetch/hello -d name=test  (post parameter)
* curl -X POST -H "Content-Type: application/json" "http://localhost:8080/iserve/service/api/fetch/hello" -d "{\"name\":\"testing\"}"
* curl http://localhost:8080/iserve/service/api/fetch/hi/test (RESTful api with path parameter)
* curl -v http://localhost:8080/iserve/service/api/fetch/error (with http status 500)
* curl http://localhost:8080/iserve/service/api/fetch/time/current (RESTful api return current time milliseconds)

