#docker run -it --name cassandra-1 -e CASSANDRA_TOKEN=0 --rm -p 9160:9160 cassandra
docker run -it --name redis -v /mnt/home/bucht/dev/redis:/data --rm -p 6379:6379 redis redis-server --appendonly yes
