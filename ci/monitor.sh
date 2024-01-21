#!/bin/sh
# Monitor memory usage
pidstat --human -r -p $(ps aux | grep SampleApp | awk '{print $2}' | head -n1) 5
