## Zip Extension
> For JPHP and DevelNext ([http://develnext.org](http://develnext.org)).

An extension for using zip archive in jphp and develnext. It contains two classes - `ZipFile` and the `ZipFileScript` component for DevelNext IDE.

---

Расширение для использования zip архивов в jphp и develnext. Оно содержит 2 класса - `ZipFile` и `ZipFileScript` компонент для среды DevelNext.

[Последняя версия (скачать)](https://github.com/jphp-compiler/jphp-zip-ext/releases/download/1.0/dn-zip-bundle.dnbundle) / [Демо Проект](http://develnext.org/project/SoCGrCEGdI)

![image](https://cloud.githubusercontent.com/assets/1113915/25070799/756a4a2e-22b0-11e7-946b-27e7ff457ca0.png)


### Example
> Примеры

```php
use php\compress\ZipFile;
// or
use compress;

// create zip archive from directory...
$zipFile = ZipFile::create('my.zip');
$zipFile->addDirectory('path/to/dir');

// unpack zip archive
$zipFile = new ZipFile('my.zip');
$zipFile->unpack('path/to/dir');
```
    
    
### API
    
```php
/**
 * Class ZipFile
 * @package php\compress
 */
class ZipFile
{
    /**
     * Create ZIP Archive.
     *
     * @param string $file
     * @param bool $rewrite
     * @return ZipFile
     */
    public static function create($file, $rewrite = true)
    {
    }

    /**
     * ZipFile constructor.
     * @param $file
     * @param bool $create
     */
    public function __construct($file, $create = false)
    {
    }

    /**
     * Extract zip archive content to directory.
     *
     * @param string $toDirectory
     * @param string $charset
     * @param callable $callback ($name)
     */
    public function unpack($toDirectory, $charset = null, callable $callback = null)
    {
    }

    /**
     * Read one zip entry from archive.
     * @param string $path
     * @param callable $reader (array $stat, Stream $stream)
     */
    public function read($path, callable $reader)
    {
    }

    /**
     * Read all zip entries from archive.
     * @param callable $reader (array $stat, Stream $stream)
     */
    public function readAll(callable $reader)
    {
    }

    /**
     * Returns stat of one zip entry by path.
     * [name, size, compressedSize, time, crc, comment, method]
     *
     * @param string $path
     * @return array
     */
    public function stat($path)
    {
    }

    /**
     * Returns all stats of zip archive.
     * @return array[]
     */
    public function statAll()
    {
    }

    /**
     * Checks zip entry exist by path.
     * @param $path
     * @return bool
     */
    public function has($path)
    {
    }

    /**
     * Add stream or file to archive.
     *
     * @param string $path
     * @param Stream|File|string $source
     * @param int $compressLevel
     */
    public function add($path, $source, $compressLevel = -1)
    {
    }

    /**
     * Add all files of directory to archive.
     *
     * @param string $dir
     * @param int $compressLevel
     * @param callable $callback
     */
    public function addDirectory($dir, $compressLevel = -1, callable $callback = null)
    {
    }

    /**
     * Add zip entry from string.
     * @param string $path
     * @param string $string
     * @param int $compressLevel
     */
    public function addFromString($path, $string, $compressLevel = -1)
    {
    }

    /**
     * Remove zip entry by its path.
     * @param string|array $path
     */
    public function remove($path)
    {
    }

    /**
     * @return string
     */
    public function getPath()
    {
    }
}
```    


### To Build
> Как собрать?

1. Open your shell (bash or cmd):
2. Use gradlew command:

```bash
// On Windows
gradlew bundle -Drelease=true

// On Linux
chmodx +x gradlew
./gradlew bundle -Drelease=true
```

3. See the `dn-zip-bundle/build` directory to find the build DevelNext bundle.
