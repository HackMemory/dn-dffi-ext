<?php
use system\DFFI;
use system\DFFIType;

$user32 = new DFFI("user32");
$user32->bind("GetCursorPos", DFFIType::INT, [DFFIType::STRUCT]);

$point = new DFFIStruct("POINT", ["int", "int"]);
DFFI::GetCursorPos($point);
pre($point->getResponse());
