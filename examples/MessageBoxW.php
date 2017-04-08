<?php

DFFI::callFunction("user32", "bool", "MessageBoxW", [0, "HelloWorld", "", 0], ["int", "wstring", "wstring", "int"]);
