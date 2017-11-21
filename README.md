# DevelNext Foreign Function Interface

#### *Example*

```php
<?php
use system\DFFI;

$user32 = new DFFI("user32");
$user32->bind("MessageBoxA", "int", ["int", "string", "string", "int"]);

DFFI::MessageBoxA(0, "Hello", "HelloWorld", 0);
```
