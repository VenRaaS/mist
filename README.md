# Goal
Mist is a client tool takes charge of Upload/Download file Into/From PaaS storage, i.e. Azure storage, AWS S3 and Google cloud storage, efficiently with progress status.

# Status
## Azure
* parallel **upload** with progress
* parallel **download** with progress
* simple file prefix pattern match include upload and download

![](https://lh5.googleusercontent.com/TnDvg2N7Bs-YA87pX_3eP7bhmwZtJqYzsg-LySnMVjTnh8CDW5HxpqHi-01GJZ-FN4L28lsVDttWb44=w1576-h693)

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
