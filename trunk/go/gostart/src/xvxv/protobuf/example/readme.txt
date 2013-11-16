create protoc code

java
protoc --java_out=. user.proto

go
protoc --plugin=protoc-gen-go=protoc-gen-go.exe --go_out=. user.proto