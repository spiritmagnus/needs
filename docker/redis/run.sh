docker run -it --name redis -v /mnt/home/bucht/dev/redis:/data --rm -p 6379:6379 redis redis-server --appendonly yes
