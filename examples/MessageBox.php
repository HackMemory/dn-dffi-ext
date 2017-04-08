<?php

DFFI::callFunction("user32", "bool", "MessageBoxA", [0, "HelloWorld", "", 0], ["int", "string", "string", "int"]);
