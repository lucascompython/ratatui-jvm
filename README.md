# Ratatui bindings for Java/JVM

This is a Java/JVM binding for the [Ratatui](https://github.com/ratatui-org/ratatui) library.  
This project uses the [FFM API](https://openjdk.org/jeps/454) that was introduced in Java 22.

## Compiling

```bash
# Compile the Rust library
cd rust
cargo build --release

# Download the jextract files if you haven't already
cd ../jextract
./setup 

# Compile the Java Bindings
cd ../java/Ratatui-JVM
./gradlew run
# If it doesn't work, try running the following command
./gradlew installDist
# then
./build/install/Ratatui-JVM/bin/Ratatui-JVM # with the proper environment variable
```
