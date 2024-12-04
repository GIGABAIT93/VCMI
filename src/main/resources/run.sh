#!/bin/bash
# TENSA example script by GIGABAIT

# Command: bash run shell <command>
str=""
for arg in "$@"; do
  str="$str $arg"
done
echo "&6--------------------------------------------------------------"
if [[ $1 == "shell" ]]; then
  find="shell"
  replace=""
  result=${str//$find/$replace}
  $result
  echo "&6--------------------------------------------------------------"
  exit
fi


echo "&7You have run the script: &3&l$0
&7With arguments:&3&l$str"
echo "&6--------------------------------------------------------------"
