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
# Postgres
You need Docker running. Then run:

    docker run --rm -P -p 127.0.0.1:5432:5432 -e POSTGRES_PASSWORD="3sf2reRer" --name postgres -d postgres
 
And here is how you connect:   
    
    psql postgresql://postgres:3sf2reRer@localhost:5432/postgres

Or with Doobie:
    
      protected val xa: Aux[IO, Unit] = Transactor.fromDriverManager[IO](
        "org.postgresql.Driver",
        "jdbc:postgresql://localhost/postgres",
        "postgres",
        "3sf2reRer"
      )
      
# Kafka
* Installation according to [Confluent Open Source Quick Start (Local)](https://docs.confluent.io/current/quickstart/cos-quickstart.html)
* Go to Kafka-bin directory:

        cd <path-to-kafka>
        # example
        cd /Users/mpa/dev/kafka/confluent-5.0.0
        
* Start Kafka

      <path-to-confluent>/bin/confluent start
* Create the Producer Topic:

      ./bin/kafka-topics --create --zookeeper localhost:2181 \
       --replication-factor 1 --partitions 1 --topic petstore-msg-topic
* To check the incoming messages do these steps:
  * Start KSQL: 
  
        LOG_DIR=./ksql_logs ./bin/ksql
  * Create a Stream: 
  
        CREATE STREAM petstorePages (route VARCHAR) \
                      WITH (KAFKA_TOPIC='petstore-msg-topic', VALUE_FORMAT='DELIMITED');

  * Create a Query:
        
        SELECT ROWKEY, route, ROWTIME FROM petstorePages LIMIT 10;`