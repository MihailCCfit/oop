@echo off
set compile = 0
set doc = 0
set execute = 0
for %%i in (%*) do (
    if %%i == -c (
        set /A compile = 1
        goto continue
    )
    if %%i == -d (
        set /A doc = 1
        goto continue
    )
    if %%i == -e (
        set /A execute = 1
        goto continue
    )
    set flag=%%i
    goto error
    :continue
    rem Continue
)
if [%compile%] == [1] javac main/java/Main.java main/java/Heap.java -d forScript
if [%doc%] == [1] javadoc main/java/Main.java main/java/Heap.java -d forScript/doc
if [%execute%] == [1] java -cp "forScript" main/java/Main.java
: main/java/Main.java main/java/Heap.java
:error
if NOT [%flag%] == [] echo Error with keys %flag%