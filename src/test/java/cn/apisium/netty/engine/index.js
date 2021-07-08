require('socket.io-client')('http://127.0.0.1:2333/?token=233').on('connect', () => {
  console.log(2333)
  io.emit('test', 2333)
}).on('test2', console.log)
