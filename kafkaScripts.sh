kafka-topics --bootstrap-server 'localhost:9092' --topic greetings-topic --create --replication-factor 1 --partitions 1
# or start a interactive bash on the broker container

kafka-topics --list --zookeeper localhost:2181

kafka-topics --zookeeper localhost:2181 --delete --topic greetings-topic

kafka-consumer-groups --bootstrap-server localhost:9092 --new-consumer --group greetings-client --describe
