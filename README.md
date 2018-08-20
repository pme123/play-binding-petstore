# play-binding-petstore

A Single Page Application SPA that demos how are things done with this stack:
* Server: Scala / Play
* Shared: Scala
* Client: ScalaJS / Binding.scala

Next to that I use it for experimenting with stuff I need for work, like Kafka.

# Work is in progress and there are lots of loose ends!

# Web Client
* Run the client:

  `sbt run`
* There are 3 Users (password anything):
  * demoAdmin
  * demoCustomer
  * demoManager  

# Kafka
* Installation according to [Confluent Open Source Quick Start (Local)](https://docs.confluent.io/current/quickstart/cos-quickstart.html)
* Go to Kafka-bin directory:

      cd kafka-installation/bin
* Create the Producer Topic:

      ./kafka-topics --create --zookeeper localhost:2181 \
       --replication-factor 1 --partitions 1 --topic petstore-msg-topic
* To check the incoming messages do these steps:
  * Start KSQL: 
  
        ./ksql_logs <path-to-confluent>/bin/ksql
  * Create a Stream: 
  
        CREATE STREAM petstorePages (route VARCHAR, time VARCHAR) \
                      WITH (KAFKA_TOPIC='petstore-msg-topic', VALUE_FORMAT='DELIMITED');

  * Create a Query:
        
        SELECT ROWKEY, route, ROWTIME FROM petstorePages LIMIT 10;`