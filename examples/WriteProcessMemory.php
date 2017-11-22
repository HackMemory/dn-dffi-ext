<?php
use system\DFFI;
use system\DFFIType;

//Описываем функции
$user32 = new DFFI("user32");
$user32->bind("FindWindowA", DFFIType::INT, [DFFIType::INT, DFFIType::STRING]);
$user32->bind("GetWindowThreadProcessId", DFFIType::INT, [DFFIType::INT, DFFIType::REFERENCE]);

$kernel32 = new DFFI("kernel32");
$kernel32->bind("OpenProcess", DFFIType::INT, [DFFIType::INT, DFFIType::BOOL, DFFIType::INT]);
$kernel32->bind("WriteProcessMemory", DFFIType::BOOL, [DFFIType::INT, DFFIType::INT, DFFIType::REFERENCE, DFFIType::INT, DFFIType::INT]);


$all_access = 0x000F0000 | 0x00100000 | 0xFFFF;
$base = 0x6FFAE0; // адрес
$newValue = 100;
$hwnd = DFFI::FindWindowA(null, "MainWindow"]); //получаем хендл окна
if($hwnd == 0){
	alert("Window not found");
} else {
	$pid_ref = new DFFIReferenceValue("int");
	DFFI::GetWindowThreadProcessId($hwnd, $pid_ref); //получаем pid процесса
	$pid = $pid_ref->getValue();
	
	$hOpen = DFFI::OpenProcess($all_access, false, $pid);
	if(!$hOpen){
		alert("Process error");
	}else{
		$newValue_ref = new DFFIReferenceValue("int", $newValue);
		DFFI::WriteProcessMemory($hOpen, $base, $newValue_ref, sizeof($newValue));
	}
}