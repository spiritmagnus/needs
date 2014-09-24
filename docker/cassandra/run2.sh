docker run -it --name cassandra-2 --link cassandra-1:cassandra-1 -e "CASSANDRA_SEEDS=cassandra-1" -e "CASSANDRA_TOKEN=4611686018427387904" --rm -p 9161:9160 cassandra
