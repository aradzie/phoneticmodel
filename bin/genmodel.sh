#!/usr/bin/env bash

cp="$( dirname "$( readlink -f "${BASH_SOURCE[0]}" ) ")/*"

java -cp "${cp}" com.keybr.phoneticmodel.Main "${@:1}"
