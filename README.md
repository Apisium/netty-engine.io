# netty-engine.io

A new version of engine.io wrapper based on netty.

## Usage

Add repository:

```groovy
repositories {
    maven {
        name 'jitpack'
        url 'https://www.jitpack.io'
    }
}
```

The add dependencies:

```groovy
dependencies {
    implementation 'com.github.Apisium:netty-engine.io:1.0'
    implementation 'io.netty:netty-all:4.1.65.Final'
    implementation 'io.socket:engine.io-server:5.0.0'
    testImplementation ('io.socket:socket.io-server:3.0.1') {
        exclude group: 'io.socket', module: 'engine.io-server' // Avoid old engine.io
    }
}
```

## Example

[Full example](./src/test/java/cn/apisium/netty/engineio)

```java
import cn.apisium.netty.engineio.EngineIoHandler;
import io.socket.engineio.server.EngineIoServer;
import io.socket.socketio.server.SocketIoServer;

public class Main {
    public static void main(String[] args) {
        EngineIoServer engineIoServer = new EngineIoServer();
        SocketIoServer socketIoServer = new SocketIoServer(engineIoServer);
        
        Channel ch; // ch = ctx.channel();
        ch.pipeline().addLast(new EngineIoHandler(engineIoServer));
    }
}
```

## Author

Shirasawa

## License

[MIT](./LICENSE)
