param([switch]$Test)
$ErrorActionPreference = 'Stop'
Push-Location $PSScriptRoot
try
{
    New-Item -ItemType Directory -Force -Path 'build/classes' | Out-Null
    $sourceRoots = @('src/main/java')
    if ($Test) { $sourceRoots += 'src/test/java' }
    $sources = Get-ChildItem -Path $sourceRoots -Recurse -Filter '*.java' | ForEach-Object { $_.FullName }
    & javac -encoding UTF-8 -cp 'lib/*' -d 'build/classes' $sources
    if ($LASTEXITCODE -ne 0) { throw 'Compilation impossible.' }
    if ($Test)
    {
        & java -ea -cp 'build/classes;lib/*' ma.youcode.lineperm.dao.LogDaoTest
        if ($LASTEXITCODE -ne 0) { throw 'Echec des tests LogDao.' }
        & java -ea -cp 'build/classes;lib/*' ma.youcode.lineperm.ui.ConsolePersistenceTest
        if ($LASTEXITCODE -ne 0) { throw 'Echec des tests console.' }
    }
    else
    {
        & java -cp 'build/classes;lib/*' ma.youcode.lineperm.Main
        if ($LASTEXITCODE -ne 0) { throw 'Echec du lancement.' }
    }
}
finally
{
    Pop-Location
}
