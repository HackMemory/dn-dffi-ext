# dn-dffi-ext

### DFFI

#### *Типы*
* String
* WString
* Int
* Char
* Long
* Float
* Double
* Struct
* Reference

#### *Функции*
```php 
DFFI::callFunction($lib, $returnType, $functionName, Array $args, Array $types);
DFFI::addSearchPath($lib, $path);
```

### DFFIReferenceValue ссылки на переменные

#### *Функции*
```php
DFFIReferenceValue($type);
DFFIReferenceValue($type, $value);
setValue($value);
getValue();
```

### DFFIStruct для работы со структурами

#### *Функции*
```php
DFFIStruct($name, Array $types);
getResponse();
```

### DFFIClassLoader работа с java библиотеками

#### *Типы*
* String
* Int
* Char
* Long
* Float
* Double

#### *Функции*
```php
DFFIClassLoader($lib, $class, Array $args, Array $types);
callMethod($functionName, $returnType, Array $args, Array $types);
DFFIClassLoader::callStaticMethod($lib, $class, $functionName, $returnType, Array $args, Array $types);
```


### Примеры
* Byte Array:
```php
$bytearray = "909090"; // тоже самое, что и 0x90 0x90 0x90
DFFI::callFunction("lib.dll", "int", "testFunction", [$bytearray], ["bytearray"]);
```

* DFFIClassLoader:
```php
$str = new DFFIClassLoader("", "java.lang.String", ["Hello World!"], ["string"]);
$str2 = $str->callMethod("replaceAll", "string", ["World", "Java"], ["string", "string"]);
pre($str2);

$hex = DFFIClassLoader::callStaticMethod("", "java.lang.Integer", "toHexString", "string", [33], ["int"]);
pre($hex);
```

DevelNext Foreign Function Interface
