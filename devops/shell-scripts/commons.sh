#!/bin/bash

#symbols
CHECK_SYMBOL="\033[0;32m\xE2\x9C\x94\033[0m"
WRENCH_SYMBOL="\xE2\x9C\xA8"

#common paths
BUSINESS_RELATIVE_PATH="application/backend/business"
INFRASTRUCTURE_RELATIVE_PATH="application/backend/infrastructure"

#log files
LOG_FILE="output.log"

#timestamps
function get_timestamp {
    date +"%l:%M:%S:%3N%p"
}