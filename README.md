# play-binding-petstore

# Work just started!

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

  `cd kafka-installation/bin)`
* Create the Producer Topic:

  `./kafka-topics --create --zookeeper localhost:2181 \
   --replication-factor 1 --partitions 1 --topic petstore-msg-topic`
* To check the incoming messages do these steps:
  * Start KSQL: 
  
    `./ksql_logs <path-to-confluent>/bin/ksql`
  * Create a Stream: 
  
    `CREATE STREAM petstorePages (username VARCHAR, route VARCHAR) \
                      WITH (KAFKA_TOPIC='petstore-msg-topic', VALUE_FORMAT='DELIMITED');
`
  * Create a Query:
    `SELECT username, route FROM petstorePages LIMIT 10;`