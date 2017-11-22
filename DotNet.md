## Как подключить DLL написанную в C#

1) Создать проект Class Library
2) Перейти в: PROJECT -> Manage NuGet Packages... В поиске ввести **UnmanagedExports**, и установить **Unmanaged Exports (DllExport for .Net)**
3) Теперь чтобы функции можно было вызывать, нужно их экспортировать. А для это перед каждой функицей прописываем **[RGiesecke.DllExport.DllExport]**
4) **Собирать нужно, учитывая разрядность системы.** Если собираете **Portable версию** проекта, **то нужно и саму DLL собирать под x86**, т.к. DN использует **32-битную Java**. Но если сама система **64-битная**, соотвественно нужно собирать той же разрядности

### Пример:
```c#
namespace ExportFunc
{
    public class Class1
    {
        [RGiesecke.DllExport.DllExport]
        public static void testMessage(string data)
        {
            MessageBox.Show(data);
        }

        [RGiesecke.DllExport.DllExport]
        public static int sum(int a, int b)
        {
            return a + b;
        }
    }
}
```

### Вызов из DN:
```php
<?php
use system\DFFI;
use system\DFFIType;

$mylib = new DFFI("DllName.dll");
$mylib->bind("testMessage", DFFIType::INT, [DFFIType::STRING]);
$mylib->bind("sum", DFFIType::INT, [DFFIType::INT, DFFIType::INT]);

DFFI::testMessage("Hello");
var_dump(DFFI::sum(5, 10)); //output: 15
```
