#!/bin/sh

result_millis=""
for i in $(seq 1 10 32); do
	echo "--- run $i ---"
	res=$(docker run -e ARGS="$i 500 10000 1000000 1920 1080" ivanasen-wator:0.0.1)
	echo "$res"
	result_millis+="$(echo -n "$res" | grep 'millis' | cut -d':' -f2), "
done

echo "$result_millis"
