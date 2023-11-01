# uploadtest
demonstration of a weird file streaming problem


# start server

```
sbt "runMain com.example.uploadtest.Main"
```

# Upload large file

I have a 1.8GB file stored in: `~/tmp/bigdatasource.csv`

```
$ curl -v --request POST \
  --url http://localhost:8080/dataSource \
  --header 'Content-Type: multipart/form-data' \
  --form file=@$HOME/tmp/bigdatasource.csv
```


