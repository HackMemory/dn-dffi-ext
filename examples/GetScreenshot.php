<?php

$hScreenDC = DFFI::callFunction("gdi32", "int", "CreateDCA", ["DISPLAY", 0, 0, 0], ["string", "int", "int", "int"]);
$hMemoryDC = DFFI::callFunction("gdi32", "int", "CreateCompatibleDC", [$hScreenDC], ["int"]);

$width = DFFI::callFunction("gdi32", "int", "GetDeviceCaps", [$hMemoryDC, 8], ["int", "int"]);
$height = DFFI::callFunction("gdi32", "int", "GetDeviceCaps", [$hMemoryDC, 10], ["int", "int"]);

$hBitmap = DFFI::callFunction("gdi32", "int", "CreateCompatibleBitmap", [$hScreenDC, $width, $height], ["int", "int", "int"]);
$hOldBitmap = DFFI::callFunction("gdi32", "int", "SelectObject", [$hMemoryDC, $hBitmap], ["int", "int"]);

DFFI::callFunction("gdi32", "bool", "BitBlt", [$hMemoryDC, 0, 0, $width, $height, $hScreenDC, 0, 0, 0x00CC0020], ["int", "int", "int","int", "int", "int","int", "int", "int",]);
DFFI::callFunction("gdi32", "int", "SelectObject", [$hMemoryDC, $hOldBitmap], ["int", "int"]);

DFFI::callFunction("user32", "bool", "OpenClipboard", [0], ["int"]);
DFFI::callFunction("user32", "bool", "EmptyClipboard", [], []);
DFFI::callFunction("user32", "bool", "SetClipboardData", [2, $hBitmap], ["int", "int"]);
DFFI::callFunction("user32", "bool", "CloseClipboard", [], []);

DFFI::callFunction("gdi32", "bool", "DeleteDC", ["int"], [$hMemoryDC]);
DFFI::callFunction("gdi32", "bool", "DeleteDC", ["int"], [$hMemoryDC]);
