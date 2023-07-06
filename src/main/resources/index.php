<?php
// VCMI example script by GIGABAIT
$argv_line = "";

foreach ($argv as $key => $value) {
  if ($key == 0) {
    continue;
  }
  $argv_line .= " " . $value;
}
echo "&6--------------------------------------------------------------";
echo "\n&7You have run the script: &3&l$argv[0]\n&7With arguments:&3&l$argv_line";
echo "\n&6--------------------------------------------------------------\n";

// echo json_encode($argv);
