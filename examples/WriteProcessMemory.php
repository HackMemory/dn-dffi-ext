<?php

function write(){
	$all_access = 0x000F0000 | 0x00100000 | 0xFFFF;
	$base = 0x6FFAE0; // значение взять из консоли (victim.exe)
	$newValue = 100;
	
	
	$hwnd = DFFI::callFunction("user32", "int", "FindWindowA", [null, "Victim"], ["int", "string"]); //получаем хендл окна
	if($hwnd == 0){
		alert("Victim not found");
	}else{
		$pid_ref = new DFFIReferenceValue("int");
		DFFI::callFunction("user32", "int", "GetWindowThreadProcessId", [$hwnd, $pid_ref], ["int", "reference"]);//получаем pid процесса
		$pid = $pid_ref->getValue();
		
		$hOpen = DFFI::callFunction("kernel32", "int", "OpenProcess", [$all_access, false, $pid], ["int", "bool", "int"]);
		if(!$hOpen){
			alert("Process error");
		}else{
			$newValue_ref = new DFFIReferenceValue("int", $newValue);
			DFFI::callFunction("kernel32", "bool", "WriteProcessMemory",     //записываем значение
				[$hOpen, $base, $newValue_ref, sizeof($newValue), null],
				["int", "int", "reference", "int", "int"]
			);
		}
	}
}
