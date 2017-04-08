<?php

$point = new DFFIStruct("POINT", ["int", "int"]);
DFFI::callFunction("user32", "int", "GetCursorPos", [$point], ["struct"]);
pre($point->getResponse());
