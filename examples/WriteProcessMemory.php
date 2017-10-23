<?php

$user32 = new DFFI("user32");
$kernel32 = new DFFI("kernel32");

$all_access = 0x000F0000 | 0x00100000 | 0xFFFF;
$base = 0x6FFAE0; // значение взять из консоли (victim.exe)
$newValue = 100;

$hwnd = $user32->FindWindowA("int", [null, "Victim"], ["int", "string"]); //получаем хендл окна
if($hwnd == 0){
	alert("Victim not found");
}else{
	$pid_ref = new DFFIReferenceValue("int");
	$user32->GetWindowThreadProcessId("int", [$hwnd, $pid_ref], ["int", "reference"]);//получаем pid процесса
	$pid = $pid_ref->getValue();

	$hOpen = $kernel32->OpenProcess("int", [$all_access, false, $pid], ["int", "bool", "int"]);
	if(!$hOpen){
		alert("Process error");
	}else{
		$newValue_ref = new DFFIReferenceValue("int", $newValue);
		$kernel32->WriteProcessMemory("bool",
			[$hOpen, $base, $newValue_ref, sizeof($newValue), null],
			["int", "int", "reference", "int", "int"]
		);
	}
}

