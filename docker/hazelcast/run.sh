docker run -it --rm --name hazelcast1 -p 5701:5701 -p 54327:54327/udp -e "MIN_HEAP=256M" -e "MAX_HEAP=256M" -e "GROUP_NAME=test" -e "GROUP_PASS=test_pass" cacciald/hazelcast
