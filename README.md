# Goal
Mist is a client tool takes charge of Upload/Download file Into/From PaaS storage, i.e. Azure storage, AWS S3 and Google cloud storage, efficiently with progress status.

# Status
## Azure
* parallel **upload** with progress
* parallel **download** with progress
* simple file prefix pattern match include upload and download

![](https://lh4.googleusercontent.com/jkouZaq6XHvW9luaXeK_U2uGX1qVo64jylFWlRx-pUpHzzhnFz5tTDphO7Xb3kLSSzRkYixpOKbho0Q=w1342-h561)

![](https://lh4.googleusercontent.com/5gwCNV7ck7WkNNcWaycb7NlCT9GTLQ2dGVNu8BjJyLlIYBjqmCPRUNOYf04t95L5i1hHmjR7YcWKZZM=w1342-h561)

# Getting Started
## Usage
### Prequisite
* JRE 1.7+
* Available memory > 1.5 * $FileSize

### Upload a single file from local to cloud
```
java -jar mist-self-executing.jar -ak <account key> -an <account name> -c <container> -ffp recomdlog_20150115 -fn up
```
### Download a single file from cloud to local
```
java -jar mist-self-executing.jar -ak <account key> -an <account name> -c <container> -ffp recomdlog_20150115 -fn down
```
### Upload a bunch of files with a common prefix pattern
* the pattern should be doulbe-quote **"** surrounded
```
java -jar mist-self-executing.jar -ak <account key> -an <account name> -c <container> -ffp "recomdlog_*" -fn up
```
