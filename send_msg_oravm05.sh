#!/bin/bash

# This script sends a message to a specified ActiveMQ server using the 'send' command.

if [ "$#" -ne 3 ]; then
    echo "Usage: $0 <message_count> <message_file> <destination>"
    exit 1
fi

/home/nino/Documents/code/tools/a-cli/a --artemis-core --broker tcp://oravm05:61616 \
    --user artemis --pass artemis \
    --count $1 --put @$2 $3
