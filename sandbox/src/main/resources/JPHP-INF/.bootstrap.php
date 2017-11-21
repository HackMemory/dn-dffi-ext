<?php

use system\DFFI;


class Main
{   	
	function __construct(){
		$user32 = new DFFI("user32");
		$user32->bind("MessageBoxA", "int", ["int", "string", "string", "int"]);
	}
	
    function run()
    {
		DFFI::MessageBoxA(0, "Hello", "HelloWorld", 0);
    }
}

new Main()->run();